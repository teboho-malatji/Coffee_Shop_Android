package com.example.coffeeshop.Adapter

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeeshop.Activity.ItemListActivity
import com.example.coffeeshop.Domain.CategoryModel
import com.example.coffeeshop.R
import com.example.coffeeshop.databinding.ViewholderCategoryBinding

// Adapter class for displaying coffee categories in a RecyclerView
class CategoryAdapter(val items: MutableList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.Viewholder>() {

    // Holds the current context (useful for accessing resources or inflating layouts)
    private lateinit var context: Context

    // Keeps track of the currently selected category item
    private var selectedPosition = -1

    // Keeps track of the last selected category item (used to reset its background)
    private var lastSelectedPosition = -1

    // ------------------------- ViewHolder Class -------------------------
    // ViewHolder holds the view references for a single item layout (viewholder_category.xml)
    class Viewholder(val binding: ViewholderCategoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    // ------------------------- onCreateViewHolder -------------------------
    // Called when RecyclerView needs a new ViewHolder (creates item layout)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryAdapter.Viewholder {
        // Initialize context from parent view
        context = parent.context

        // Inflate the layout for a single category item using ViewBinding
        val binding = ViewholderCategoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return Viewholder(binding)
    }

    // ------------------------- onBindViewHolder -------------------------
    // Binds data (CategoryModel) to each item view when displayed on screen
    override fun onBindViewHolder(holder: CategoryAdapter.Viewholder, position: Int) {
        val item = items[position] // Get category item based on position
        holder.binding.titleCat.text = item.title // Set the category title text

        // When a category item is clicked:
        holder.binding.root.setOnClickListener {
            lastSelectedPosition = selectedPosition      // Store previous selected position
            selectedPosition = holder.adapterPosition    // Update to the new selected position

            // Refresh both previously selected and currently selected items
            notifyItemChanged(lastSelectedPosition)
            notifyItemChanged(selectedPosition)


            Handler(Looper.getMainLooper()).postDelayed({
                val intent= Intent(context, ItemListActivity::class.java).apply {
                    putExtra("id", item.id.toString())
                    putExtra("title", item.title)
                }
                ContextCompat.startActivity(context,intent,null)
            },500)

        }

        // Change the background color to indicate the selected category
        if (selectedPosition == position) {
            holder.binding.titleCat.setBackgroundResource(R.drawable.brown_bg) // Selected
        } else {
            holder.binding.titleCat.setBackgroundResource(R.drawable.dark_brown_bg) // Default
        }


    }

    // ------------------------- getItemCount -------------------------
    // Returns total number of categories in the list
    override fun getItemCount(): Int = items.size
}