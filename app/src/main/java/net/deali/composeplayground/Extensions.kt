package net.deali.composeplayground

import androidx.lifecycle.LiveData

val <T> LiveData<T>.valueNN
    get() = this.value!!