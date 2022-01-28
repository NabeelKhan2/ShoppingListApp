package com.example.shoppinglistapp.ui.addfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.FragmentAddShoppingItemBinding
import com.example.shoppinglistapp.ui.sharedviewmodel.ShoppingViewModel
import com.example.shoppinglistapp.utils.extensions.snackBar
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
            viewModel.insertShoppingItem(
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

//        val callback = object : OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                viewModel.setCurImageUrl("")
//                findNavController().popBackStack()
//            }
//        }
//        requireActivity().onBackPressedDispatcher.addCallback(callback)

    }

    private fun subscribeToObservers() {

        lifecycleScope.launchWhenStarted {
            viewModel.curImageUrl.collect {
                binding.ivShoppingImage.load(it)
            }
        }

        lifecycleScope.launchWhenStarted {

            viewModel.imageState.collect {
                when (it) {
                    is ShoppingViewModel.Image.Success -> {

                        snackBar("Added Shopping Item")
                        findNavController().popBackStack()

                    }

                    is ShoppingViewModel.Image.Error -> {

                        snackBar(it.msg ?: "An unknown error occurred")
                    }

                    else -> {}
                }
            }
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}