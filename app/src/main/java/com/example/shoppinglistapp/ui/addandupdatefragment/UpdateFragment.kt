package com.example.shoppinglistapp.ui.addandupdatefragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import coil.load
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.data.local.ShoppingItem
import com.example.shoppinglistapp.utils.Constants
import com.example.shoppinglistapp.utils.extensions.snackBar

class UpdateFragment : AddAndUpdateBaseFragment() {

    private val args by navArgs<UpdateFragmentArgs>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            args.shoppingItem?.run {
                etShoppingItemName.setText(name)
                etShoppingItemAmount.setText(amount.toString())
                etShoppingItemPrice.setText(price.toInt().toString())
                ivShoppingImage.load(imageUrl)
            }

            btnAddShoppingItem.run {
                text = context.getString(R.string.btnUpdate)
                setOnClickListener {
                    updateItem()
                }
            }

            ivShoppingImage.setOnClickListener {
                findNavController().navigate(
                    UpdateFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
                )
            }
        }
    }

    private fun updateItem() {

        binding.apply {
            val itemName = etShoppingItemName.text.toString()
            val itemAmount = etShoppingItemAmount.text.toString()
            val itemPrice = etShoppingItemPrice.text.toString()
            val image = Constants.IMAGE_URL_UPDATION

            if (inputCheck(itemName, itemAmount, itemPrice)) {
                val updatedItem =
                    ShoppingItem(
                        itemName,
                        itemAmount.toInt(),
                        itemPrice.toFloat(),
                        image,
                        args.shoppingItem?.id
                    )
                viewModel.updateShoppingItem(updatedItem)

                snackBar("Updated Shopping Item")
                findNavController().navigateUp()

            } else {
                snackBar("please fill out the field")
            }
        }
    }

    private fun inputCheck(itemName: String, itemAmount: String, itemPrice: String): Boolean {
        return !(itemName.isEmpty() || itemAmount.isEmpty() || itemPrice.isEmpty())
    }
}