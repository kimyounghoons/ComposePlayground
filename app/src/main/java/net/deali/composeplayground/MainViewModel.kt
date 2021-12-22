package net.deali.composeplayground

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

class MainViewModel(val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val _items = savedStateHandle.getLiveData(KEY_ITEMS, listOf(
        Item("1"),
        Item("2"),
        Item("3"),
        Item("4")
    ))
    val items: LiveData<List<Item>>
        get() = _items

    fun addItem() {
        _items.value?.let {
            _items.value = it + Item("아이템")
        }
    }

    fun deleteItem() {
        _items.value?.let {
            _items.value = _items.value!!.filterNot { item ->
                item === it.last()
            }
        }
    }

    companion object {
        val KEY_ITEMS = "KEY_ITEMS"
    }
}