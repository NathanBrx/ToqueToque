package fr.zerohour.toquetoque.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MealPlanDao {
    @Transaction
    @Query("SELECT * FROM meal_plans")
    fun getAllMealPlans(): Flow<List<MealPlanWithRecipe>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMealPlan(mealPlan: MealPlanEntity)

    @Query("DELETE FROM meal_plans WHERE id = :planId")
    suspend fun deleteMealPlan(planId: Long)
}