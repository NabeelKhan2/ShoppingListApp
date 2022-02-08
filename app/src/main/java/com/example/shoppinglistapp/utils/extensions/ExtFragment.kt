package com.example.shoppinglistapp.utils.extensions

import android.content.DialogInterface
import android.widget.Toast
import androidx.annotation.StyleRes
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

fun Fragment.toast(content: Any = "", length: Int = Toast.LENGTH_SHORT) =
    requireContext().toast(content, length)

fun Fragment.snackBar(content: Any = "" , length: Int = Snackbar.LENGTH_SHORT) {
    Snackbar.make(requireView() , content.toString() , length).show()
}

fun Fragment.alert(
    @StyleRes style: Int = 0,
    dialogBuilder: MaterialAlertDialogBuilder.() -> Unit
) {
    MaterialAlertDialogBuilder(requireContext(), style)
        .apply {
            setCancelable(false)
            dialogBuilder()
            create()
            show()
        }
}

fun MaterialAlertDialogBuilder.negativeButton(
    text: String = "No",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setNegativeButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}

fun MaterialAlertDialogBuilder.positiveButton(
    text: String = "Yes",
    handleClick: (dialogInterface: DialogInterface) -> Unit = { it.dismiss() }
) {
    this.setPositiveButton(text) { dialogInterface, _ -> handleClick(dialogInterface) }
}





