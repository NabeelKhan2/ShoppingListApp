package com.example.shoppinglistapp.ui.addfragment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.data.local.ShoppingItem
import com.example.shoppinglistapp.data.repository.ShoppingRepo
import com.example.shoppinglistapp.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddShoppingItemViewModel @Inject constructor(
    private val repository: ShoppingRepo
) : ViewModel() {

    private val _image = MutableStateFlow<State>(State.Idle)
    val image = _image.asStateFlow()

    private val eventChannel = Channel<Event>()
    val eventFLow = eventChannel.receiveAsFlow()

    private fun insertShoppingItemIntoDb(shoppingItem: ShoppingItem) = viewModelScope.launch {
        repository.insertShoppingItem(shoppingItem)
    }

    fun currentImage(url: String?) {
        _image.value = State.Image(url)
    }

    fun insertShoppingItem(
        name: String,
        amountString: String,
        priceString: String,
        image: String?
    ) {
        viewModelScope.launch {
            if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
                eventChannel.send(Event.Error("The fields must not be empty"))
                return@launch
            }
            if (name.length > Constants.MAX_NAME_LENGTH) {
                eventChannel.send(
                    Event.Error(
                        "The name of the item" +
                                "must not exceed ${Constants.MAX_NAME_LENGTH} characters"
                    )
                )
                return@launch
            }
            if (priceString.length > Constants.MAX_PRICE_LENGTH) {
                eventChannel.send(
                    Event.Error(
                        "The price of the item" +
                                "must not exceed ${Constants.MAX_PRICE_LENGTH} characters"
                    )
                )
                return@launch
            }
            val amount = try {
                amountString.toInt()
            } catch (e: Exception) {
                eventChannel.send(Event.Error("Please enter a valid amount"))
                return@launch
            }
            val shoppingItem =
                ShoppingItem(name, amount, priceString.toFloat(), image ?: "")
            insertShoppingItemIntoDb(shoppingItem)
            eventChannel.send(Event.Success(shoppingItem))
        }
    }


    sealed class Event {
        data class Success(val image: ShoppingItem) : Event()
        data class Error(val msg: String?) : Event()
    }

    sealed class State {
        data class Image(val image: String?) : State()
        object Idle : State()
    }

}