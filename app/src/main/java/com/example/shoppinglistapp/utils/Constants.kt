package com.example.shoppinglistapp.utils

import android.os.Build

object Constants {
    var IMAGE_URL_UPDATION = ""
    const val BASE_URL = "https://pixabay.com"
    const val API_KEY = "25417538-c06d34656ce070ff6ba02ddbd"
    const val DATABASE_NAME = "ShoppingItemDatabase"

    const val MAX_NAME_LENGTH = 20
    const val MAX_PRICE_LENGTH = 10

    const val SEARCH_TIME_DELAY = 300L
    const val GRID_SPAN_COUNT = 3

    const val REQUEST_KEY = "requestKey"
    const val BUNDLE_KEY = "bundleKey"
}
inline fun <T> sdk29AndUp(onSdk29: () -> T): T? {
    return if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        onSdk29()
    } else null
}





