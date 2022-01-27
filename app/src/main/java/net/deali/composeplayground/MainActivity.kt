package net.deali.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.deali.composeplayground.main.AddItemComposable
import net.deali.composeplayground.main.FavoriteComposable
import net.deali.composeplayground.main.MainHome
import net.deali.composeplayground.main.Screen
import net.deali.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                val navController = rememberNavController()
                val items = listOf(
                    Screen.Home,
                    Screen.Favorite,
                )
                val needBackButton by vm.needBackButton.observeAsState(initial = false)

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Compose Playground")
                            },
                            navigationIcon = if (needBackButton) {
                                {
                                    IconButton(onClick = {
                                        vm.showBackButton(false)
                                        navController.navigateUp()
                                    }) {
                                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                                    }
                                }
                            } else {
                                null
                            },
                            actions = {
                                IconButton(onClick = {
                                    vm.showBackButton(true)
                                    navController.navigate(Screen.AddItem.route)
                                }) {
                                    Icon(Icons.Filled.Add, contentDescription = null)
                                }
                            })
                    },
                    bottomBar = {
                        BottomNavigation {
                            val navBackStackEntry by navController.currentBackStackEntryAsState()
                            val currentDestination = navBackStackEntry?.destination
                            items.forEach { screen ->
                                BottomNavigationItem(
                                    icon = {
                                        Icon(if (screen is Screen.Home) {
                                            Icons.Filled.Home
                                        } else {
                                            Icons.Filled.Favorite
                                        }, contentDescription = null)
                                    },
                                    label = { Text(stringResource(screen.resourceId)) },
                                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                                    onClick = {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }
                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                )
                            }
                        }
                    }
                ) {
                    NavHost(navController = navController, startDestination = Screen.Home.route) {
                        composable(Screen.Home.route) {
                            MainHome(vm) {
                                navController.navigateUp()
                            }
                        }
                        composable(Screen.Favorite.route) {
                            FavoriteComposable(navController = navController, vm = vm) {
                                navController.navigateUp()
                            }
                        }
                        composable(Screen.AddItem.route) {
                            AddItemComposable(navController = navController, vm = vm) {
                                vm.showBackButton(false)
                                navController.navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }

}