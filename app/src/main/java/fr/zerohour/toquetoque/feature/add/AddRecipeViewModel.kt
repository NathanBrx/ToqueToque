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

    fun saveOrUpdateRecipe(
        recipeIdToEdit: String?,
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
            val isUpdating = recipeIdToEdit != null
            val recipeId = recipeIdToEdit ?: UUID.randomUUID().toString()

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

            val ingGroupEntities = mutableListOf<IngredientGroupEntity>()
            val ingEntities = mutableListOf<IngredientEntity>()

            ingredientGroupsUi.forEach { group ->
                val validIngredients = group.ingredients.filter { it.name.isNotBlank() }

                if (validIngredients.isNotEmpty() || group.title.isNotBlank()) {
                    ingGroupEntities.add(IngredientGroupEntity(group.id, recipeId, group.title))

                    validIngredients.forEach { ingredient ->
                        ingEntities.add(
                            IngredientEntity(ingredient.id, group.id, ingredient.quantity, ingredient.unit, ingredient.name)
                        )
                    }
                }
            }

            val instGroupEntities = mutableListOf<InstructionGroupEntity>()
            val instStepEntities = mutableListOf<InstructionStepEntity>()

            instructionGroupsUi.forEach { group ->
                val validSteps = group.steps.filter { it.isNotBlank() }

                if (validSteps.isNotEmpty() || group.title.isNotBlank()) {
                    instGroupEntities.add(InstructionGroupEntity(group.id, recipeId, group.title))

                    validSteps.forEachIndexed { index, stepText ->
                        val stepId = UUID.randomUUID().toString()
                        instStepEntities.add(
                            InstructionStepEntity(stepId, group.id, index, stepText)
                        )
                    }
                }
            }

            val photoEntities = photoUris.map { uriString ->
                RecipePhotoEntity(id = UUID.randomUUID().toString(), recipeId = recipeId, photoUri = uriString)
            }

            if (isUpdating) {
                repository.updateRecipe(recipeEntity, ingGroupEntities, ingEntities, instGroupEntities, instStepEntities, photoEntities)
            } else {
                repository.saveRecipe(recipeEntity, ingGroupEntities, ingEntities, instGroupEntities, instStepEntities, photoEntities)
            }
        }
    }

    suspend fun getRecipeById(id: String): FullRecipe? {
        return repository.getFullRecipeByIdSync(id)
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