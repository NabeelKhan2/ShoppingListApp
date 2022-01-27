package com.example.shoppinglistapp.ui.addfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.data.local.ShoppingItem
import com.example.shoppinglistapp.databinding.FragmentAddShoppingItemBinding
import com.example.shoppinglistapp.ui.ShoppingViewModel
import com.example.shoppinglistapp.utils.Constants
import com.example.shoppinglistapp.utils.extensions.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddShoppingItemFragment : Fragment(R.layout.fragment_add_shopping_item) {

    private var _binding: FragmentAddShoppingItemBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ShoppingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddShoppingItemBinding.bind(view)


        subscribeToObservers()

        binding.btnAddShoppingItem.setOnClickListener {
            insertShoppingItem(
                binding.etShoppingItemName.text.toString(),
                binding.etShoppingItemAmount.text.toString(),
                binding.etShoppingItemPrice.text.toString()
            )
        }

        binding.ivShoppingImage.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.setCurImageUrl("")
                findNavController().popBackStack()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)


    }

    private fun subscribeToObservers() {

        lifecycleScope.launchWhenStarted {
            viewModel.curImageUrl.collect {
                binding.ivShoppingImage.load(it)
            }
        }
    }


    private fun insertShoppingItem(name: String, amountString: String, priceString: String) {
        if (name.isEmpty() || amountString.isEmpty() || priceString.isEmpty()) {
            toast("The fields must not be empty")
            return
        }
        if (name.length > Constants.MAX_NAME_LENGTH) {
            toast(
                "The name of the item" +
                        "must not exceed ${Constants.MAX_NAME_LENGTH} characters"
            )
            return
        }
        if (priceString.length > Constants.MAX_PRICE_LENGTH) {
            toast(
                "The price of the item" +
                        "must not exceed ${Constants.MAX_PRICE_LENGTH} characters"
            )
            return
        }
        val amount = try {
            amountString.toInt()
        } catch (e: Exception) {
            toast("Please enter a valid amount")
            return
        }
        val shoppingItem = ShoppingItem(name, amount, priceString.toFloat(), "")
        viewModel.insertShoppingItemIntoDb(shoppingItem)
        viewModel.setCurImageUrl("")
        findNavController().popBackStack()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}