package fr.zerohour.toquetoque.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import fr.zerohour.toquetoque.ToqueToqueApp
import fr.zerohour.toquetoque.data.local.FullRecipe
import fr.zerohour.toquetoque.data.repository.RecipeRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class RecipeDetailViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    private val _recipeState = MutableStateFlow<FullRecipe?>(null)
    val recipeState = _recipeState.asStateFlow()

    fun loadRecipe(recipeId: String) {
        viewModelScope.launch {
            delay(300.milliseconds)
            repository.getFullRecipeById(recipeId).collect { fullRecipe ->
                _recipeState.value = fullRecipe
            }
        }
    }

    fun deleteRecipe(recipeId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            repository.deleteRecipeById(recipeId)
            onSuccess()
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ToqueToqueApp
                return RecipeDetailViewModel(application.repository) as T
            }
        }
    }
}