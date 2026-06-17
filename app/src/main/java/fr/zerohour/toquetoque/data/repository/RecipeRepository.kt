package fr.zerohour.toquetoque.data.repository

import fr.zerohour.toquetoque.data.local.FullRecipe
import fr.zerohour.toquetoque.data.local.IngredientEntity
import fr.zerohour.toquetoque.data.local.IngredientGroupEntity
import fr.zerohour.toquetoque.data.local.InstructionGroupEntity
import fr.zerohour.toquetoque.data.local.InstructionStepEntity
import fr.zerohour.toquetoque.data.local.RecipeDao
import fr.zerohour.toquetoque.data.local.RecipeEntity
import fr.zerohour.toquetoque.data.local.RecipePhotoEntity
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {

    suspend fun saveRecipe(
        recipe: RecipeEntity,
        ingredientGroups: List<IngredientGroupEntity>,
        ingredients: List<IngredientEntity>,
        instructionGroups: List<InstructionGroupEntity>,
        instructionSteps: List<InstructionStepEntity>,
        photos: List<RecipePhotoEntity>
    ) {
        recipeDao.saveFullRecipe(
            recipe,
            ingredientGroups,
            ingredients,
            instructionGroups,
            instructionSteps,
            photos
        )
    }

    fun getFullRecipesByTag(tag: String): Flow<List<FullRecipe>> {
        return recipeDao.getFullRecipesByTag(tag)
    }

    fun getFullRecipeById(id: String): Flow<FullRecipe?> {
        return recipeDao.getFullRecipeById(id)
    }

    suspend fun deleteRecipeById(id: String) {
        recipeDao.deleteRecipeById(id)
    }
}