package fr.zerohour.toquetoque.ui.navigation

import android.app.Activity
import android.view.WindowManager
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import fr.zerohour.toquetoque.data.local.SettingsManager
import fr.zerohour.toquetoque.feature.add.AddRecipeScreen
import fr.zerohour.toquetoque.feature.category.CategoryScreen
import fr.zerohour.toquetoque.feature.detail.RecipeDetailScreen
import fr.zerohour.toquetoque.feature.home.HomeScreen
import fr.zerohour.toquetoque.feature.planner.PlannerScreen
import kotlinx.coroutines.launch

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Planner : Screen("planner", "Organisation", Icons.Outlined.Dining)
    object Categories : Screen("categories", "Categories", Icons.Filled.Category)
    object Add : Screen("add", "Add", Icons.Outlined.Add)
}

@Composable
fun MainScreen(settingsManager: SettingsManager) {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val context = LocalContext.current
    val activity = context as? Activity

    val themeMode by settingsManager.themeMode.collectAsState()
    val keepScreenOn by settingsManager.keepScreenOn.collectAsState()

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(keepScreenOn) {
        if (keepScreenOn) {
            activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        } else {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            AppDrawer(
                themeMode = themeMode,
                onThemeChange = { settingsManager.setThemeMode(it) },
                keepScreenOn = keepScreenOn,
                onKeepScreenOnChange = { settingsManager.setKeepScreenOn(it) }
            )
        }
    ) {
        Scaffold(
            bottomBar = {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    tonalElevation = 8.dp
                ) {
                    val navigationItems = listOf(Screen.Planner, Screen.Categories, Screen.Add)

                    navigationItems.forEach { screen ->
                        NavigationBarItem(
                            icon = { Icon(screen.icon, contentDescription = screen.title) },
                            label = {
                                Text(
                                    screen.title,
                                    style = MaterialTheme.typography.labelMedium
                                )
                            },
                            selected = currentRoute?.substringBefore("?") == screen.route,
                            onClick = {
                                if (currentRoute?.substringBefore("?") != screen.route) {
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
                    Screen.Planner.route,
                    enterTransition = { fadeIn(animationSpec = tween(150)) },
                    exitTransition = { fadeOut(animationSpec = tween(150)) },
                    popEnterTransition = { fadeIn(animationSpec = tween(150)) },
                    popExitTransition = { fadeOut(animationSpec = tween(150)) }
                ) {
                    PlannerScreen(
                        onRecipeClick = { recipeId ->
                            navController.navigate("recipe_detail/$recipeId")
                        }
                    )
                }

                composable(Screen.Categories.route) {
                    HomeScreen(
                        onCategoryClick = { categoryName ->
                            navController.navigate("category/$categoryName")
                        },
                        onRecipeClick = { recipeId ->
                            navController.navigate("recipe_detail/$recipeId")
                        },
                        onOpenDrawer = {
                            scope.launch { drawerState.open() }
                        }
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
                        },
                        onBackClick = {
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}