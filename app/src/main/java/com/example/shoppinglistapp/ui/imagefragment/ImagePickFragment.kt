package com.example.shoppinglistapp.ui.imagefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.View
import com.example.shoppinglistapp.R
import com.example.shoppinglistapp.databinding.FragmentImagePickBinding

class ImagePickFragment : Fragment(R.layout.fragment_image_pick) {

    private var _binding : FragmentImagePickBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentImagePickBinding.bind(view)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}