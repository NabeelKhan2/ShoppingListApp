package com.example.shoppinglistapp.ui.addandupdatefragment

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
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
abstract class AddAndUpdateBaseFragment : Fragment(R.layout.fragment_add_shopping_item) {

    private var _binding: FragmentAddShoppingItemBinding? = null
    val binding get() = _binding!!

    private var result: String? = null

    val viewModel by viewModels<AddShoppingItemViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentAddShoppingItemBinding.bind(view)

        setFragmentResultListener(Constants.REQUEST_KEY) { _, bundle ->
            result = bundle.getString(Constants.BUNDLE_KEY)
            viewModel.image.value = result
        }

        subscribeToObservers()
    }


    private fun subscribeToObservers() {

        lifecycleScope.launchWhenStarted {
            viewModel.image.collect {
                it?.let { imageUrl ->
                    if (imageUrl.isNotEmpty()) binding.ivShoppingImage.load(imageUrl)
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.eventFLow.collect { event ->
                when (event) {
                    is AddShoppingItemViewModel.Event.Success -> {

                        snackBar("Added Shopping Item")
                        findNavController().navigateUp()

                    }

                    is AddShoppingItemViewModel.Event.Error -> {
                        snackBar(event.msg ?: "An unknown error occurred")
                    }
                }
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}