package com.example.shoppinglistapp.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "Shopping_item")
data class ShoppingItem(
    val name: String,
    val amount: Int,
    val price: Float,
    val imageUrl: String,
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null
) : Parcelable