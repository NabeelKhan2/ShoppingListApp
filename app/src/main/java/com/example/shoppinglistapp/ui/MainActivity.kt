package com.example.shoppinglistapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shoppinglistapp.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    @Inject
//    lateinit var fragmentFactory: ShoppingFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        supportFragmentManager.fragmentFactory = fragmentFactory
        setContentView(R.layout.activity_main)
    }
}