package com.example.myapplication.ui.menu

import androidx.lifecycle.ViewModel
import com.example.myapplication.data.model.Dish
import com.example.myapplication.data.repository.MenuRepository
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel : ViewModel() {
    val dishes: StateFlow<List<Dish>> = MenuRepository.dishes

    fun addDish(name: String, description: String, price: Double, category: String) {
        val dish = Dish(
            name = name,
            description = description,
            price = price,
            category = category
        )
        MenuRepository.addDish(dish)
    }

    fun updateDish(dish: Dish) {
        MenuRepository.updateDish(dish)
    }

    fun deleteDish(dishId: String) {
        MenuRepository.deleteDish(dishId)
    }
}
