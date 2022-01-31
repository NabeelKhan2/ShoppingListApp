package com.example.shoppinglistapp.ui.shoppinglistfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.data.local.ShoppingItem
import com.example.shoppinglistapp.data.repository.ShoppingRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val repository: ShoppingRepo
) : ViewModel() {

    private val _insertShoppingItemStatus = MutableStateFlow<State>(State.Idle)
    val insertShoppingItemStatus = _insertShoppingItemStatus.asStateFlow()

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

    fun deleteShoppingItem(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.deleteShoppingItem(shoppingItem)
    }

    fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }


    sealed class State {
        data class Success(val shoppingItem: List<ShoppingItem>) : State()
        object Idle : State()
    }


}

