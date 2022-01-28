package net.deali.composeplayground.ui.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import net.deali.composeplayground.components.AddGoodsItemView
import net.deali.composeplayground.components.GoodsItemDetailView
import net.deali.composeplayground.components.MainFavoriteGoodsItemView
import net.deali.composeplayground.components.MainHome
import net.deali.composeplayground.components.Screen
import net.deali.composeplayground.models.GoodsItem
import net.deali.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val items = listOf(
                    Screen.MainHomeGoodsItems,
                    Screen.MainFavoriteGoodsItems,
                )

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                navBackStackEntry?.destination?.route?.let {
                                    Text(text = Screen.getTitle(it))
                                }
                            },
                            navigationIcon = if (navBackStackEntry?.destination?.route ?: "" != Screen.MainHomeGoodsItems.route) {
                                {
                                    IconButton(onClick = {
                                        if (navController.navigateUp().not()) {
                                            finishAffinity()
                                        }
                                    }) {
                                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                                    }
                                }
                            } else {
                                null
                            },
                            actions = {
                                if (navBackStackEntry?.destination?.route ?: "" != Screen.AddGoodsItem.route) {
                                    IconButton(onClick = {
                                        navController.navigate(Screen.AddGoodsItem.route)
                                    }) {
                                        Icon(Icons.Filled.Add, contentDescription = null)
                                    }
                                }
                            }
                        )
                    },
                    bottomBar = {
                        if (navBackStackEntry?.destination?.route ?: "" == Screen.MainHomeGoodsItems.route || navBackStackEntry?.destination?.route ?: "" == Screen.MainFavoriteGoodsItems.route)
                            BottomNavigation {
                                val currentDestination = navBackStackEntry?.destination
                                items.forEach { screen ->
                                    BottomNavigationItem(
                                        icon = {
                                            Icon(if (screen is Screen.MainHomeGoodsItems) {
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
                ) { paddingValues ->
                    NavHost(navController = navController, startDestination = Screen.MainHomeGoodsItems.route) {
                        val onBackPressed = {
                            navController.navigateUp()
                            Unit
                        }

                        composable(Screen.MainHomeGoodsItems.route) {
                            logNavigationBackQueue(navController)
                            val mainItems by vm.items.observeAsState(initial = listOf())
                            val isRefreshing by vm.isRefreshing.observeAsState(false)

                            MainHome(
                                modifier = Modifier.padding(paddingValues),
                                goodsItems = mainItems,
                                isRefreshing = isRefreshing,
                                onRefresh = { vm.refresh() },
                                onLoadMore = { vm.loadMore() },
                                goToDetail = {
                                    navController.navigate(Screen.GoodsItemDetail.route)
                                    //TODO KYH GoodsItem 전달 시 DetailView 갔다가 강제로 홈으로 오는 이슈 수정 해야함
                                    //                                    val json = Uri.encode(Gson().toJson(it))
                                    //                                    navController.navigate(Screen.GoodsItemDetail.route + "?goodsItem=${json}")
                                },
                                onBackPressed = {
                                    finish()
                                }
                            )
                        }
                        composable(Screen.MainFavoriteGoodsItems.route) {
                            logNavigationBackQueue(navController)
                            MainFavoriteGoodsItemView(onBackPressed = onBackPressed)
                        }
                        composable(Screen.AddGoodsItem.route) {
                            logNavigationBackQueue(navController)
                            AddGoodsItemView(navController = navController, onBackPressed = onBackPressed)
                        }

                        composable(route = Screen.GoodsItemDetail.route) {
                            GoodsItemDetailView(goodsItem = GoodsItem(name = "테스트"), navController = navController, onBackPressed = onBackPressed)
                            logNavigationBackQueue(navController)
                        }
                        //TODO KYH GoodsItem 전달 시 DetailView 갔다가 강제로 홈으로 오는 이슈 수정 해야함
                        //                        composable(route = Screen.GoodsItemDetail.route + "?goodsItem={goodsItem}", arguments =  listOf(navArgument("goodsItem") {
                        //                            type = GoodsNavType()
                        //                        })) {
                        //                            val goodsItem = navBackStackEntry?.arguments?.getParcelable("goodsItem") ?: GoodsItem("test")
                        //                            GoodsItemDetailView(goodsItem = goodsItem, navController = navController, onBackPressed = onBackPressed)
                        //                            logNavigationBackQueue(navController)
                        //                        }
                    }
                }
            }
        }
    }

    private fun logNavigationBackQueue(navController: NavHostController) {
        Log.e("kyh!!!", "navController.backQueue.size: " + navController.backQueue.size)
    }

}