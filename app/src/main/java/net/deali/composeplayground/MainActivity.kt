package net.deali.composeplayground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import net.deali.composeplayground.main.AddItemComposable
import net.deali.composeplayground.main.MainHome
import net.deali.composeplayground.ui.theme.ComposePlaygroundTheme

class MainActivity : ComponentActivity() {
    val vm by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComposePlaygroundTheme {
                val navController = rememberNavController()

                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "Compose Playground")
                            },
                            navigationIcon = {
                                IconButton(onClick = { onBackPressed() }) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = null)
                                }
                            },
                            actions = {
                                IconButton(onClick = { navController.navigate("AddItem") }) {
                                    Icon(Icons.Filled.Add, contentDescription = null)
                                }
                            })
                    }
                ) {
                    NavHost(navController = navController, startDestination = "Main") {
                        composable("Main") {
                            MainHome(navController, vm) {
                                navController.navigateUp()
                            }
                        }
                        composable("AddItem") {
                            AddItemComposable(navController, vm) {
                                navController.navigateUp()
                            }
                        }
                    }
                }
            }
        }
    }

}