package com.example.shoppinglistapp.data.repository

import com.example.shoppinglistapp.data.local.ShoppingItem
import com.example.shoppinglistapp.data.remote.response.ImageResponse
import com.example.shoppinglistapp.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ShoppingRepo {

    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)
    suspend fun updateShoppingItem(shoppingItem: ShoppingItem)
    fun observeAllShoppingItems(): Flow<List<ShoppingItem>>


    fun searchForImage(imageQuery: String): Flow<Resource<ImageResponse>>

}