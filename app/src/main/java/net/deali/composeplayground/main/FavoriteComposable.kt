package net.deali.composeplayground.main

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import net.deali.composeplayground.MainViewModel

@Composable
fun FavoriteComposable(navController: NavHostController, vm: MainViewModel, onBackPressed: () -> Unit) {
    BackHandler(enabled = true, onBack = onBackPressed)
}