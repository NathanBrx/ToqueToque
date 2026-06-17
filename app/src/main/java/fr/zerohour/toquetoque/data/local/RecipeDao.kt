package fr.zerohour.toquetoque.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow

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

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipePhotos(photos: List<RecipePhotoEntity>)

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

    @Transaction
    @Query("SELECT * FROM recipes WHERE selectedTag = :tag")
    fun getFullRecipesByTag(tag: String): Flow<List<FullRecipe>>

    @Transaction
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getFullRecipeById(recipeId: String): Flow<FullRecipe?>

    @Query("DELETE FROM recipes WHERE id = :recipeId")
    suspend fun deleteRecipeById(recipeId: String)
}