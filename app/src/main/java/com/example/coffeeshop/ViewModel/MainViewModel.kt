package com.example.coffeeshop.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.coffeeshop.Domain.CategoryModel
import com.example.coffeeshop.Domain.ItemModel
import com.example.coffeeshop.Repository.MainRepository

class MainViewModel: ViewModel() {

    // Instance of the Repository class to access Firebase data
    private val repository = MainRepository()

    // ---------------------- CATEGORY DATA ---------------------- //
    // This function calls the repository to load category data from Firebase
    // It returns LiveData so the UI can observe data changes automatically
    fun loadCategory(): LiveData<MutableList<CategoryModel>> {
        return repository.loadCategory()
    }

    // ---------------------- POPULAR ITEMS DATA ---------------------- //
    // This function calls the repository to load popular coffee items from Firebase
    // It also returns LiveData to keep the UI updated in real time
    fun loadPopular(): LiveData<MutableList<ItemModel>> {
        return repository.loadPopular()
    }

    // ---------------------- SPECIAL ITEMS DATA ---------------------- //
    // This function calls the repository to load special coffee items from Firebase
    // It returns LiveData so the UI can observe data changes automatically
    fun loadSpecial(): LiveData<MutableList<ItemModel>> {
        return repository.loadSpecial()
    }

    fun loadItems(categoryId: String): LiveData<MutableList<ItemModel>>{
        return repository.loadCategoryItems(categoryId)
    }
}