package net.deali.composeplayground

import androidx.lifecycle.SavedStateHandle
import kotlin.properties.PropertyDelegateProvider

fun <T> SavedStateHandle.mutableStateOf(
    defaultValue: T,
) = PropertyDelegateProvider<Any, SavedStateHandleDelegate<T>> { _, property ->
    SavedStateHandleDelegate(
        savedStateHandle = this,
        key = property.name,
        defaultValue = defaultValue,
    )
}