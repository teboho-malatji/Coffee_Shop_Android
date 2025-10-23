package com.example.coffeeshop.Domain

import java.io.Serializable

// Implements Serializable to allow easy passing between activities/intents
data class ItemModel(
    // Item name (e.g., "Cappuccino")
    var title: String = "",

    // Short product description (e.g., "Rich espresso with steamed milk foam")
    var description: String = "",

    // List of image URLs for the item (can store multiple photos)
    val picUrl: ArrayList<String> = ArrayList(),

    // Price of the item
    val price: Double = 0.0,

    // Customer rating (e.g., 4.5 stars)
    var rating: Double = 0.0,

    // Number of this item currently in the user's cart
    var numberInCart: Int = 0,

    // Any additional notes or special options (e.g., “Extra hot”, “Soy milk”)
    var extra: String = ""
): Serializable
