package net.deali.composeplayground.components

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import net.deali.composeplayground.ui.main.MainViewModel

@Composable
fun MainFavoriteGoodsItemView(onBackPressed: () -> Unit) {
    BackHandler(enabled = true, onBack = onBackPressed)
}