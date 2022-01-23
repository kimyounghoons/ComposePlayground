package net.deali.composeplayground

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(val title: String, val content: String? = null, val imageUrl: String = "https://ifh.cc/g/97ponc.webp") : Parcelable