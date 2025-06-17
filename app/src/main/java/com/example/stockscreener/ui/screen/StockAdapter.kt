package com.example.stockscreener.ui.screen

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stockscreener.R
import com.example.stockscreener.data.model.Stock
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide

class StockAdapter(
    private var stocks: List<Stock>,
    private var favouriteIds: Set<Int>,
    private val onFavouriteToggle: (Int) -> Unit
) : RecyclerView.Adapter<StockAdapter.StockViewHolder>() {

    inner class StockViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgLogo: ImageView = view.findViewById(R.id.imgLogo)
        val symbol: TextView = view.findViewById(R.id.txtSymbol)
        val name: TextView = view.findViewById(R.id.txtName)
        val currPrice: TextView = view.findViewById(R.id.txtCurrPrice)
        val priceChange: TextView = view.findViewById(R.id.txtPriceChange)
        val percentChange: TextView = view.findViewById(R.id.txtPercentChange)
        val favIcon: ImageView = view.findViewById(R.id.imgFavourite)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StockViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stock, parent, false)
        return StockViewHolder(view)
    }

    override fun onBindViewHolder(holder: StockViewHolder, position: Int) {
        val stock = stocks[position]
        val currPrice = stock.stock_price?.current_price?.amount ?: "-"
        val priceChange = stock.stock_price?.price_change ?: 0.0
        val percentageChange = stock.stock_price?.percentage_change ?: 0.0

        holder.name.text = stock.name
        holder.symbol.text = stock.symbol
        holder.currPrice.text = currPrice
        holder.priceChange.text = "$priceChange"
        holder.percentChange.text = "${percentageChange}%"

        val color = getPriceChangeColor(holder.itemView.context, percentageChange)
        holder.currPrice.setTextColor(color)

        val background = holder.percentChange.background
        if (background is GradientDrawable) {
            background.setColor(color)
        }

        Glide.with(holder.itemView).load(stock.logo_url).error(R.drawable.ic_error).into(holder.imgLogo)
        holder.favIcon.setImageResource(
            if (favouriteIds.contains(stock.id)) R.drawable.ic_star else R.drawable.ic_unstar
        )
        holder.favIcon.setOnClickListener { onFavouriteToggle(stock.id) }
    }

    override fun getItemCount(): Int = stocks.size

    private fun getPriceChangeColor(context: Context, change: Double): Int {
        return ContextCompat.getColor(context, if (change >= 0) R.color.green else R.color.red)
    }

    fun updateStocks(newStocks: List<Stock>) {
        stocks = newStocks
        notifyDataSetChanged()
    }

    fun updateFavourites(newFavourites: Set<Int>) {
        this.favouriteIds = newFavourites
        notifyDataSetChanged()
    }
}