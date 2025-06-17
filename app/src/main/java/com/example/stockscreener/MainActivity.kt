package com.example.stockscreener

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.stockscreener.data.repository.StockRepository
import com.example.stockscreener.databinding.ActivityMainBinding
import com.example.stockscreener.ui.screen.StockViewModel
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.example.stockscreener.ui.screen.AllStocksFragment
import com.example.stockscreener.ui.screen.StarredStocksFragment
import com.example.stockscreener.ui.screen.UpdatableList
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: StockViewModel
    private var currentTabPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return StockViewModel(StockRepository(applicationContext)) as T
            }
        })[StockViewModel::class.java]

        val fragments = listOf(
            AllStocksFragment(viewModel),
            StarredStocksFragment(viewModel)
        )

        val titles = listOf("All", "Starred")

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentTabPosition = position
            }
        })

        binding.viewPager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = fragments.size
            override fun createFragment(position: Int): Fragment = fragments[position]
        }

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = titles[position]
        }.attach()

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                val filtered = if (currentTabPosition == 0) {
                    viewModel.filterStocks(query)
                } else {
                    viewModel.filterStarredStocks(query)
                }

                val currentFragment = supportFragmentManager.findFragmentByTag("f$currentTabPosition")
                if (currentFragment is UpdatableList) {
                    currentFragment.updateList(filtered)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        viewModel.loadStocks()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_refresh -> {
                showLoading()
                Handler(Looper.getMainLooper()).postDelayed({
                    if (currentTabPosition == 0) {
                        viewModel.loadStocks()
                    } else {
                        viewModel.loadStarredStocks()
                    }
                    hideLoading()
                }, 500)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showLoading() {
        binding.loadingOverlay.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.loadingOverlay.visibility = View.GONE
    }
}