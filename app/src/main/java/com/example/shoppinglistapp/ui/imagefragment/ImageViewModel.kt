package com.example.shoppinglistapp.ui.imagefragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.data.remote.response.ImageResponse
import com.example.shoppinglistapp.data.repository.ShoppingRepo
import com.example.shoppinglistapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val repository: ShoppingRepo
) : ViewModel() {

    private val _images = MutableStateFlow<State>(State.Idle)
    val images = _images.asStateFlow()


    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }
        viewModelScope.launch {
            repository.searchForImage(imageQuery).collect {
                _images.value = State.Loading
                when (it) {
                    is Resource.Success -> {
                        _images.value = State.Success(it.data)
                    }
                    is Resource.Error -> State.Error(it.message)
                }
            }
        }
    }


    sealed class State {
        data class Success(val image: ImageResponse?) : State()
        data class Error(val msg: String?) : State()
        object Idle : State()
        object Loading : State()
    }

}