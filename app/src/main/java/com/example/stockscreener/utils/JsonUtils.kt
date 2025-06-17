package com.example.stockscreener.utils

import android.content.Context
import com.example.stockscreener.data.model.Stock
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

object JsonUtils {
    fun loadStocksFromAssets(context: Context): List<Stock> {
        val json = context.assets.open("stocks.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(json)
        val stockArray = jsonObject.getJSONArray("stocks")
        val gson = Gson()
        val listType = object : TypeToken<List<Stock>>() {}.type
        return gson.fromJson(stockArray.toString(), listType)
    }
}