package fr.zerohour.toquetoque.feature.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import fr.zerohour.toquetoque.ToqueToqueApp
import fr.zerohour.toquetoque.data.local.*
import fr.zerohour.toquetoque.data.repository.RecipeRepository
import kotlinx.coroutines.launch
import java.util.UUID

class AddRecipeViewModel(
    private val repository: RecipeRepository
) : ViewModel() {

    fun saveRecipeToDatabase(
        title: String,
        description: String,
        prepTime: String,
        servings: String,
        cookTime: String,
        coolingTime: String,
        freezingTime: String,
        selectedType: String,
        ingredientGroupsUi: List<IngredientGroup>,
        instructionGroupsUi: List<InstructionGroup>,
        photoUris: List<String>
    ) {
        viewModelScope.launch {
            val recipeId = UUID.randomUUID().toString()

            val recipeEntity = RecipeEntity(
                id = recipeId,
                title = title,
                description = description,
                prepTime = prepTime,
                servings = servings,
                cookTime = cookTime,
                coolingTime = coolingTime,
                freezingTime = freezingTime,
                selectedTag = selectedType
            )

            // --- ingrédients ---
            val ingGroupEntities = mutableListOf<IngredientGroupEntity>()
            val ingEntities = mutableListOf<IngredientEntity>()

            ingredientGroupsUi.forEach { group ->
                ingGroupEntities.add(IngredientGroupEntity(group.id, recipeId, group.title))
                group.ingredients.forEach { ingredient ->
                    ingEntities.add(
                        IngredientEntity(ingredient.id, group.id, ingredient.quantity, ingredient.unit, ingredient.name)
                    )
                }
            }

            // --- instructions ---
            val instGroupEntities = mutableListOf<InstructionGroupEntity>()
            val instStepEntities = mutableListOf<InstructionStepEntity>()

            instructionGroupsUi.forEach { group ->
                instGroupEntities.add(InstructionGroupEntity(group.id, recipeId, group.title))
                group.steps.forEachIndexed { index, stepText ->
                    val stepId = UUID.randomUUID().toString()
                    instStepEntities.add(
                        InstructionStepEntity(stepId, group.id, index, stepText)
                    )
                }
            }

            // --- photos ---
            val photoEntities = photoUris.map { uriString ->
                RecipePhotoEntity(id = UUID.randomUUID().toString(), recipeId = recipeId, photoUri = uriString)
            }

            repository.saveRecipe(
                recipeEntity, ingGroupEntities, ingEntities, instGroupEntities, instStepEntities, photoEntities
            )
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as ToqueToqueApp

                return AddRecipeViewModel(application.repository) as T
            }
        }
    }
}