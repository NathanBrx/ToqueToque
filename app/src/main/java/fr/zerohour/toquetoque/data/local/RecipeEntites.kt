package fr.zerohour.toquetoque.data.local

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String,
    val prepTime: String,
    val servings: String,
    val cookTime: String,
    val coolingTime: String,
    val freezingTime: String,
    val selectedTag: String
)

@Entity(
    tableName = "ingredient_groups",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IngredientGroupEntity(
    @PrimaryKey val id: String,
    val recipeId: String,
    val title: String
)

@Entity(
    tableName = "ingredients",
    foreignKeys = [
        ForeignKey(
            entity = IngredientGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class IngredientEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val quantity: String,
    val unit: String,
    val name: String
)

@Entity(
    tableName = "instruction_groups",
    foreignKeys = [
        ForeignKey(
            entity = RecipeEntity::class,
            parentColumns = ["id"],
            childColumns = ["recipeId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class InstructionGroupEntity(
    @PrimaryKey val id: String,
    val recipeId: String,
    val title: String
)

@Entity(
    tableName = "instruction_steps",
    foreignKeys = [
        ForeignKey(
            entity = InstructionGroupEntity::class,
            parentColumns = ["id"],
            childColumns = ["groupId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class InstructionStepEntity(
    @PrimaryKey val id: String,
    val groupId: String,
    val stepIndex: Int,
    val text: String
)