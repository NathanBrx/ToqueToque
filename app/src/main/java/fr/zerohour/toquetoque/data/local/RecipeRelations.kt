package fr.zerohour.toquetoque.data.local

import androidx.room.Embedded
import androidx.room.Relation

data class IngredientGroupWithIngredients(
    @Embedded val group: IngredientGroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId"
    )
    val ingredients: List<IngredientEntity>
)

data class InstructionGroupWithSteps(
    @Embedded val group: InstructionGroupEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "groupId"
    )
    val steps: List<InstructionStepEntity>
)

data class FullRecipe(
    @Embedded val recipe: RecipeEntity,

    @Relation(
        entity = IngredientGroupEntity::class,
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val ingredientGroups: List<IngredientGroupWithIngredients>,

    @Relation(
        entity = InstructionGroupEntity::class,
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val instructionGroups: List<InstructionGroupWithSteps>,

    @Relation(
        entity = RecipePhotoEntity::class,
        parentColumn = "id",
        entityColumn = "recipeId"
    )
    val photos: List<RecipePhotoEntity>
)