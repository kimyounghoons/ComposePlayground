package net.deali.composeplayground

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _items = savedStateHandle.getLiveData(KEY_ITEMS, listOf<Item>())
    val items: LiveData<List<Item>>
        get() = _items

    private val _isAllLoaded = savedStateHandle.getLiveData(KEY_IS_ALL_LOADED, false)
    val isAllLoaded: LiveData<Boolean>
        get() = _isAllLoaded

    private val _isLoading = savedStateHandle.getLiveData(KEY_IS_LOADING, false)
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private var pageCount = 1

    fun addItem() {
        _items.value = _items.valueNN.let {
            it + Item("아이템")
        }
    }

    fun deleteItem() {
        _items.value = _items.valueNN.let {
            _items.valueNN.filterNot { item ->
                item === it.last()
            }
        }
    }

    fun loadMore() {
        if (_isLoading.valueNN.not() && _isAllLoaded.valueNN.not()) {
            viewModelScope.launch {
                _isLoading.value = true
                delay(1000)
                val items = arrayListOf<Item>()
                repeat(20) {
                    items.add(Item("아이템 ${_items.valueNN.size + it + 1}"))
                }
                _items.value = _items.valueNN + items
                ++pageCount
                if (pageCount > 10) {
                    _isAllLoaded.value = true
                }
                _isLoading.value = false
            }
        }
    }

    companion object {
        val KEY_ITEMS = "KEY_ITEMS"
        val KEY_IS_LOADING = "KEY_IS_LOADING"
        val KEY_IS_ALL_LOADED = "KEY_IS_ALL_LOADED"
    }
}