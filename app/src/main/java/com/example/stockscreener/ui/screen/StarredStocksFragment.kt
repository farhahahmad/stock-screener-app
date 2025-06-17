package com.example.stockscreener.ui.screen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.stockscreener.R
import com.example.stockscreener.data.model.Stock

class StarredStocksFragment(private val viewModel: StockViewModel) : Fragment(), UpdatableList {
    private lateinit var adapter: StockAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        val view = inflater.inflate(R.layout.fragment_stock_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = StockAdapter(emptyList(), viewModel.starredIds.value ?: emptySet()) {
            viewModel.toggleFavourite(it)
        }
        recyclerView.adapter = adapter

        viewModel.starredIds.observe(viewLifecycleOwner) { star ->
            val all = viewModel.stocks.value ?: emptyList()
            adapter.updateStocks(all.filter { star.contains(it.id) })
            adapter.updateFavourites(star)
        }

        viewModel.starredStocks.observe(viewLifecycleOwner) { starredList ->
            updateList(starredList)
        }

        return view
    }

    override fun updateList(stocks: List<Stock>) {
        adapter.updateStocks(stocks)
    }
}
