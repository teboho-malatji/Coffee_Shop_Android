package com.example.coffeeshop.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.Activity.DetailActivity
import com.example.coffeeshop.Domain.ItemModel
import com.example.coffeeshop.databinding.ViewholderPopularBinding

/**
 * Adapter class responsible for displaying a list of popular coffee items
 * in a RecyclerView on the main screen.
 */
class PopularAdapter(val items: MutableList<ItemModel>) :
    RecyclerView.Adapter<PopularAdapter.ViewHolder>() {

    // Used to access application resources and perform actions like starting new activities
    lateinit var context: Context

    /**
     * The ViewHolder class holds references to the individual views
     * inside each RecyclerView item (viewholder_popular.xml layout).
     */
    class ViewHolder(val binding: ViewholderPopularBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Called when a new ViewHolder needs to be created.
     * This inflates the XML layout for each coffee item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularAdapter.ViewHolder {
        // Initialize context (so it can be used throughout the adapter)
        context = parent.context

        // Inflate the layout for a single popular coffee card
        val binding = ViewholderPopularBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        // Return a new instance of the ViewHolder
        return ViewHolder(binding)
    }

    /**
     * Called to bind (set) the data for each coffee item in the RecyclerView.
     * It connects data from the ItemsModel to the views.
     */
    override fun onBindViewHolder(holder: PopularAdapter.ViewHolder, position: Int) {
        // Get the current coffee item based on its position in the list
        val item = items[position]

        // Set the coffee name (title)
        holder.binding.titleTxt.text = item.title

        // Set the additional coffee info (e.g., description or size)
        holder.binding.extraTxt.text = item.extra

        // Set the price and add "R" before the number (for currency)
        holder.binding.priceTxt.text = "R" + item.price.toString()

        // Load the first image of the coffee item using Glide
        // Glide handles image loading and caching automatically
        Glide.with(context)
            .load(item.picUrl[0]) // Load the first image in the list
            .into(holder.binding.pic) // Display it in the ShapeableImageView

        // Set an OnClickListener to handle when a user taps the item
        holder.itemView.setOnClickListener {
            // Create an Intent to open the DetailActivity
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)

            // Pass the selected coffee item to the next screen
            intent.putExtra("object", item)

            // Start the DetailActivity
            holder.itemView.context.startActivity(intent)
        }
    }

    /**
     * Returns the total number of coffee items to display.
     */
    override fun getItemCount(): Int = items.size
}