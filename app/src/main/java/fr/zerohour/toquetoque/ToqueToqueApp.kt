package fr.zerohour.toquetoque

import android.app.Application
import androidx.room.Room
import fr.zerohour.toquetoque.data.local.AppDatabase
import fr.zerohour.toquetoque.data.repository.RecipeRepository

class ToqueToqueApp : Application() {

    private val database by lazy {
        Room.databaseBuilder(
            this,
            AppDatabase::class.java,
            "toquetoque_database"
        ).build()
    }

    val repository by lazy {
        RecipeRepository(database.recipeDao())
    }
}