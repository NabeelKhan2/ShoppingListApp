package com.example.shoppinglistapp.ui.imagefragment

import android.app.Application
import android.content.ContentValues
import android.graphics.Bitmap
import android.provider.MediaStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglistapp.data.remote.response.ImageResponse
import com.example.shoppinglistapp.data.repository.ShoppingRepo
import com.example.shoppinglistapp.utils.Resource
import com.example.shoppinglistapp.utils.sdk29AndUp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val app: Application,
    private val repository: ShoppingRepo
) : ViewModel() {

    private val _images = MutableStateFlow<State>(State.Idle)
    val images = _images.asStateFlow()

    fun searchForImage(imageQuery: String) {
        if (imageQuery.isEmpty()) {
            return
        }
        viewModelScope.launch {
            repository.searchForImage(imageQuery).collect { response ->
                _images.value = State.Loading
                when (response) {
                    is Resource.Success -> {
                        _images.value = State.Success(response.data)
                    }
                    is Resource.Error -> State.Error(response.message)
                }
            }
        }
    }

    suspend fun savePhotoToExternalStorage(displayName: String, bmp: Bitmap): Boolean {
        return withContext(Dispatchers.IO) {
            val imageCollection = sdk29AndUp {
                MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
            } ?: MediaStore.Images.Media.EXTERNAL_CONTENT_URI

            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "$displayName.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.WIDTH, bmp.width)
                put(MediaStore.Images.Media.HEIGHT, bmp.height)
            }
            try {
                val contentResolver = app.applicationContext.contentResolver
                contentResolver.insert(imageCollection, contentValues)?.also { uri ->
                    contentResolver.openOutputStream(uri).use { outputStream ->
                        if (!bmp.compress(Bitmap.CompressFormat.PNG, 100, outputStream)) {
                            throw IOException("Couldn't save bitmap")
                        }
                    }
                } ?: throw IOException("Couldn't create MediaStore entry")
                true
            } catch (e: IOException) {
                e.printStackTrace()
                false
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