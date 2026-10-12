package fr.zerohour.toquetoque.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "meal_plans")
data class MealPlanEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val date: String,
    val recipeId: String
)

data class MealPlanWithRecipe(
    @Embedded val mealPlan: MealPlanEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "id"
    )
    val recipe: RecipeEntity,
    @Relation(
        parentColumn = "recipeId",
        entityColumn = "recipeId"
    )
    val photos: List<RecipePhotoEntity>
)