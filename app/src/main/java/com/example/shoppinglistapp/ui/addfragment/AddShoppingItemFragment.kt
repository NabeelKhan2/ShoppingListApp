package com.example.shoppinglistapp.ui.addfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import coil.load
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.FragmentAddShoppingItemBinding
import com.example.shoppinglistapp.utils.Constants
import com.example.shoppinglistapp.utils.extensions.snackBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class AddShoppingItemFragment : Fragment(R.layout.fragment_add_shopping_item) {

    private var _binding: FragmentAddShoppingItemBinding? = null
    private val binding get() = _binding!!

    private var result: String? = null

    private val viewModel by viewModels<AddShoppingItemViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddShoppingItemBinding.bind(view)

        binding.btnAddShoppingItem.setOnClickListener {
            viewModel.insertShoppingItem(
                binding.etShoppingItemName.text.toString(),
                binding.etShoppingItemAmount.text.toString(),
                binding.etShoppingItemPrice.text.toString(),
                result
            )
        }

        setFragmentResultListener(Constants.REQUEST_KEY) { _, bundle ->
            result = bundle.getString(Constants.BUNDLE_KEY)
            viewModel.currentImage(result)
        }

        binding.ivShoppingImage.setOnClickListener {
            findNavController().navigate(
                AddShoppingItemFragmentDirections.actionAddShoppingItemFragmentToImagePickFragment()
            )
        }

        subscribeToObservers()
    }

    private fun subscribeToObservers() {

        lifecycleScope.launchWhenStarted {
            viewModel.image.collect {
                when(it){
                    is AddShoppingItemViewModel.State.Image -> {
                        binding.ivShoppingImage.load(it.image)
                    }
                    else -> {}
                }
            }
        }

        lifecycleScope.launchWhenStarted {

            viewModel.eventFLow.collect {
                when (it) {
                    is AddShoppingItemViewModel.Event.Success -> {

                        snackBar("Added Shopping Item")
                        findNavController().navigateUp()

                    }

                    is AddShoppingItemViewModel.Event.Error -> {
                        snackBar(it.msg ?: "An unknown error occurred")
                    }
                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}