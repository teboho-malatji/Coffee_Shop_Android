package com.example.coffeeshop.Activity

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.coffeeshop.Adapter.SizeAdapter
import com.example.coffeeshop.Domain.ItemModel
import com.example.coffeeshop.Helper.ManagementCart
import com.example.coffeeshop.databinding.ActivityDetailBinding
import eightbitlab.com.blurview.RenderScriptBlur

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var item: ItemModel
    private lateinit var managementCart: ManagementCart

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize cart manager
        managementCart = ManagementCart(this)

        // Load data passed via Intent
        bundle()

        // Apply blur background effect
        setBlurEffect()

        // Initialize size selector list and image
        initSizeList()
    }

    /**
     * Initializes the horizontal list of available coffee sizes
     * and loads the main image from the selected item.
     */
    private fun initSizeList() {
        val sizeList = arrayListOf("1", "2", "3", "4")

        // Set up RecyclerView adapter and layout
        binding.sizeList.adapter = SizeAdapter(sizeList)
        binding.sizeList.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        // Load the first image of the item as the main image
        if (item.picUrl.isNotEmpty()) {
            Glide.with(this)
                .load(item.picUrl[0])
                .apply(RequestOptions.bitmapTransform(RoundedCorners(100)))
                .into(binding.picMain)
        }
    }

    /**
     * Configures a blurred background effect for the UI
     */
    private fun setBlurEffect() {
        val radius = 10f
        val decorView = this.window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background // ✅ fixed typo: "windosBackground" → "windowBackground"

        binding.blurView.setupWith(rootView, RenderScriptBlur(this))
            .setFrameClearDrawable(windowBackground)
            .setBlurRadius(radius)

        binding.blurView.setOutlineProvider(ViewOutlineProvider.BACKGROUND)
        binding.blurView.clipToOutline = true // ✅ use property syntax instead of deprecated setter
    }

    /**
     * Retrieves item details from Intent and binds them to UI components.
     */
    private fun bundle() {
        binding.apply {
            // ✅ Safe cast with null check to avoid crash if Intent is missing the extra
            val serializableItem = intent.getSerializableExtra("object")
            if (serializableItem is ItemModel) {
                item = serializableItem
            } else {
                finish() // gracefully close activity if no item data found
                return
            }

            // Load main image
            Glide.with(this@DetailActivity)
                .load(item.picUrl.firstOrNull()) // safer than [0]
                .into(binding.picMain)

            // Bind text fields
            titleTxt.text = item.title
            descriptionTxt.text = item.description
            priceTxt.text = "R${item.price}"
            ratingTxt.text = item.rating.toString()
            extraTxt.text = item.extra
            numberItemTxt.text = item.numberInCart.toString() // ✅ show initial quantity

            // Add to cart button logic
            addToCartBtn.setOnClickListener {
                val quantity = numberItemTxt.text.toString().toIntOrNull() ?: 1
                item.numberInCart = quantity
                managementCart.insertItems(item)
            }

            // Back button
            backBtn.setOnClickListener { finish() }

            // Plus button
            plusCart.setOnClickListener {
                item.numberInCart++
                numberItemTxt.text = item.numberInCart.toString()
            }

            // Minus button
            minusCart.setOnClickListener {
                if (item.numberInCart > 1) { // ✅ prevent going below 1
                    item.numberInCart--
                    numberItemTxt.text = item.numberInCart.toString()
                }
            }
        }
    }
}
