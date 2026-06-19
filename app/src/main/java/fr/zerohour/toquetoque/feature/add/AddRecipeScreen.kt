package fr.zerohour.toquetoque.feature.add

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import fr.zerohour.toquetoque.R
import fr.zerohour.toquetoque.data.local.*
import java.util.UUID
import androidx.core.net.toUri

data class IngredientInput(
    val id: String = UUID.randomUUID().toString(),
    val quantity: String = "",
    val unit: String = "",
    val name: String = ""
)

class IngredientGroup {
    val id: String = UUID.randomUUID().toString()
    var title by mutableStateOf("")
    val ingredients = mutableStateListOf(IngredientInput())
}

class InstructionGroup {
    val id: String = UUID.randomUUID().toString()
    var title by mutableStateOf("")
    val steps = mutableStateListOf("")
}

data class OptionalTimeConfig(
    val checkboxText: String,
    val inputLabel: String,
    val placeholder: String,
    val isChecked: Boolean,
    val onCheckedChange: (Boolean) -> Unit,
    val value: String,
    val onValueChange: (String) -> Unit
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddRecipeScreen(viewModel: AddRecipeViewModel = viewModel(factory = AddRecipeViewModel.Factory), onSaveSuccess: () -> Unit, recipeIdToEdit: String? = null) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var prepTime by remember { mutableStateOf("") }
    var servings by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf("") }

    var hasCookTime by remember { mutableStateOf(false) }
    var hasCoolingTime by remember { mutableStateOf(false) }
    var hasFreezingTime by remember { mutableStateOf(false) }
    var cookTime by remember { mutableStateOf("") }
    var coolingTime by remember { mutableStateOf("") }
    var freezingTime by remember { mutableStateOf("") }

    val ingredientGroups = remember { mutableStateListOf(IngredientGroup()) }
    val instructionGroups = remember { mutableStateListOf(InstructionGroup()) }

    var selectedPhotoUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val context = LocalContext.current

    LaunchedEffect(recipeIdToEdit) {
        if (recipeIdToEdit != null) {
            val existingRecipe = viewModel.getRecipeById(recipeIdToEdit)
            if (existingRecipe != null) {
                title = existingRecipe.recipe.title
                description = existingRecipe.recipe.description
                prepTime = existingRecipe.recipe.prepTime
                cookTime = existingRecipe.recipe.cookTime
                coolingTime = existingRecipe.recipe.coolingTime
                freezingTime = existingRecipe.recipe.freezingTime
                servings = existingRecipe.recipe.servings
                selectedType = existingRecipe.recipe.selectedTag
                hasCookTime = existingRecipe.recipe.cookTime.isNotBlank()
                hasCoolingTime = existingRecipe.recipe.coolingTime.isNotBlank()
                hasFreezingTime = existingRecipe.recipe.freezingTime.isNotBlank()

                selectedPhotoUris = existingRecipe.photos.map { it.photoUri.toUri() }

                ingredientGroups.clear()

                existingRecipe.ingredientGroups.forEach { dbGroupData ->
                    val newGroup = IngredientGroup().apply {
                        this.title = dbGroupData.group.title
                        this.ingredients.clear()

                        dbGroupData.ingredients.forEach { dbIngredient ->
                            ingredients.add(
                                IngredientInput(
                                    quantity = dbIngredient.quantity,
                                    unit = dbIngredient.unit,
                                    name = dbIngredient.name
                                )
                            )
                        }
                    }
                    ingredientGroups.add(newGroup)
                }

                instructionGroups.clear()

                existingRecipe.instructionGroups.forEach { dbGroupData ->
                    val newGroup = InstructionGroup().apply {
                        this.title = dbGroupData.group.title
                        this.steps.clear()

                        dbGroupData.steps.sortedBy { it.stepIndex }.forEach { dbStep ->
                            steps.add(dbStep.text)
                        }
                    }
                    instructionGroups.add(newGroup)
                }
            }
        }
    }

    Scaffold(
        // top bar : logo + nom
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row() {
                        Text (
                            text = "Toque",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_toque_logo),
                            contentDescription = "Toque Toque Logo",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.height(30.dp)
                        )
                        Spacer(modifier = Modifier.width(5.dp))
                        Text (
                            text = "Toque",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Unspecified,
                    navigationIconContentColor = Color.Unspecified,
                    titleContentColor = Color.Unspecified,
                    actionIconContentColor = Color.Unspecified
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            // --- titre principal ---
            Column {
                val mainTitle = if (recipeIdToEdit != null) {
                    stringResource(R.string.edit_recipe)
                } else {
                    stringResource(R.string.add_recipe)
                }
                Text(text = mainTitle, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onSurface)
            }

            // --- photo(s) ---
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Photos", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)

                val maxPhotos = 5

                // --- photo picker ---
                val photoPickerLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickMultipleVisualMedia(maxItems = maxPhotos)
                ) { uris ->
                    if (uris.isNotEmpty()) {
                        selectedPhotoUris = (selectedPhotoUris + uris).distinct().take(maxPhotos)
                    }
                }

                if (selectedPhotoUris.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp)
                            .clip(MaterialTheme.shapes.large)
                            .dashedBorder(
                                color = MaterialTheme.colorScheme.outlineVariant,
                                width = 2.dp,
                                radius = 16.dp
                            )
                            .background(MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.5f))
                            .clickable {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(horizontal = 16.dp)
                        ) {
                            Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surfaceContainer, modifier = Modifier.size(48.dp)) {
                                Icon(Icons.Outlined.AddAPhoto, contentDescription = "Add Photo", modifier = Modifier.padding(12.dp), tint = MaterialTheme.colorScheme.secondary)
                            }
                            Text(text = stringResource(R.string.upload_photos), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurface, textAlign = TextAlign.Center)
                            Text(text = stringResource(R.string.upload_photos_sec), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary, textAlign = TextAlign.Center)
                        }
                    }
                } else {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(selectedPhotoUris) { uri ->
                            Box(
                                modifier = Modifier
                                    .size(160.dp)
                                    .clip(MaterialTheme.shapes.large)
                            ) {
                                AsyncImage(
                                    model = uri,
                                    contentDescription = "Selected Recipe Photo",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )

                                Surface(
                                    shape = CircleShape,
                                    color = MaterialTheme.colorScheme.surfaceContainerLowest.copy(alpha = 0.8f),
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .size(32.dp)
                                        .clickable {
                                            selectedPhotoUris = selectedPhotoUris.filter { it != uri }
                                        }
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove Photo",
                                        modifier = Modifier.padding(6.dp),
                                        tint = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        if (selectedPhotoUris.size < maxPhotos) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .size(160.dp)
                                        .clip(MaterialTheme.shapes.large)
                                        .dashedBorder(
                                            color = MaterialTheme.colorScheme.outlineVariant,
                                            width = 2.dp,
                                            radius = 16.dp
                                        )
                                        .clickable {
                                            photoPickerLauncher.launch(
                                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                            )
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Icon(Icons.Outlined.AddAPhoto, contentDescription = "Add More", tint = MaterialTheme.colorScheme.secondary)
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text("Add More", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // --- détails ---
            Surface(
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainerLowest,
                shadowElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                    // --- titre ---
                    ToqueToqueInputField(value = title, onValueChange = { title = it }, placeholder = stringResource(R.string.title_example), label = stringResource(R.string.title))

                    // --- description ---
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(stringResource(R.string.story_description), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        TextField(
                            value = description,
                            onValueChange = { description = it },
                            placeholder = { Text(stringResource(R.string.story_desc_ex), color = MaterialTheme.colorScheme.tertiaryContainer, style = MaterialTheme.typography.labelMedium) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .defaultMinSize(minHeight = 100.dp),
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            ),
                            shape = MaterialTheme.shapes.medium
                        )
                    }

                    // --- type ---
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("Type", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val types = listOf(stringResource(R.string.starters), stringResource(R.string.main_courses), "Desserts")

                            types.forEach { type ->
                                val isSelected = (type == selectedType)

                                RecipeType(
                                    text = type,
                                    icon = if (isSelected) Icons.Default.Check else Icons.Default.Add,
                                    isSelected = isSelected,
                                    onClick = { selectedType = type }
                                )
                            }
                        }
                    }

                    // --- portions, temps de préparation, cuisson, réfrigération, congélation ---
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.Bottom
                    ) {
                        ToqueToqueInputField(
                            value = prepTime,
                            onValueChange = { prepTime = it },
                            placeholder = "15",
                            label = stringResource(R.string.prep_time),
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number
                        )
                        ToqueToqueInputField(
                            value = servings,
                            onValueChange = { servings = it},
                            placeholder = "4",
                            label = stringResource(R.string.servings),
                            modifier = Modifier.weight(1f),
                            keyboardType = KeyboardType.Number
                        )
                    }

                    val optionalTimes = listOf(
                        OptionalTimeConfig(
                            checkboxText = stringResource(R.string.requires_cooking),
                            inputLabel = stringResource(R.string.cook_time),
                            placeholder = "45",
                            isChecked = hasCookTime,
                            onCheckedChange = { hasCookTime = it; if (!it) cookTime = "" },
                            value = cookTime,
                            onValueChange = { cookTime = it }
                        ),
                        OptionalTimeConfig(
                            checkboxText = stringResource(R.string.requires_cooling),
                            inputLabel = stringResource(R.string.cooling_time),
                            placeholder = "20",
                            isChecked = hasCoolingTime,
                            onCheckedChange = { hasCoolingTime = it; if (!it) coolingTime = "" },
                            value = coolingTime,
                            onValueChange = { coolingTime = it }
                        ),
                        OptionalTimeConfig(
                            checkboxText = stringResource(R.string.requires_freezing),
                            inputLabel = stringResource(R.string.freezing_time),
                            placeholder = "120",
                            isChecked = hasFreezingTime,
                            onCheckedChange = { hasFreezingTime = it; if (!it) freezingTime = "" },
                            value = freezingTime,
                            onValueChange = { freezingTime = it }
                        )
                    )

                    optionalTimes.forEach { config ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Checkbox(
                                    checked = config.isChecked,
                                    onCheckedChange = config.onCheckedChange
                                )
                                Text(
                                    config.checkboxText,
                                    style = MaterialTheme.typography.labelLarge
                                )
                            }

                            Box(modifier = Modifier.weight(1f)) {
                                if (config.isChecked) {
                                    ToqueToqueInputField(
                                        value = config.value,
                                        onValueChange = config.onValueChange,
                                        placeholder = config.placeholder,
                                        label = config.inputLabel,
                                        keyboardType = KeyboardType.Number
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // --- ingredients ---
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(stringResource(R.string.ingredients), style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    TextButton(
                        onClick = { ingredientGroups.add(IngredientGroup()) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Outlined.AddCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.add_group), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }

                // --- groupes et ingrédients de chaque groupe ---
                ingredientGroups.forEachIndexed { groupIndex, group ->
                    key(group.id) {
                        Surface(
                            shape = MaterialTheme.shapes.large,
                            color = MaterialTheme.colorScheme.surfaceContainerLowest,
                            shadowElevation = 2.dp,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {

                                // --- groupe ---
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    ToqueToqueInputField(
                                        value = group.title,
                                        onValueChange = { group.title = it },
                                        placeholder = stringResource(R.string.group_name),
                                        modifier = Modifier.weight(1f)
                                    )

                                    if (ingredientGroups.size > 1) {
                                        IconButton(onClick = { ingredientGroups.removeAt(groupIndex) }) {
                                            Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Group", tint = MaterialTheme.colorScheme.error)
                                        }
                                    }
                                }

                                HorizontalDivider(
                                    Modifier,
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                                )

                                // --- liste ingrédients ---
                                group.ingredients.forEachIndexed { index, ingredient ->
                                    key(ingredient.id) {
                                        var isDragging by remember { mutableStateOf(false) }
                                        var dragOffsetY by remember { mutableFloatStateOf(0f) }
                                        var itemHeightPx by remember { mutableFloatStateOf(0f) }

                                        val elevation by animateDpAsState(if (isDragging) 8.dp else 0.dp, label = "elevation")
                                        val scale by animateFloatAsState(if (isDragging) 1.02f else 1f, label = "scale")
                                        val bgColor by animateColorAsState(
                                            targetValue = if (isDragging) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Transparent,
                                            label = "bgColor"
                                        )

                                        val updateIngredient = { updatedIngredient: IngredientInput ->
                                            val currentIndex = group.ingredients.indexOfFirst { it.id == ingredient.id }
                                            if (currentIndex != -1) {
                                                group.ingredients[currentIndex] = updatedIngredient
                                            }
                                        }

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .onGloballyPositioned { coordinates ->
                                                    itemHeightPx = coordinates.size.height.toFloat()
                                                }
                                                .zIndex(if (isDragging) 1f else 0f)
                                                .graphicsLayer {
                                                    translationY = dragOffsetY
                                                    scaleX = scale
                                                    scaleY = scale
                                                    shadowElevation = elevation.toPx()
                                                    shape = RoundedCornerShape(8.dp)
                                                }
                                                .background(bgColor, RoundedCornerShape(8.dp))
                                                .padding(4.dp),
                                            verticalAlignment = Alignment.Top,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            // Drag Icon
                                            Icon(
                                                imageVector = Icons.Default.DragIndicator,
                                                contentDescription = "Drag",
                                                tint = MaterialTheme.colorScheme.surfaceContainerHighest,
                                                modifier = Modifier
                                                    .padding(top = 16.dp)
                                                    .pointerInput(Unit) {
                                                        detectDragGesturesAfterLongPress(
                                                            onDragStart = {
                                                                isDragging = true
                                                            },
                                                            onDragEnd = {
                                                                isDragging = false
                                                                dragOffsetY = 0f
                                                            },
                                                            onDragCancel = {
                                                                isDragging = false
                                                                dragOffsetY = 0f
                                                            },
                                                            onDrag = { change, dragAmount ->
                                                                change.consume()
                                                                dragOffsetY += dragAmount.y

                                                                val liveIndex = group.ingredients.indexOfFirst { it.id == ingredient.id }
                                                                if (liveIndex == -1) return@detectDragGesturesAfterLongPress

                                                                val swapThreshold = itemHeightPx * 0.5f

                                                                if (dragOffsetY > swapThreshold && liveIndex < group.ingredients.size - 1) {
                                                                    val temp = group.ingredients[liveIndex]
                                                                    group.ingredients[liveIndex] = group.ingredients[liveIndex + 1]
                                                                    group.ingredients[liveIndex + 1] = temp
                                                                    dragOffsetY -= itemHeightPx
                                                                } else if (dragOffsetY < -swapThreshold && liveIndex > 0) {
                                                                    val temp = group.ingredients[liveIndex]
                                                                    group.ingredients[liveIndex] = group.ingredients[liveIndex - 1]
                                                                    group.ingredients[liveIndex - 1] = temp
                                                                    dragOffsetY += itemHeightPx
                                                                }
                                                            }
                                                        )
                                                    }
                                            )

                                            // --- saisie des ingrédients ---
                                            Column(
                                                modifier = Modifier.weight(1f),
                                                verticalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                ToqueToqueInputField(
                                                    value = ingredient.name,
                                                    onValueChange = { updateIngredient(ingredient.copy(name = it)) },
                                                    placeholder = stringResource(R.string.ingredient),
                                                    modifier = Modifier.fillMaxWidth()
                                                )
                                                Row(
                                                    modifier = Modifier.fillMaxWidth(),
                                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                                ) {
                                                    ToqueToqueInputField(
                                                        value = ingredient.quantity,
                                                        onValueChange = { updateIngredient(ingredient.copy(quantity = it)) },
                                                        placeholder = stringResource(R.string.quantity),
                                                        modifier = Modifier.weight(1f),
                                                        keyboardType = KeyboardType.Number
                                                    )
                                                    ToqueToqueInputField(
                                                        value = ingredient.unit,
                                                        onValueChange = { updateIngredient(ingredient.copy(unit = it)) },
                                                        placeholder = stringResource(R.string.unit),
                                                        modifier = Modifier.weight(1f)
                                                    )
                                                }
                                            }

                                            // --- bouton supprimer ingrédient ---
                                            IconButton(
                                                onClick = { group.ingredients.removeAt(group.ingredients.indexOfFirst { it.id == ingredient.id }) },
                                                modifier = Modifier.padding(top = 4.dp)
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = "Delete",
                                                    tint = MaterialTheme.colorScheme.error
                                                )
                                            }
                                        }
                                    }
                                }

                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(48.dp)
                                        .clip(MaterialTheme.shapes.medium)
                                        .dashedBorder(
                                            color = MaterialTheme.colorScheme.outlineVariant,
                                            width = 1.dp,
                                            radius = 8.dp
                                        )
                                        .clickable { group.ingredients.add(IngredientInput()) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(stringResource(R.string.add_ingredient), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                                }
                            }
                        }
                    }
                }
            }

            // --- groupes  d'instructions ---
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Instructions", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
                    TextButton(
                        onClick = { instructionGroups.add(InstructionGroup()) },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(Icons.Outlined.AddCircleOutline, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.add_group), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    }
                }

                // --- groupes ---
                instructionGroups.forEachIndexed { groupIndex, group ->
                    key(group.id) {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(top = if (groupIndex > 0) 16.dp else 0.dp) // Extra spacing between groups
                            ) {
                                ToqueToqueInputField(
                                    value = group.title,
                                    onValueChange = { group.title = it },
                                    placeholder = stringResource(R.string.group_name),
                                    label = null,
                                    modifier = Modifier.weight(1f)
                                )

                                // --- bouton supprimer pour groupes 1+ ---
                                if (instructionGroups.size > 1) {
                                    IconButton(onClick = { instructionGroups.removeAt(groupIndex) }) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Delete Group", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }

                            // --- étapes du groupe ---
                            group.steps.forEachIndexed { stepIndex, stepText ->
                                Box {
                                    Surface(
                                        shape = MaterialTheme.shapes.large,
                                        color = MaterialTheme.colorScheme.surfaceContainerLowest,
                                        shadowElevation = 2.dp,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 12.dp, start = 8.dp)
                                    ) {
                                        Box {
                                            TextField(
                                                value = stepText,
                                                onValueChange = { group.steps[stepIndex] = it },
                                                placeholder = {
                                                    Text(
                                                        stringResource(R.string.describe_step),
                                                        color = MaterialTheme.colorScheme.tertiaryContainer,
                                                        style = MaterialTheme.typography.labelMedium
                                                    )
                                                },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .defaultMinSize(minHeight = 100.dp)
                                                    .padding(start = 16.dp, top = 24.dp, end = 16.dp, bottom = 16.dp),
                                                singleLine = false,
                                                colors = TextFieldDefaults.colors(
                                                    focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                                    unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLow,
                                                    focusedIndicatorColor = Color.Transparent,
                                                    unfocusedIndicatorColor = Color.Transparent
                                                ),
                                                shape = MaterialTheme.shapes.medium
                                            )

                                            // --- button supprimer pour étapes 2+ ---
                                            if (stepIndex > 0) {
                                                IconButton(
                                                    onClick = { group.steps.removeAt(stepIndex) },
                                                    modifier = Modifier
                                                        .align(Alignment.TopEnd)
                                                        .padding(top = 16.dp, end = 8.dp)
                                                ) {
                                                    Icon(
                                                        imageVector = Icons.Default.Close,
                                                        contentDescription = "Delete step",
                                                        tint = MaterialTheme.colorScheme.error
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    Surface(
                                        shape = CircleShape,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                "${stepIndex + 1}",
                                                color = MaterialTheme.colorScheme.onPrimary,
                                                fontWeight = FontWeight.Bold,
                                                style = MaterialTheme.typography.labelMedium
                                            )
                                        }
                                    }
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .clip(MaterialTheme.shapes.large)
                                    .dashedBorder(
                                        color = MaterialTheme.colorScheme.outlineVariant,
                                        width = 1.dp,
                                        radius = 16.dp
                                    )
                                    .clickable { group.steps.add("") },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(stringResource(R.string.add_step), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.secondary)
                            }

                            if (groupIndex < instructionGroups.size - 1) {
                                HorizontalDivider(
                                    modifier = Modifier.padding(top = 16.dp),
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                                )
                            }
                        }
                    }
                }
            }

            // --- bouton sauvegarder ---
            Button(
                onClick = {
                    selectedPhotoUris.forEach { uri ->
                        try {
                            context.contentResolver.takePersistableUriPermission(
                                uri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                            )
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }

                    val uriStrings = selectedPhotoUris.map { it.toString() }

                    viewModel.saveOrUpdateRecipe(
                        recipeIdToEdit, title, description, prepTime, servings, cookTime,
                        coolingTime, freezingTime, selectedType,
                        ingredientGroups, instructionGroups, uriStrings
                    )
                    onSaveSuccess()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = MaterialTheme.shapes.medium,
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(stringResource(R.string.save_recipe), style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// --- composants réutilisables ---

@Composable
        /**
         * Crée un OutlinedTextField self contained dans une Column, avec les spécifications données en arguments
         */
fun ToqueToqueInputField(value: String, onValueChange: (String) -> Unit, placeholder: String, modifier: Modifier = Modifier, label: String? = null, keyboardType: KeyboardType = KeyboardType.Text) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = modifier) {
        if (label != null) Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = MaterialTheme.colorScheme.tertiaryContainer, style = MaterialTheme.typography.labelMedium) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )
    }
}

@Composable
        /**
         * Crée un bouton "pilule" avec les spécifications données en arguments
         */
fun RecipeType(text: String, icon: ImageVector, isSelected: Boolean, isCustom: Boolean = false, onClick: () -> Unit) {
    val bgColor = when {
        isSelected -> MaterialTheme.colorScheme.primaryContainer
        isCustom -> MaterialTheme.colorScheme.surfaceContainerHigh
        else -> MaterialTheme.colorScheme.secondaryContainer
    }
    val contentColor = when {
        isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
        isCustom -> MaterialTheme.colorScheme.onSurfaceVariant
        else -> MaterialTheme.colorScheme.onSecondaryContainer
    }

    Surface(
        shape = CircleShape,
        color = bgColor,
        modifier = Modifier.clip(CircleShape).clickable { onClick() }
    ) {
        Row(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = contentColor)
            Text(text, style = MaterialTheme.typography.labelMedium, color = contentColor)
        }
    }
}

/**
 * Modifier personnalisé pour les bordures pointillées
 */
fun Modifier.dashedBorder(color: Color, width: Dp, radius: Dp) = this.drawBehind {
    drawRoundRect(
        color = color,
        style = Stroke(
            width = width.toPx(),
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f)
        ),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(radius.toPx())
    )
}