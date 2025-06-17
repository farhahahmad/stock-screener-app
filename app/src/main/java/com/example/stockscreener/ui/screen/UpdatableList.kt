package com.example.stockscreener.ui.screen

import com.example.stockscreener.data.model.Stock

interface UpdatableList {
    fun updateList(stocks: List<Stock>)
}