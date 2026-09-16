package com.example.myapplication.data.repository

import com.example.myapplication.data.model.Dish
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.UUID

object MenuRepository {
    private val _dishes = MutableStateFlow(listOf(
        Dish("1", "Hamburguesa Clásica", "Carne de res, queso, lechuga y tomate", 12.99, "", "Fondo"),
        Dish("2", "Pizza Margherita", "Salsa de tomate, mozzarella y albahaca", 10.50, "", "Fondo"),
        Dish("3", "Ensalada César", "Lechuga romana, croutones y aderezo césar", 8.00, "", "Entrada"),
        Dish("4", "Papas Fritas", "Papas crujientes con sal", 4.50, "", "Entrada"),
        Dish("5", "Limonada", "Jugo de limón natural", 3.00, "", "Bebida"),
        Dish("6", "Helado de Vainilla", "Dos bolas de helado premium", 5.00, "", "Postre")
    ))
    val dishes: StateFlow<List<Dish>> = _dishes.asStateFlow()

    fun addDish(dish: Dish) {
        val newDish = dish.copy(id = UUID.randomUUID().toString().take(6))
        _dishes.update { it + newDish }
    }

    fun updateDish(updatedDish: Dish) {
        _dishes.update { currentList ->
            currentList.map { if (it.id == updatedDish.id) updatedDish else it }
        }
    }

    fun deleteDish(dishId: String) {
        _dishes.update { it.filterNot { dish -> dish.id == dishId } }
    }
}
