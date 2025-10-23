package com.example.coffeeshop.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.coffeeshop.Activity.DetailActivity
import com.example.coffeeshop.Domain.ItemModel
import com.example.coffeeshop.databinding.ViewholderSpecialBinding

/**
 * Adapter class used to display a list of "special" coffee items
 * inside a RecyclerView. It connects the data (ItemsModel) to the layout (viewholder_special.xml).
 */
class SpecialAdapter(val items: MutableList<ItemModel>) :
    RecyclerView.Adapter<SpecialAdapter.Viewholder>() {

    // Used to hold a reference to the current context (needed for inflating layouts and launching activities)
    lateinit var context: Context

    /**
     * ViewHolder class represents a single row/item in the RecyclerView.
     * It holds a reference to the layout binding so that we can easily access views.
     */
    class Viewholder(val binding: ViewholderSpecialBinding) :
        RecyclerView.ViewHolder(binding.root)

    /**
     * Called when the RecyclerView needs a new ViewHolder.
     * This inflates (creates) a new item layout to be displayed.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialAdapter.Viewholder {
        // Save context from parent (used later for image loading and Intent launching)
        context = parent.context

        // Inflate the layout for each "special" item using View Binding
        val binding = ViewholderSpecialBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        // Return a new instance of our ViewHolder with the inflated layout
        return Viewholder(binding)
    }

    /**
     * Called when the RecyclerView binds data to an existing ViewHolder.
     * This is where we display the coffee details (title, price, rating, image, etc.)
     */
    override fun onBindViewHolder(holder: SpecialAdapter.Viewholder, position: Int) {
        // Get the coffee item at the current position
        val item = items[position]

        // Display the coffee title
        holder.binding.titleTxt.text = item.title

        // Display the price with an "R" prefix (for Rand currency)
        holder.binding.priceTxt.text = "R" + item.price.toString()

        // Set the coffee's rating value in the RatingBar
        holder.binding.ratingBar.rating = item.rating.toFloat()

        /**
         * Load the coffee image using Glide — a library that handles image
         * downloading, caching, and displaying efficiently.
         */
        if (item.picUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(item.picUrl[0]) // Load the first image from the list
                .into(holder.binding.picMain) // Show it in the ImageView (picMain)

            /**
             * Handle what happens when a user taps this coffee item.
             * In this case, open the DetailActivity and pass the selected coffee item.
             */
            holder.itemView.setOnClickListener {
                // Create an Intent to navigate to the DetailActivity screen
                val intent = Intent(holder.itemView.context, DetailActivity::class.java)

                // Attach the selected coffee item to the Intent so it can be accessed in DetailActivity
                intent.putExtra("object", items[position])

                // Launch the DetailActivity
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    /**
     * Returns the total number of special items in the list.
     * This tells the RecyclerView how many rows it needs to display.
     */
    override fun getItemCount(): Int = items.size
}
