package fr.zerohour.toquetoque.feature.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import fr.zerohour.toquetoque.ToqueToqueApp
import fr.zerohour.toquetoque.data.local.RecipeEntity
import fr.zerohour.toquetoque.data.repository.RecipeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CategoryViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow<List<RecipeEntity>>(emptyList())
    val recipes = _recipes.asStateFlow()

    fun loadRecipes(category: String) {
        viewModelScope.launch {
            repository.getRecipesByTag(category).collect { recipeList ->
                _recipes.value = recipeList
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ToqueToqueApp
                return CategoryViewModel(application.repository) as T
            }
        }
    }
}