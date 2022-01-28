package com.example.shoppinglistapp.utils.extensions

import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

fun Fragment.toast(content: Any = "", length: Int = Toast.LENGTH_SHORT) =
    requireContext().toast(content, length)

fun Fragment.snackBar(content: Any = "" , length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(requireView() , content.toString() , length).show()
}