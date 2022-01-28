package net.deali.composeplayground.navType

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson
import net.deali.composeplayground.models.GoodsItem

class GoodsNavType : NavType<GoodsItem>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): GoodsItem? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): GoodsItem {
        return Gson().fromJson(value, GoodsItem::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: GoodsItem) {
        bundle.putParcelable(key, value)
    }
}