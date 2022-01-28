package net.deali.composeplayground.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GoodsItem(val name: String, val price: String? = null, val imageUrl: String = "https://ifh.cc/g/97ponc.webp") : Parcelable