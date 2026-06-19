package fr.zerohour.toquetoque.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {

    // ------------------------------
    // Create
    // ------------------------------
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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipePhotos(photos: List<RecipePhotoEntity>)

    // ------------------------------
    // Insert
    // ------------------------------

    @Transaction
    suspend fun saveFullRecipe(
        recipe: RecipeEntity,
        ingGroups: List<IngredientGroupEntity>,
        ingredients: List<IngredientEntity>,
        insGroups: List<InstructionGroupEntity>,
        steps: List<InstructionStepEntity>,
        photos: List<RecipePhotoEntity>
    ) {
        insertRecipe(recipe)

        if (ingGroups.isNotEmpty()) insertIngredientGroups(ingGroups)
        if (insGroups.isNotEmpty()) insertInstructionGroups(insGroups)

        if (ingredients.isNotEmpty()) insertIngredients(ingredients)
        if (steps.isNotEmpty()) insertInstructionSteps(steps)

        if (photos.isNotEmpty()) insertRecipePhotos(photos)
    }

    // ------------------------------
    // Get
    // ------------------------------
    @Transaction
    @Query("SELECT * FROM recipes WHERE selectedTag = :tag")
    fun getFullRecipesByTag(tag: String): Flow<List<FullRecipe>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getFullRecipeById(recipeId: String): Flow<FullRecipe?>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getFullRecipeByIdSync(recipeId: String): FullRecipe?

    @Transaction
    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%' OR description LIKE '%' || :query || '%'")
    fun searchRecipes(query: String): Flow<List<FullRecipe>>

    // ------------------------------
    // Delete
    // ------------------------------
    @Query("DELETE FROM recipes WHERE id = :recipeId")
    suspend fun deleteRecipeById(recipeId: String)

    // -----------------------------------
    // Update
    // -----------------------------------
    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)

    @Query("DELETE FROM ingredient_groups WHERE recipeId = :recipeId")
    suspend fun deleteIngredientsByRecipeId(recipeId: String)

    @Query("DELETE FROM instruction_groups WHERE recipeId = :recipeId")
    suspend fun deleteInstructionsByRecipeId(recipeId: String)

    @Query("DELETE FROM recipe_photos WHERE recipeId = :recipeId")
    suspend fun deletePhotosByRecipeId(recipeId: String)

    @Transaction
    suspend fun updateFullRecipe(
        recipe: RecipeEntity,
        ingGroups: List<IngredientGroupEntity>,
        ingredients: List<IngredientEntity>,
        insGroups: List<InstructionGroupEntity>,
        steps: List<InstructionStepEntity>,
        photos: List<RecipePhotoEntity>
    ) {
        updateRecipe(recipe)

        deleteIngredientsByRecipeId(recipe.id)
        deleteInstructionsByRecipeId(recipe.id)
        deletePhotosByRecipeId(recipe.id)

        if (ingGroups.isNotEmpty()) insertIngredientGroups(ingGroups)
        if (ingredients.isNotEmpty()) insertIngredients(ingredients)
        if (insGroups.isNotEmpty()) insertInstructionGroups(insGroups)
        if (steps.isNotEmpty()) insertInstructionSteps(steps)
        if (photos.isNotEmpty()) insertRecipePhotos(photos)
    }
}