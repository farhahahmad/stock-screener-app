package com.example.stockscreener.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.stockscreener.data.model.Stock
import com.example.stockscreener.data.repository.StockRepository

class StockViewModel(private val repository: StockRepository) : ViewModel() {
    private val _stocks = MutableLiveData<List<Stock>>()
    val stocks: LiveData<List<Stock>> = _stocks

    private val _starredIds = MutableLiveData<Set<Int>>(setOf())
    val starredIds: LiveData<Set<Int>> = _starredIds

    private val _starredStocks = MutableLiveData<List<Stock>>()
    val starredStocks: LiveData<List<Stock>> = _starredStocks

    init {
        stocks.observeForever { updateStarredStocks() }
        starredIds.observeForever { updateStarredStocks() }
    }

    fun loadStocks() {
        _stocks.value = repository.getStocks()
    }

    fun toggleFavourite(stockId: Int) {
        val currentFavourites = _starredIds.value ?: emptySet()
        _starredIds.value = if (stockId in currentFavourites) currentFavourites - stockId else currentFavourites + stockId
    }

    fun filterStocks(query: String): List<Stock> {
        return _stocks.value?.filter { it.name.contains(query, ignoreCase = true) } ?: emptyList()
    }

    private fun updateStarredStocks() {
        val currentStocks = _stocks.value ?: emptyList()
        val star = _starredIds.value ?: emptySet()
        _starredStocks.value = currentStocks.filter { it.id in star }
    }

    fun loadStarredStocks() {
        val allStocks = repository.getStocks()
        _stocks.value = allStocks
        val star = _starredIds.value ?: emptySet()
        _starredStocks.value = allStocks.filter { it.id in star }
    }

    fun filterStarredStocks(query: String): List<Stock> {
        val star = _starredIds.value ?: emptySet()
        return _stocks.value
            ?.filter { it.id in star && it.name.contains(query, ignoreCase = true) }
            ?: emptyList()
    }
}