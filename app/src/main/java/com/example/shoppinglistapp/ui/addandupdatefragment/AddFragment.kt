package com.example.shoppinglistapp.ui.addandupdatefragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import com.example.shoppinglistapp.R

class AddFragment : AddAndUpdateBaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {

            ivShoppingImage.setOnClickListener {
                findNavController().navigate(
                    AddFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
                )
            }
            btnAddShoppingItem.run {
                text = context.getString(R.string.btnAdd)
                setOnClickListener {
                    viewModel.insertShoppingItem(
                        etShoppingItemName.text.toString(),
                        etShoppingItemAmount.text.toString(),
                        etShoppingItemPrice.text.toString()
                    )
                }
            }
        }
    }
}