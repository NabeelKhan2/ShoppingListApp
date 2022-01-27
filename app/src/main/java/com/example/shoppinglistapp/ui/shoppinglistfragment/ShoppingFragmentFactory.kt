package com.example.shoppinglistapp.ui.shoppinglistfragment

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentFactory
import com.example.shoppinglistapp.ui.adapters.ImageAdapter
import com.example.shoppinglistapp.ui.adapters.ShoppingItemAdapter
import com.example.shoppinglistapp.ui.addfragment.AddShoppingItemFragment
import com.example.shoppinglistapp.ui.imagefragment.ImagePickFragment
import javax.inject.Inject

class ShoppingFragmentFactory @Inject constructor(
    private val imageAdapter: ImageAdapter,
    private val shoppingItemAdapter: ShoppingItemAdapter
): FragmentFactory() {

    override fun instantiate(classLoader: ClassLoader, className: String): Fragment {
        return when(className) {
            ImagePickFragment::class.java.name -> ImagePickFragment(imageAdapter)
            AddShoppingItemFragment::class.java.name -> AddShoppingItemFragment()
            ShoppingFragment::class.java.name -> ShoppingFragment(shoppingItemAdapter)
            else -> super.instantiate(classLoader, className)
        }
    }
}