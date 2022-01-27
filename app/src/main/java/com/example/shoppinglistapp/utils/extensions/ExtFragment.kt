package com.example.shoppinglistapp.utils.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment

fun Fragment.toast(content: Any = "", length: Int = Toast.LENGTH_SHORT) =
    requireContext().toast(content, length)