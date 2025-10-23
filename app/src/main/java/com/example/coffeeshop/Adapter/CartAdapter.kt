package com.example.coffeeshop.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.request.RequestOptions
import com.example.coffeeshop.Domain.ItemModel
import com.example.coffeeshop.Helper.ChangeNumberItemsListener
import com.example.coffeeshop.Helper.ManagementCart
import com.example.coffeeshop.databinding.ViewholderCartBinding

/**
 * Adapter for displaying items in the cart RecyclerView.
 * Handles increasing, decreasing, and removing items, and updating total amounts.
 */
class CartAdapter(
    private val listItemSelected: ArrayList<ItemModel>, // List of items in the cart
    context: Context,
    private var changeNumberItemsListener: ChangeNumberItemsListener? = null
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    // ViewHolder holds the view binding reference for each cart item
    class ViewHolder(val binding: ViewholderCartBinding) :
        RecyclerView.ViewHolder(binding.root)

    // ManagementCart handles cart operations (add, remove, update)
    private val managementCart = ManagementCart(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the layout for each cart item
        val binding = ViewholderCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItemSelected[position]

        // Display product information
        holder.binding.titleTxt.text = item.title
        holder.binding.feeEachItem.text = "R${item.price}"
        holder.binding.numberItemTxt.text = item.numberInCart.toString()
        holder.binding.totalEachItem.text = "R${Math.round(item.numberInCart * item.price)}"

        // Load product image using Glide
        Glide.with(holder.itemView.context)
            .load(item.picUrl[0])
            .apply(RequestOptions().transform(CenterCrop()))
            .into(holder.binding.picCart)

        // Increase item quantity
        holder.binding.plusEachItem.setOnClickListener {
            managementCart.plusItem(listItemSelected, holder.adapterPosition, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    // Refresh RecyclerView and notify total update
                    notifyItemChanged(holder.adapterPosition)
                    changeNumberItemsListener?.onChanged()
                }
            })
        }

        // Decrease item quantity
        holder.binding.minusEachItem.setOnClickListener {
            managementCart.minusItem(listItemSelected, holder.adapterPosition, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    // Refresh RecyclerView and notify total update
                    notifyItemChanged(holder.adapterPosition)
                    changeNumberItemsListener?.onChanged()
                }
            })
        }

        // Remove item from cart
        holder.binding.removeItemBtn.setOnClickListener {
            managementCart.romoveItem(listItemSelected, holder.adapterPosition, object : ChangeNumberItemsListener {
                override fun onChanged() {
                    // Refresh list and update total
                    notifyItemRemoved(holder.adapterPosition)
                    changeNumberItemsListener?.onChanged()
                }
            })
        }
    }

    override fun getItemCount(): Int = listItemSelected.size
}
