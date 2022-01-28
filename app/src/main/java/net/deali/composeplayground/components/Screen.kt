package net.deali.composeplayground.components

import androidx.annotation.StringRes
import net.deali.composeplayground.R

sealed class Screen(val route: String, @StringRes val resourceId: Int) {
    object MainHomeGoodsItems : Screen("MainHomeGoodsItems", R.string.home)
    object MainFavoriteGoodsItems : Screen("MainFavoriteGoodsItems", R.string.favorite)
    object AddGoodsItem : Screen("AddGoodsItem", R.string.add_item)
    object GoodsItemDetail : Screen("GoodsItemDetail", R.string.goods_item_detail)

    companion object {
        fun getTitle(route: String): String = when (route) {
            MainHomeGoodsItems.route -> {
                "홈"
            }
            MainFavoriteGoodsItems.route -> {
                "즐겨찾기"
            }
            AddGoodsItem.route -> {
                "상품 추가"
            }
            GoodsItemDetail.route -> {
                "상품 상세"
            }
            else -> {
                "홈"
            }
        }
    }
}