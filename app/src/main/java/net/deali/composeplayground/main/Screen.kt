package net.deali.composeplayground.main

import androidx.annotation.StringRes
import net.deali.composeplayground.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object Home : Screen("home", R.string.home)
    object Favorite : Screen("favorite", R.string.favorite)
    object AddItem : Screen("addItem", R.string.add_item)
}