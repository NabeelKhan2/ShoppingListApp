package com.example.shoppinglistapp.utils.extensions

import android.content.Context
import android.widget.Toast

fun Context.toast(content: Any = "", length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, content.toString(), length).show()
}