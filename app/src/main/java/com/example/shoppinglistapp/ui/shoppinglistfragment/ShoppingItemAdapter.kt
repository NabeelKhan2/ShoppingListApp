package com.example.shoppinglistapp.ui.shoppinglistfragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.shoppinglistapp.data.local.ShoppingItem
import com.example.shoppinglistapp.databinding.ItemShoppingBinding
import com.example.shoppinglistapp.utils.Constants

class ShoppingItemAdapter : RecyclerView.Adapter<ShoppingItemAdapter.ShoppingItemViewHolder>() {

    private val diffCallback = object : DiffUtil.ItemCallback<ShoppingItem>() {
        override fun areItemsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ShoppingItem, newItem: ShoppingItem): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var shoppingItems: List<ShoppingItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShoppingItemViewHolder {
        val binding =
            ItemShoppingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ShoppingItemViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return shoppingItems.size
    }

    override fun onBindViewHolder(holder: ShoppingItemViewHolder, position: Int) {
        holder.bind(shoppingItems[position])
    }


    class ShoppingItemViewHolder(private val binding: ItemShoppingBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ShoppingItem) {
            binding.apply {
                ivShoppingImage.load(item.imageUrl)
                tvName.text = item.name

                val amountText = "${item.amount}x"
                tvShoppingItemAmount.text = amountText

                val priceText = "${item.price}$"
                tvShoppingItemPrice.text = priceText

                rootLayout.setOnClickListener {
                    Constants.IMAGE_URL_UPDATION = item.imageUrl
                    val action =
                        ShoppingFragmentDirections.actionShoppingFragmentToUpdateShoppingItemFragment(
                            item
                        )
                    binding.root.findNavController().navigate(action)
                }
            }
        }
    }
}