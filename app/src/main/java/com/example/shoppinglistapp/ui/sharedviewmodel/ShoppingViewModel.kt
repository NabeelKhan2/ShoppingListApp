package com.example.shoppinglistapp.ui.sharedviewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.data.local.ShoppingItem
import com.example.shoppinglistapp.data.remote.response.ImageResponse
import com.example.shoppinglistapp.data.repository.ShoppingRepo
import com.example.shoppinglistapp.utils.Constants
import com.example.shoppinglistapp.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepo
) : ViewModel() {

    private val _images = MutableStateFlow<Event>(Event.Idle)
    val images = _images.asStateFlow()

    private val _curImageUrl = MutableStateFlow("")
    val curImageUrl = _curImageUrl.asStateFlow()

    private val _insertShoppingItemStatus = MutableStateFlow<State>(State.Idle)
    val insertShoppingItemStatus = _insertShoppingItemStatus.asStateFlow()

    private val _imageState = MutableStateFlow<Image>(Image.Idle)
    val imageState = _imageState.asStateFlow()

    init {
        observerShoppingItem()
    }

    private fun observerShoppingItem() {
        viewModelScope.launch {
            repository.observeAllShoppingItems().collect {
                _insertShoppingItemStatus.value = State.Success(it)
            }
        }
    }


    fun setCurImageUrl(url: String) {
        _curImageUrl.value = url
    }

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }


    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }
        viewModelScope.launch {
            repository.searchForImage(imageQuery).collect {
                _images.value = Event.Loading
                when (it) {
                    is Resource.Success -> {
                        _images.value = Event.Success(it.data)
                    }
                    is Resource.Error -> Event.Error(it.message)
                }
            }
        }
    }

    fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            _imageState.value = Image.Error("The fields must not be empty")
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            _imageState.value = Image.Error(
                "The name of the item" +
                        "must not exceed ${Constants.MAX_NAME_LENGTH} characters"
            )
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            _imageState.value = Image.Error(
                "The price of the item" +
                        "must not exceed ${Constants.MAX_PRICE_LENGTH} characters"
            )
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            _imageState.value = Image.Error("Please enter a valid amount")
            return
        }
        val shoppingItem =
            ShoppingItem(name, amount, priceString.toFloat(), _curImageUrl.value)
        insertShoppingItemIntoDb(shoppingItem)
        _imageState.value = Image.Success(shoppingItem)
    }


    sealed class Event {
        data class Success(val image: ImageResponse?) : Event()
        data class Error(val msg: String?) : Event()
        object Idle : Event()
        object Loading : Event()
    }

    sealed class State {
        data class Success(val shoppingItem: List<ShoppingItem>) : State()
        object Idle : State()
    }

    sealed class Image {
        data class Success(val image: ShoppingItem) : Image()
        data class Error(val msg: String?) : Image()
        object Idle : Image()
    }

}

