package com.yookiely.lostfond2.search

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.widget.*
import android.support.v7.widget.Toolbar
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.lostfond2.R
import com.orhanobut.hawk.Hawk
import com.twt.wepeiyang.commons.mta.mtaClick
import com.yookiely.lostfond2.service.Utils
import com.yookiely.lostfond2.waterfall.WaterfallPagerAdapter

class SearchActivity : AppCompatActivity() {

    private var inputMethodManager: InputMethodManager? = null
    private lateinit var toolbar: Toolbar
    private lateinit var lostFragment: SearchFragment
    private lateinit var foundFragment: SearchFragment
    private lateinit var searchPager: ViewPager
    private lateinit var chooseTimeRecyclerView: RecyclerView//弹窗，选择时间的rv
    lateinit var chooseTimePopupWindow: PopupWindow
    private var layoutManagerForChooseTime = LinearLayoutManager(this@SearchActivity)
    private lateinit var searchTableLayout: TabLayout
    private lateinit var imageViewGrey: ImageView
    private lateinit var imageViewBlue: ImageView
    private lateinit var searchType: RelativeLayout
    private lateinit var textView: TextView

    lateinit var keyword: String private set
    var page = 1
    var campus: Int = 1
    private var time = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)//隐藏actionbar，需在setContentView前面
        setContentView(R.layout.lf2_activity_search)

        window.statusBarColor = resources.getColor(R.color.statusBarColor)
        toolbar = findViewById(R.id.tb_search)
        textView = findViewById(R.id.tv_search_result)
        searchTableLayout = findViewById(R.id.tl_search)
        imageViewBlue = findViewById(R.id.iv_search_type_blue)
        imageViewGrey = findViewById(R.id.iv_search_type_grey)
        searchType = findViewById(R.id.tl_search_type)
        lostFragment = SearchFragment.newInstance("lost")
        foundFragment = SearchFragment.newInstance("found")
        campus = Utils.campus ?: 1//1 北洋园 ，2 卫津路

        val waterfallPagerAdapter = WaterfallPagerAdapter(supportFragmentManager)
        val popupWindowView = LayoutInflater.from(this).inflate(R.layout.lf2_popupwindow_search, null, false)
        waterfallPagerAdapter.add(foundFragment, "捡到")
        waterfallPagerAdapter.add(lostFragment, "丢失")
        searchPager = findViewById(R.id.vp_search_pager_content)
        searchPager.adapter = waterfallPagerAdapter
        searchTableLayout.apply {
            setupWithViewPager(searchPager)
            tabGravity = TabLayout.GRAVITY_FILL
            setSelectedTabIndicatorColor(Color.parseColor("#00a1e9"))
        }
        imageViewBlue.visibility = View.GONE
        toolbar.apply {
            setSupportActionBar(this)
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            setNavigationOnClickListener { onBackPressed() }
        }

        //将上一个activity的数据取出来用
        val bundle = intent.extras
        keyword = bundle.getString(Utils.QUERY_KEY)
        textView.text = keyword
        hideInputKeyboard()
        onDetachedFromWindow()

        textView.setOnClickListener {
            hideInputKeyboard()
            val intent = Intent()
            intent.setClass(this@SearchActivity, SearchInitActivity::class.java)
            startActivity(intent)
        }

        searchType.setOnClickListener {
            mtaClick("lostfound2_搜索 点击筛选按钮的次数")
            if (imageViewGrey.visibility == View.VISIBLE) run {
                imageViewBlue.visibility = View.VISIBLE
                imageViewGrey.visibility = View.GONE

                chooseTimePopupWindow = PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
                chooseTimePopupWindow.apply {
                    setBackgroundDrawable(ColorDrawable(ContextCompat.getColor(this@SearchActivity, R.color.white_color)))
                    isOutsideTouchable = true
                    isTouchable = true
                    showAsDropDown(it)
                    setTouchInterceptor { _, event ->
                        if (event?.action == MotionEvent.ACTION_DOWN) {
                            imageViewGrey.visibility = View.VISIBLE
                            imageViewBlue.visibility = View.GONE
                        }
                        false
                    }
                }

                chooseTimeRecyclerView = popupWindowView.findViewById(R.id.rv_search_type)
                chooseTimeRecyclerView.apply {
                    adapter = SearchChooseTimeAdapter(this@SearchActivity, time, chooseTimePopupWindow)
                    layoutManager = layoutManagerForChooseTime
                }

            } else if (imageViewBlue.visibility == View.VISIBLE) {
                imageViewGrey.visibility = View.VISIBLE
                imageViewBlue.visibility = View.GONE
            }
        }
    }

    private fun hideInputKeyboard() {
        inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        if (inputMethodManager != null) {
            val v = this@SearchActivity.currentFocus ?: return
            inputMethodManager!!.hideSoftInputFromWindow(v.windowToken,
                    InputMethodManager.HIDE_NOT_ALWAYS)
            //searchView.clearFocus()
        }
    }

    fun changeTime(time: Int) {
        this.time = time
        lostFragment.setTimeAndLoad(this.time)
        foundFragment.setTimeAndLoad(this.time)
    }
}