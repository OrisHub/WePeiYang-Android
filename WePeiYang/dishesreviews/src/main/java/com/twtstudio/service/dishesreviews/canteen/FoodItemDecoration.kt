package com.twtstudio.service.dishesreviews.canteen

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.TextPaint
import android.view.View


/**
 * Created by SGXM on 2018/5/11.
 */
class FoodItemDecoration(val context: Context, var mDatas: MutableList<CanteenBean>, val numOfType: MutableList<Pair<Int, Int>>) : RecyclerView.ItemDecoration() {
    var onDrawOverLastTitle = "#"
    var leftAdapter: LeftAdapter? = null
    var mTitlePaddingLeft = 36
    var mTitleHeight = 100
    val mPaint = Paint().apply {
        color = Color.WHITE
    }
    val mTextPaint = TextPaint().apply {
        color = Color.BLACK
        textSize = 36f
        isAntiAlias = true
    }

    override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
        super.getItemOffsets(outRect, view, parent, state)
        val pos = (view?.layoutParams as RecyclerView.LayoutParams).viewLayoutPosition
        if (pos > -1) {//我记得Rv的item position在重置时可能为-1.保险点判断一下吧???或许吧
            if (pos == 0) {//等于0肯定要有title的
                outRect?.set(0, mTitleHeight, 0, 0)
            } else {//其他的通过判断
                if ("#" != mDatas.get(pos).tag && !mDatas.get(pos).tag.equals(mDatas.get(pos - 1).tag)) {
                    outRect?.set(0, mTitleHeight, 0, 0)//不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                } else {
                    outRect?.set(0, 0, 0, 0)
                }
            }
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        val childCount = parent.childCount//只会绘制当前屏幕内显示的item，count为4
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val position = params.viewLayoutPosition
            //我记得Rv的item position在重置时可能为-1.保险点判断一下吧
            if (position > -1) {
                //将需要添加title的item对应向下平移title的高度，留出绘制title的地方
                if (position == 0) {//等于0肯定要有title的
                    drawTextArea(c, left.toFloat(), right.toFloat(), child, params, position)
                } else {//其他的通过判断
                    if (!mDatas[position].tag.equals(mDatas[position - 1].tag)) {
                        //不为空 且跟前一个tag不一样了，说明是新的分类，也要title
                        drawTextArea(c, left.toFloat(), right.toFloat(), child, params, position)
                    } else {
                        //none
                    }
                }
            }
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State?) {
        super.onDrawOver(c, parent, state)
        val pos = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (pos < 0) return
        val tag = mDatas[pos].tag
//        val view = parent.findViewHolderForLayoutPosition(pos).itemView
        if (onDrawOverLastTitle != tag) {
            var num = 0
            numOfType.withIndex().forEach {
                if (pos >= it.value.second) num = it.index
            }
            leftAdapter?.selectItem(num)
        }
        c.drawRect(parent.paddingLeft * 1f, parent.paddingTop * 1f, parent.right * 1f, parent.top + mTitleHeight * 1f, mPaint)
        c.drawText(tag, parent.paddingLeft + mTitlePaddingLeft * 1f, parent.paddingTop + mTitleHeight * 0.65f, mTextPaint)
        onDrawOverLastTitle = tag
    }

    private fun drawTextArea(canvas: Canvas, left: Float, right: Float, view: View, recLayoutParams: RecyclerView.LayoutParams, pos: Int) {
        canvas.drawRect(left, 1f * (view.top - recLayoutParams.topMargin - mTitleHeight), right, 1f * (view.top - recLayoutParams.topMargin), mPaint)
        canvas.drawText(mDatas[pos].tag, view.paddingLeft + mTitlePaddingLeft * 1f, view.top - mTitleHeight * 0.4f, mTextPaint)
    }
}