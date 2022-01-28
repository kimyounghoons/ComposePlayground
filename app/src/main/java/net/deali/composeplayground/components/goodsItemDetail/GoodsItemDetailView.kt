package net.deali.composeplayground.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import net.deali.composeplayground.models.GoodsItem

@Composable
fun GoodsItemDetailView(goodsItem: GoodsItem, navController: NavController, onBackPressed: () -> Unit) {
    BackHandler(enabled = true, onBack = onBackPressed)

    Column {
        Text(text = "상품명 : ${goodsItem.name}")
        Text(text = "상품 추가 화면 이동",
            Modifier
                .background(Color.Blue)
                .size(200.dp)
                .clickable {
                    navController.navigate(Screen.AddGoodsItem.route)
                })
        Text(text = "홈으로",
            Modifier
                .size(200.dp)
                .background(Color.Yellow)
                .clickable {
                    navController.popBackStack(Screen.MainHomeGoodsItems.route, false)
                })
    }
}