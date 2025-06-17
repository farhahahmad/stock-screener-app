package com.example.stockscreener.data.repository

import android.content.Context
import com.example.stockscreener.data.model.Stock
import com.example.stockscreener.utils.JsonUtils

class StockRepository(private val context: Context) {
    fun getStocks(): List<Stock> {
        return JsonUtils.loadStocksFromAssets(context)
    }
}