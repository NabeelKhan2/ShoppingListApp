package com.example.shoppinglistapp.ui.shoppinglistfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.FragmentShoppingBinding

class ShoppingFragment : Fragment(R.layout.fragment_shopping) {
    private var _binding : FragmentShoppingBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentShoppingBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}