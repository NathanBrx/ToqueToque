package fr.zerohour.toquetoque.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Transaction

@Dao
interface RecipeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredientGroups(groups: List<IngredientGroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIngredients(ingredients: List<IngredientEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstructionGroups(groups: List<InstructionGroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInstructionSteps(steps: List<InstructionStepEntity>)

    @Transaction
    suspend fun saveFullRecipe(
        recipe: RecipeEntity,
        ingGroups: List<IngredientGroupEntity>,
        ingredients: List<IngredientEntity>,
        insGroups: List<InstructionGroupEntity>,
        steps: List<InstructionStepEntity>
    ) {
        insertRecipe(recipe)

        if (ingGroups.isNotEmpty()) insertIngredientGroups(ingGroups)
        if (insGroups.isNotEmpty()) insertInstructionGroups(insGroups)

        if (ingredients.isNotEmpty()) insertIngredients(ingredients)
        if (steps.isNotEmpty()) insertInstructionSteps(steps)
    }
}