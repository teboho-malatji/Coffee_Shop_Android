package com.example.coffeeshop.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeeshop.Domain.CategoryModel
import com.example.coffeeshop.Domain.ItemModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Query
import com.google.firebase.database.ValueEventListener

// Repository class that handles communication between the app and Firebase Realtime Database
// It provides LiveData objects for observing data changes (Categories and Popular items)
class MainRepository {

    // Instance of Firebase Realtime Database
    private val firebaseDatabase = FirebaseDatabase.getInstance()

    // ---------------------- CATEGORY DATA ---------------------- //
    // Function to fetch the list of categories from the "Category" node in Firebase
    fun loadCategory(): LiveData<MutableList<CategoryModel>> {

        // LiveData that holds the list of CategoryModel objects
        val listData = MutableLiveData<MutableList<CategoryModel>>()

        // Reference to the "Category" path in Firebase Database
        val ref = firebaseDatabase.getReference("Category")

        // Add a listener to listen for real-time data updates
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // Create a mutable list to store categories
                val list = mutableListOf<CategoryModel>()

                // Loop through all child nodes in the "Category" snapshot
                for (child in snapshot.children) {

                    // Convert each child snapshot into a CategoryModel object
                    val item = child.getValue(CategoryModel::class.java)

                    // If not null, add the item to the list
                    item?.let { list.add(it) }
                }

                // Update the LiveData with the new list of categories
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {

                // Called when Firebase fails to read data (e.g., due to permission issues)
                println("Firebase load failed: ${error.message}")
            }
        })

        // Return the LiveData so the ViewModel or Activity can observe it
        return listData
    }

    // ---------------------- POPULAR ITEMS DATA ---------------------- //
    // Function to fetch the list of popular coffee items from the "Popular" node in Firebase
    fun loadPopular(): LiveData<MutableList<ItemModel>> {

        // LiveData that holds the list of ItemModel objects
        val listData = MutableLiveData<MutableList<ItemModel>>()

        // Reference to the "Popular" path in Firebase Database
        val ref = firebaseDatabase.getReference("Popular")

        // Add a listener to listen for real-time updates in the "Popular" node
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // Create a mutable list to store popular items
                val list = mutableListOf<ItemModel>()

                // Loop through all child nodes in the "Popular" snapshot
                for (child in snapshot.children) {

                    // Convert each child snapshot into an ItemModel object
                    val item = child.getValue(ItemModel::class.java)

                    // If not null, add the item to the list
                    item?.let { list.add(it) }
                }

                // Update the LiveData with the new list of popular items
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                // proper error handling
                println("Firebase load failed: ${error.message}")
            }
        })

        // Return the LiveData so it can be observed in the UI layer
        return listData
    }

    // ---------------------- SPECIAL ITEMS DATA ---------------------- //
    // Function to fetch the list of special coffee items from the "Special" node in Firebase
    fun loadSpecial(): LiveData<MutableList<ItemModel>> {

        // LiveData that holds the list of ItemModel objects
        val listData = MutableLiveData<MutableList<ItemModel>>()

        // Reference to the "Special" path in Firebase Database
        val ref = firebaseDatabase.getReference("Special")

        // Add a listener to listen for real-time updates in the "Special" node
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                // Create a mutable list to store special items
                val list = mutableListOf<ItemModel>()

                // Loop through all child nodes in the "Special" snapshot
                for (child in snapshot.children) {

                    // Convert each child snapshot into an ItemModel object
                    val item = child.getValue(ItemModel::class.java)

                    // If not null, add the item to the list
                    item?.let { list.add(it) }
                }

                // Update the LiveData with the new list of special items
                listData.value = list
            }

            override fun onCancelled(error: DatabaseError) {
                // proper error handling
                println("Firebase load failed: ${error.message}")
            }
        })

        // Return the LiveData so it can be observed in the UI layer
        return listData
    }

    //list of items
    fun loadCategoryItems(categoryId: String): LiveData<MutableList<ItemModel>>{
     val itemsLiveData= MutableLiveData<MutableList<ItemModel>>()
      val ref=firebaseDatabase.getReference("Items")
      val query: Query= ref.orderByChild("categoryId").equalTo(categoryId)

       query.addListenerForSingleValueEvent(object:ValueEventListener{
           override fun onDataChange(snapshot: DataSnapshot) {
               val list = mutableListOf<ItemModel>()
               for (child in snapshot.children){
                   val item= child.getValue(ItemModel:: class.java)
                   item?.let { list.add(it) }
               }

               itemsLiveData.value = list
           }

           override fun onCancelled(error: DatabaseError) {
               TODO("Not yet implemented")
           }

       })
        return itemsLiveData
    }
}