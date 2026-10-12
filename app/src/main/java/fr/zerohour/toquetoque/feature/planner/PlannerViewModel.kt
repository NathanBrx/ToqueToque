package fr.zerohour.toquetoque.feature.planner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import fr.zerohour.toquetoque.ToqueToqueApp
import fr.zerohour.toquetoque.data.local.FullRecipe
import fr.zerohour.toquetoque.data.local.MealPlanWithRecipe
import fr.zerohour.toquetoque.data.repository.RecipeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class PlannerViewModel(private val repository: RecipeRepository) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())

    val mealPlans: StateFlow<List<MealPlanWithRecipe>> = repository.getAllMealPlans()
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    val allRecipes: StateFlow<List<FullRecipe>> = repository.getAllRecipes()
        .stateIn(scope = viewModelScope, started = SharingStarted.WhileSubscribed(5000), initialValue = emptyList())

    fun assignRecipeToDate(date: LocalDate, recipeId: String) {
        viewModelScope.launch {
            repository.addMealPlan(date.toString(), recipeId)
        }
    }

    fun removeMealPlan(planId: Long) {
        viewModelScope.launch {
            repository.removeMealPlan(planId)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                if (modelClass.isAssignableFrom(PlannerViewModel::class.java)) {
                    val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ToqueToqueApp

                    val viewModel = PlannerViewModel(app.repository)
                    return modelClass.cast(viewModel)!!
                }
                throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
            }
        }
    }
}