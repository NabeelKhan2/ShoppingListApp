package com.example.shoppinglistapp.ui.addfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.FragmentAddShoppingItemBinding

class AddShoppingItemFragment : Fragment(R.layout.fragment_add_shopping_item) {

    private var _binding : FragmentAddShoppingItemBinding? = null
    val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentAddShoppingItemBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}