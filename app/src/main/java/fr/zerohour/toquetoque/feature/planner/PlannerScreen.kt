package fr.zerohour.toquetoque.feature.planner

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.background
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale
import fr.zerohour.toquetoque.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlannerScreen() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabs = listOf("Menu de la semaine", "Liste de courses")

    Scaffold(
        // top bar : logo + nom
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row {
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
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
        ) {
            PrimaryTabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.primary
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    )
                }
            }

            when (selectedTabIndex) {
                0 -> WeeklyMealPlannerView()
                1 -> GroceryListView()
            }
        }
    }
}
@Composable
fun WeeklyMealPlannerView(viewModel: PlannerViewModel = viewModel(factory = PlannerViewModel.Factory)) {
    val mealPlans by viewModel.mealPlans.collectAsState()
    val allRecipes by viewModel.allRecipes.collectAsState()

    // date sélectionnée
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showRecipeDialog by remember { mutableStateOf(false) }

    // timeline: 7 jours avant, 14 jours après
    val calendarDates = remember {
        val today = LocalDate.now()
        (-7..14).map { today.plusDays(it.toLong()) }
    }

    // premier élément affiché : aujourd'hui
    val listState = rememberLazyListState(initialFirstVisibleItemIndex = 7)

    val coroutineScope = rememberCoroutineScope()
    val today = remember { LocalDate.now() }

    Column(modifier = Modifier.fillMaxSize()) {

        // --- Row pour les jours ---
        LazyRow(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(calendarDates) { date ->
                val isSelected = date == selectedDate
                val hasMeals = mealPlans.any { it.mealPlan.date == date.toString() }

                val dayOfWeek = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.FRENCH).uppercase()
                val dayOfMonth = date.dayOfMonth.toString()
                val isToday = date == today

                Column(
                    modifier = Modifier
                        .width(56.dp)
                        .height(76.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { selectedDate = date },
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = dayOfWeek,
                        style = MaterialTheme.typography.labelSmall,
                        color = when {
                            isSelected -> MaterialTheme.colorScheme.onPrimary
                            isToday -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.onSurfaceVariant
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dayOfMonth,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = when {
                            isSelected -> MaterialTheme.colorScheme.onPrimary
                            isToday -> MaterialTheme.colorScheme.primary
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    Box( // point indicateur de présence de repas sur le jour
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isSelected && hasMeals -> MaterialTheme.colorScheme.onPrimary
                                    !isSelected && hasMeals -> MaterialTheme.colorScheme.primary
                                    else -> Color.Transparent
                                }
                            )
                    )
                }
            }
        }

        HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))

        // --- liste de repas du jour sélectionné ---
        val dateFormatted = "${selectedDate.dayOfMonth} ${selectedDate.month.getDisplayName(TextStyle.FULL, Locale.FRENCH)}"
        val mealsForSelectedDate = mealPlans.filter { it.mealPlan.date == selectedDate.toString() }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = dateFormatted,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                TextButton( // bouton pour retourner à aujourd'hui
                    onClick = {
                        selectedDate = today
                        coroutineScope.launch {
                            listState.animateScrollToItem(7)
                        }
                    }
                ) {
                    Text(stringResource(R.string.today))
                }

                Spacer(modifier = Modifier.width(8.dp))

                FilledTonalIconButton(onClick = { showRecipeDialog = true }) {
                    Icon(Icons.Default.Add, contentDescription = stringResource(R.string.add_meal))
                }
            }
        }

        // contenu du jour (rien ou liste de repas)
        if (mealsForSelectedDate.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                Text(stringResource(R.string.no_meal_day), color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize().weight(1f),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(mealsForSelectedDate, key = { it.mealPlan.id }) { plan ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = plan.recipe.title, style = MaterialTheme.typography.bodyLarge)
                            IconButton(onClick = { viewModel.removeMealPlan(plan.mealPlan.id) }) {
                                Icon(Icons.Default.Delete, contentDescription = stringResource(R.string.delete), tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }

    // --- fenêtre d'ajout de repas ---
    if (showRecipeDialog) {
        AlertDialog(
            onDismissRequest = { showRecipeDialog = false },
            title = { Text(stringResource(R.string.add_to_menu)) },
            text = {
                LazyColumn(modifier = Modifier.height(300.dp)) {
                    items(allRecipes) { fullRecipe ->
                        Text(
                            text = fullRecipe.recipe.title,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.assignRecipeToDate(selectedDate, fullRecipe.recipe.id)
                                    showRecipeDialog = false
                                }
                                .padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                        HorizontalDivider()
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showRecipeDialog = false }) { Text(stringResource(R.string.cancel)) }
            }
        )
    }
}

@Composable
fun GroceryListView() {
    Text("La liste de courses fusionnée ira ici.", modifier = Modifier.padding(16.dp))
}