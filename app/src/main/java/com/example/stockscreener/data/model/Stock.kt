package com.example.stockscreener.data.model

data class Stock(
    val id: Int,
    val symbol: String,
    val name: String,
    val logo_url: String,
    val stock_price: StockPrice?,
    var is_favourite: Boolean = false
)

data class StockPrice(
    val current_price: Price?,
    val price_change: Double,
    val percentage_change: Double
)

data class Price(
    val amount: String,
    val currency: String
)

