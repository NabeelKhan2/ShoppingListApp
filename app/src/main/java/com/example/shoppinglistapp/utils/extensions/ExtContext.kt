package com.example.shoppinglistapp.utils.extensions

import android.content.Context
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

fun Context.toast(content: Any = "", length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, content.toString(), length).show()
}

