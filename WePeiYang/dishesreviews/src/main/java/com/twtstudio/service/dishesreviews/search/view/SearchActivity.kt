package com.twtstudio.service.dishesreviews.search.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.twtstudio.service.dishesreviews.R

class SearchActivity : AppCompatActivity(){
    private lateinit var toolbar: Toolbar
    private lateinit var searchView: SearchView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dishes_reviews_activity_search)
        toolbar=findViewById<Toolbar>(R.id.toolbar).apply {
            title = ""
        }
        setSupportActionBar(toolbar)
        getSupportActionBar()?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.dishes_reviews_ic_action_back)
        }
        searchView=findViewById<SearchView>(R.id.sv).apply {
            setIconified(false)
            setOnCloseListener {  true} //禁止关闭搜索框
        }
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

}