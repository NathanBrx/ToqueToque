package fr.zerohour.toquetoque.data.repository

import fr.zerohour.toquetoque.data.local.FullRecipe
import fr.zerohour.toquetoque.data.local.IngredientEntity
import fr.zerohour.toquetoque.data.local.IngredientGroupEntity
import fr.zerohour.toquetoque.data.local.InstructionGroupEntity
import fr.zerohour.toquetoque.data.local.InstructionStepEntity
import fr.zerohour.toquetoque.data.local.RecipeDao
import fr.zerohour.toquetoque.data.local.RecipeEntity
import kotlinx.coroutines.flow.Flow

class RecipeRepository(private val recipeDao: RecipeDao) {

    suspend fun saveRecipe(
        recipe: RecipeEntity,
        ingredientGroups: List<IngredientGroupEntity>,
        ingredients: List<IngredientEntity>,
        instructionGroups: List<InstructionGroupEntity>,
        instructionSteps: List<InstructionStepEntity>
    ) {
        recipeDao.saveFullRecipe(
            recipe,
            ingredientGroups,
            ingredients,
            instructionGroups,
            instructionSteps
        )
    }

    fun getRecipesByTag(tag: String): Flow<List<RecipeEntity>> {
        return recipeDao.getRecipesByTag(tag)
    }

    fun getFullRecipeById(id: String): Flow<FullRecipe?> {
        return recipeDao.getFullRecipeById(id)
    }
}