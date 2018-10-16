package com.twtstudio.retrox.tjulibrary.view

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Window
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.twt.wepeiyang.commons.experimental.extensions.QuietCoroutineExceptionHandler
import com.twt.wepeiyang.commons.experimental.extensions.fitSystemWindowWithStatusBar
import com.twtstudio.retrox.tjulibrary.R
import com.twtstudio.retrox.tjulibrary.tjulibservice.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

class DetailActivity : AppCompatActivity() {


    lateinit var toolbar: Toolbar
    lateinit var recyclerView: RecyclerView
    lateinit var adapter :DetailConditionAdapter
    lateinit var book_name : TextView
    lateinit var book_author : TextView
    lateinit var book_content : TextView
    lateinit var book_publish_date : TextView
    lateinit var book_publisher : TextView
    lateinit var book_pic : ImageView
    lateinit var total_borrow_num : TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_detail)
        toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbar).also {
            title = "借阅数据"
            it.setBackgroundColor(Color.parseColor("#e78fae"))

            setSupportActionBar(it)
        }

        window.statusBarColor = Color.parseColor("#e78fae")

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar.setNavigationOnClickListener { onBackPressed() }

        val bundle = intent.extras
        val id = bundle.getString("id")

        book_name = findViewById(R.id.book_name)
        book_author = findViewById(R.id.book_author)
        book_content = findViewById(R.id.book_content)
        book_publish_date = findViewById(R.id.book_publish_date)
        book_publisher = findViewById(R.id.book_publisher)
        book_pic = findViewById(R.id.book_pic)
        recyclerView = findViewById(R.id.book_recyclerview)
        total_borrow_num = findViewById(R.id.total_borrow_num)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        launch(UI + QuietCoroutineExceptionHandler) {
            val book = LibraryApi.getBook(id).await()
            val totalNum = LibraryApi.getTotalNum(id).await()
            Log.d("whatthefuck",totalNum.toString())
            try {
                if (book.data.summary != null){
                    if(book.data.summary.length>151){
                        val x = book.data.summary.substring(0,151)+"..."
                        book_content.text = x
                    }else{
                        book_content.text = book.data.summary
                    }
                }


                val url  = ImgApi.getImgUrl(book.data.isbn).await()
                Glide.with(this@DetailActivity)
                        .load(url[0].result[0].coverlink)
                        .asBitmap()
                        .placeholder(R.drawable.src)
                        .error(R.drawable.src2)
                        .into(book_pic)
                Log.d("julao",url.toString())
            }catch (e : Exception){
                Log.d("julao",e.toString())
            }




            setDetial(book.data)
            total_borrow_num.text = totalNum.totalBorrowNum.toString()

        }

    }

    fun setDetial (data : Datax) {
        book_name.text = data.title
        var author = ""

        for (i in 0..((data.authorPrimary).size - 1)) {
            author += data.authorPrimary[i]
            if (i != (data.authorPrimary.size - 1)) author += "，"
        }

        book_author.text = author
        book_publisher.text = data.publisher
        book_publish_date.text = data.year
        adapter = DetailConditionAdapter(data.holding, this)
        recyclerView.adapter = adapter
    }
}
