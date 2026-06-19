package fr.zerohour.toquetoque.ui.navigation

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Dining
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.zerohour.toquetoque.feature.add.AddRecipeScreen
import fr.zerohour.toquetoque.feature.category.CategoryScreen
import fr.zerohour.toquetoque.feature.detail.RecipeDetailScreen
import fr.zerohour.toquetoque.feature.home.HomeScreen

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Feed : Screen("feed", "Feed", Icons.Outlined.Dining)
    object Categories : Screen("categories", "Categories", Icons.Filled.Category)
    object Add : Screen("add", "Add", Icons.Outlined.Add)
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                tonalElevation = 8.dp
            ) {
                val navigationItems = listOf(Screen.Feed, Screen.Categories, Screen.Add)

                navigationItems.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title, style = MaterialTheme.typography.labelMedium) },
                        selected = currentRoute == screen.route,
                        onClick = {
                            if (currentRoute != screen.route) {
                                navController.navigate(screen.route) {

                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        },
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
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Categories.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeIn(animationSpec = tween(200))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -it / 3 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeOut(animationSpec = tween(200))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -it / 3 },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeIn(animationSpec = tween(200))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { it },
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMediumLow
                    )
                ) + fadeOut(animationSpec = tween(200))
            },

            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(
                Screen.Feed.route,
                enterTransition = { fadeIn(animationSpec = tween(150)) },
                exitTransition = { fadeOut(animationSpec = tween(150)) },
                popEnterTransition = { fadeIn(animationSpec = tween(150)) },
                popExitTransition = { fadeOut(animationSpec = tween(150)) }
            ) {
                Text("Feed Screen Placeholder", modifier = Modifier.padding(16.dp))
            }

            composable(Screen.Categories.route) {
                HomeScreen(
                    onCategoryClick = { categoryName -> navController.navigate("category/$categoryName")}
                )
            }

            composable(
                route = "category/{categoryName}",
                arguments = listOf(navArgument("categoryName") { type = NavType.StringType })
            ) { backStackEntry ->
                val categoryName = backStackEntry.arguments?.getString("categoryName") ?: ""

                CategoryScreen(
                    categoryName = categoryName,
                    onBackClick = { navController.popBackStack() },
                    onRecipeClick = { recipeId ->
                        navController.navigate("recipe_detail/$recipeId")
                    }
                )
            }

            composable(
                route = "recipe_detail/{recipeId}",
                arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
            ) { backStackEntry ->
                val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
                RecipeDetailScreen(
                    recipeId = recipeId,
                    onBackClick = { navController.popBackStack() },
                    onEditClick = { idToEdit ->
                        navController.navigate("add?recipeId=$idToEdit")
                    }
                )
            }

            composable(
                route = "add?recipeId={recipeId}",
                arguments = listOf(
                    navArgument("recipeId") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                ),
                enterTransition = {
                    slideInVertically(
                        initialOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(400)
                    ) + fadeIn(animationSpec = tween(400))
                },
                popExitTransition = {
                    slideOutVertically(
                        targetOffsetY = { fullHeight -> fullHeight },
                        animationSpec = tween(400)
                    ) + fadeOut(animationSpec = tween(400))
                }
            ) { backStackEntry ->
                val recipeIdToEdit = backStackEntry.arguments?.getString("recipeId")

                AddRecipeScreen(
                    recipeIdToEdit = recipeIdToEdit,
                    onSaveSuccess = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}