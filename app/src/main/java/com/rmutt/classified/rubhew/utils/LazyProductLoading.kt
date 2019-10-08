package com.rmutt.classified.rubhew.utils

interface LazyProductLoading {
    fun onProductLoadRequired(currentVisibleItem: Int)
}