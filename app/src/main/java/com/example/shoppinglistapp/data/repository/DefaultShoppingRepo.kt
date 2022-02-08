package com.example.shoppinglistapp.data.repository

import com.example.shoppinglistapp.data.local.ShoppingDao
import com.example.shoppinglistapp.data.local.ShoppingItem
import com.example.shoppinglistapp.data.remote.Api
import com.example.shoppinglistapp.data.remote.response.ImageResponse
import com.example.shoppinglistapp.utils.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DefaultShoppingRepo @Inject constructor(
    private val shoppingDao: ShoppingDao,
    private val api: Api
) : ShoppingRepo {

    override suspend fun insertShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.insertShoppingItem(shoppingItem)
    }

    override suspend fun deleteShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.deleteShoppingItem(shoppingItem)
    }

    override suspend fun updateShoppingItem(shoppingItem: ShoppingItem) {
        shoppingDao.updateShoppingItem(shoppingItem)
    }

    override fun observeAllShoppingItems(): Flow<List<ShoppingItem>> {
        return shoppingDao.observerAllShoppingItems()
    }

    override fun searchForImage(imageQuery: String): Flow<Resource<ImageResponse>> =
        flow {
            try {
                val response = api.searchForImage(imageQuery)
                val result = response.body()
                if (response.isSuccessful && result != null) {
                    emit(Resource.Success(result))
                } else {
                    emit(Resource.Error<ImageResponse>(response.message()))
                }
            } catch (e: Exception) {
                emit(Resource.Error<ImageResponse>(e.message ?: "An error occurred"))
            }
        }


}