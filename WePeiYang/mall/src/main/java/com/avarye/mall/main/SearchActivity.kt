package com.avarye.mall.main

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.StaggeredGridLayoutManager
import android.view.View
import com.avarye.mall.R
import com.avarye.mall.detail.DetailActivity
import com.avarye.mall.service.MallManager
import com.avarye.mall.service.ViewModel
import com.avarye.mall.service.searchLiveData
import com.avarye.mall.service.selectLiveData
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.bindNonNull
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemAdapter
import com.twt.wepeiyang.commons.ui.rec.ItemManager
import es.dmoral.toasty.Toasty
import kotlinx.android.synthetic.main.mall_activity_search.*
import kotlinx.android.synthetic.main.mall_item_toolbar.*

class SearchActivity : AppCompatActivity() {
    private var page = 1
    private var totalPage = 1
    private var isLoading = false
    private val itemManager = ItemManager()
    private val viewModel = ViewModel()
    private var key = ""
    private var type = ""
    private var which = MallManager.W_SALE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.mall_activity_search)
        window.statusBarColor = ContextCompat.getColor(this, R.color.mallColorMain)
        key = intent.getStringExtra(MallManager.KEY)
        type = intent.getStringExtra(MallManager.TYPE)
        if (type == MallManager.SELECT) {
            which = intent.getIntExtra(MallManager.MODE, MallManager.W_SALE)
        }
        tb_main.apply {
            title = when (type) {
                MallManager.SEARCH -> getString(R.string.mallStringResultSearch)
                MallManager.SELECT -> getString(R.string.mallStringResultSelect)
                else -> "薛定谔"
            }
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        srl_search.apply {
            setColorSchemeResources(R.color.mallColorMain)
            //下拉刷新加载监听
            setOnRefreshListener {
                if (!isLoading) {
                    isLoading = true
                    resetPage()
                    //redo
                    getData(page)
                    isRefreshing = false
                    Toasty.success(this@SearchActivity, "已刷新").show()
                }
                isLoading = false
            }
        }

        rv_search.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            adapter = ItemAdapter(itemManager)
            //加载下一页监听
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    if (!canScrollVertically(1) && page < totalPage && !isLoading) {
                        isLoading = true
                        //more
                        getData(++page)
                    }
                    isLoading = false
                }
            })
        }

        resetPage()
        getData(page)
        when (type) {
            MallManager.SEARCH -> bindSearch()
            MallManager.SELECT -> when (which) {
                MallManager.W_SALE -> bindSelectSale()
                MallManager.W_NEED -> bindSelectNeed()
            }
        }
    }

    private fun getData(page: Int) = when (type) {
        MallManager.SEARCH -> viewModel.search(key, page)
        MallManager.SELECT -> viewModel.getSelect(key, which, page)
        else -> Unit
    }

    private fun bindSearch() {
        searchLiveData.bindNonNull(this) { list ->
            totalPage = list[0].page
            if (totalPage == 0) {
                itemManager.autoRefresh { removeAll { it is RecItem } }
                iv_search_null.visibility = View.VISIBLE
                Toasty.info(this, "搜索结果为空TvT").show()
            } else {
                iv_search_null.visibility = View.GONE
                val items = mutableListOf<Item>().apply {
                    for (i in 1 until list.size) {
                        recItem {
                            Glide.with(this@SearchActivity)
                                    .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${list[i].imgurl}")
                                    .into(image)
                            name.text = list[i].name
                            price.text = list[i].price
                            locate.text = MallManager.dealText(list[i].location)
                            card.setOnClickListener {
                                val intent = Intent(this@SearchActivity, DetailActivity::class.java)
                                        .putExtra(MallManager.ID, list[i].id)
                                this@SearchActivity.startActivity(intent)
                            }
                        }
                    }
                }
                if (page == 1) {
                    itemManager.autoRefresh {
                        removeAll { it is RecItem }
                        addAll(items)
                    }
                } else {
                    itemManager.addAll(items)
                }
            }
        }
    }

    private fun bindSelectSale() {
        selectLiveData.bindNonNull(this) { list ->
            totalPage = list[0].page
            if (totalPage == 0) {
                itemManager.autoRefresh { removeAll { it is RecItem } }
                iv_search_null.visibility = View.VISIBLE
                Toasty.info(this, "此分类下无结果").show()
            } else {
                iv_search_null.visibility = View.GONE
                val items = mutableListOf<Item>().apply {
                    for (i in 1 until list.size) {
                        recItem {
                            Glide.with(this@SearchActivity)
                                    .load("https://mall.twt.edu.cn/api.php/Upload/img_redirect?id=${list[i].imgurl}")
                                    .into(image)
                            name.text = list[i].name
                            price.text = list[i].price
                            locate.text = MallManager.dealText(list[i].location)
                            card.setOnClickListener {
                                val intent = Intent(this@SearchActivity, DetailActivity::class.java)
                                        .putExtra(MallManager.ID, list[i].id)
                                this@SearchActivity.startActivity(intent)
                            }
                        }
                    }
                }
                if (page == 1) {
                    itemManager.autoRefresh {
                        removeAll { it is RecItem }
                        addAll(items)
                    }
                } else {
                    itemManager.addAll(items)
                }
            }
        }
    }

    private fun bindSelectNeed() {
        selectLiveData.bindNonNull(this) { list ->
            totalPage = list[0].page
            if (totalPage == 0) {
                itemManager.autoRefresh { removeAll { it is RecItem } }
                iv_search_null.visibility = View.VISIBLE
                Toasty.info(this, "此分类下无结果").show()
            } else {
                iv_search_null.visibility = View.GONE
                val items = mutableListOf<Item>().apply {
                    for (i in 1 until list.size) {
                        recItem {
                            name.text = list[i].name
                            price.text = list[i].price
                            locate.text = MallManager.dealText(list[i].location)
                            card.setOnClickListener {
                                val intent = Intent(this@SearchActivity, DetailActivity::class.java)
                                        .putExtra(MallManager.ID, list[i].id)
                                this@SearchActivity.startActivity(intent)
                            }
                        }
                    }
                }
                if (page == 1) {
                    itemManager.autoRefresh {
                        removeAll { it is RecItem }
                        addAll(items)
                    }
                } else {
                    itemManager.addAll(items)
                }
            }
        }
    }

    private fun resetPage() {
        page = 1
        totalPage = 1
    }
}