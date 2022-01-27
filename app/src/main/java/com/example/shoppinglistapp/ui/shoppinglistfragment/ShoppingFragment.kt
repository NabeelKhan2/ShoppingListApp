package com.example.shoppinglistapp.ui.shoppinglistfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import androidx.fragment.app.viewModels
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.FragmentShoppingBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.LEFT
import androidx.recyclerview.widget.ItemTouchHelper.RIGHT
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglistapp.ui.ShoppingViewModel
import com.example.shoppinglistapp.ui.adapters.ShoppingItemAdapter
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

@AndroidEntryPoint
class ShoppingFragment @Inject constructor(
    val shoppingItemAdapter: ShoppingItemAdapter
) : Fragment(R.layout.fragment_shopping) {
    private var _binding : FragmentShoppingBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<ShoppingViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShoppingBinding.bind(view)

        subscribeToObservers()
        setupRecyclerView()

        binding.fabAddShoppingItem.setOnClickListener {
            findNavController().navigate(
                ShoppingFragmentDirections.actionShoppingFragmentToAddShoppingItemFragment()
            )
        }

    }

    private val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(
        0, LEFT or RIGHT
    ) {
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ) = true

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val pos = viewHolder.layoutPosition
            val item = shoppingItemAdapter.shoppingItems[pos]
            viewModel?.deleteShoppingItem(item)
            Snackbar.make(requireView(), "Successfully deleted item", Snackbar.LENGTH_LONG).apply {
                setAction("Undo") {
                    viewModel?.insertShoppingItemIntoDb(item)
                }
                show()
            }
        }
    }

    private fun subscribeToObservers() {

        lifecycleScope.launchWhenStarted {
            viewModel.insertShoppingItemStatus.collect {
                when(it){
                    is ShoppingViewModel.State.Success -> {
                        shoppingItemAdapter.shoppingItems = it.shoppingItem
                    }
                    else -> {}
                }
            }
        }


//        viewModel?.totalPrice?.observe(viewLifecycleOwner, Observer {
//            val price = it ?: 0f
//            val priceText = "Total Price: $price€"
//            tvShoppingItemPrice.text = priceText
//        })
    }

    private fun setupRecyclerView() {
        binding.rvShoppingItems.apply {
            adapter = shoppingItemAdapter
            layoutManager = LinearLayoutManager(requireContext())
            ItemTouchHelper(itemTouchCallback).attachToRecyclerView(this)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}