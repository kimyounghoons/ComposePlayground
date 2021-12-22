package net.deali.composeplayground

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Item(val title : String) : Parcelable