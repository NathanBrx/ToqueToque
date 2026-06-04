package fr.zerohour.toquetoque.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

import fr.zerohour.toquetoque.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    Scaffold(
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
                navigationIcon = {
                    IconButton(onClick = { /* Open Drawer */ }) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* Open Search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
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
        bottomBar = {
            HomeBottomNavigation()
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Header
            Text(
                text = "Collection",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Vos recettes enregistrées",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(modifier = Modifier.fillMaxWidth().height(260.dp)) {
                // Left Column
                Column(modifier = Modifier.weight(1f).fillMaxHeight()) {
                    CategoryCard(title = "Entrées", modifier = Modifier.weight(1f), placeholderColor = Color(0xFFCFD8DC))
                    Spacer(modifier = Modifier.height(12.dp))
                    CategoryCard(title = "Desserts", modifier = Modifier.weight(1f), placeholderColor = Color(0xFFB0BEC5))
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Right Column
                CategoryCard(title = "Plats", modifier = Modifier.weight(1f).fillMaxHeight(), placeholderColor = Color(0xFF90A4AE))
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// ---------------------------------------------------------------------------
// Reusable UI Components
// ---------------------------------------------------------------------------

@Composable
fun CategoryCard(
    title: String,
    modifier: Modifier = Modifier,
    height: Dp = Dp.Unspecified,
    placeholderColor: Color
) {
    Box(
        modifier = modifier
            .then(if (height != Dp.Unspecified) Modifier.height(height) else Modifier)
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.large)
            .background(placeholderColor),    // TODO: Replace with Coil AsyncImage
        contentAlignment = Alignment.BottomStart
    ) {
        // Gradient
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                    )
                )
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = Color.White,
            modifier = Modifier.padding(16.dp)
        )
    }
}

@Composable
fun HomeBottomNavigation() {
    var selectedItem by remember { mutableIntStateOf(1) }
    val items = listOf("Feed", "Collection", "Ajouter")
    val icons = listOf(Icons.Outlined.Dining, Icons.Filled.Category, Icons.Outlined.Add)

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        contentColor = MaterialTheme.colorScheme.primary,
        tonalElevation = 8.dp
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(icons[index], contentDescription = item) },
                label = { Text(item, style = MaterialTheme.typography.labelMedium) },
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedIconColor = MaterialTheme.colorScheme.secondary,
                    selectedTextColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    unselectedTextColor = MaterialTheme.colorScheme.secondary,
                    indicatorColor = MaterialTheme.colorScheme.primaryFixed
                )
            )
        }
    }
}