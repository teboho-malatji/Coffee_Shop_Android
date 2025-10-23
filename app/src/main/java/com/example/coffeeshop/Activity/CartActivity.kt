package com.example.coffeeshop.Activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.coffeeshop.Adapter.CartAdapter
import com.example.coffeeshop.Helper.ChangeNumberItemsListener
import com.example.coffeeshop.Helper.ManagementCart
import com.example.coffeeshop.databinding.ActivityCartBinding

/**
 * CartActivity displays the user's shopping cart items.
 * It calculates totals (subtotal, tax, delivery, and total),
 * and updates dynamically when the user changes quantities.
 */
class CartActivity : AppCompatActivity() {

    // ViewBinding for accessing XML views safely
    private lateinit var binding: ActivityCartBinding

    // Handles cart logic (add, remove, update, totals)
    private lateinit var managementCart: ManagementCart

    // Variable to hold tax calculation result
    private var tax: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        managementCart = ManagementCart(this)

        // Initialize UI and cart functions
        setVariable()
        initCartList()
        calculateCart()
    }

    /**
     * Initializes the RecyclerView that displays the list of cart items.
     */
    private fun initCartList() {
        binding.apply {
            listView.layoutManager =
                LinearLayoutManager(this@CartActivity, LinearLayoutManager.VERTICAL, false)

            // Set up adapter and listener for quantity change events
            listView.adapter = CartAdapter(
                managementCart.getListCart(),
                this@CartActivity,
                object : ChangeNumberItemsListener {
                    override fun onChanged() {
                        calculateCart() // Recalculate totals whenever the cart updates
                    }
                }
            )
        }
    }

    /**
     * Sets up UI actions, such as back button.
     */
    private fun setVariable() {
        binding.backBtn.setOnClickListener { finish() }
    }

    /**
     * Calculates subtotal, tax, delivery, and total price,
     * then updates the text views in the layout.
     */
    private fun calculateCart() {
        val percentTax = 0.02 // 2% tax
        val delivery = 35     // Fixed delivery fee

        // Calculate subtotal and tax (rounded to two decimal places)
        val subTotal = Math.round(managementCart.getTotalFee() * 100) / 100.0
        tax = Math.round((subTotal * percentTax) * 100) / 100.0
        val total = Math.round((subTotal + tax + delivery) * 100) / 100.0

        // Update UI with calculated values
        binding.apply {
            totalFeeTxt.text = "R${subTotal}"
            taxTxt.text = "R${tax}"
            deliveryTxt.text = "R${delivery}"
            totalTxt.text = "R${total}"
        }
    }
}
