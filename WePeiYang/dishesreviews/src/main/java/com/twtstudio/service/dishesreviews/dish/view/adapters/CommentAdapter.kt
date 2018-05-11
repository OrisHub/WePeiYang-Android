package com.twtstudio.service.dishesreviews.dish.view.adapters

import android.arch.lifecycle.LifecycleOwner
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.twtstudio.service.dishesreviews.R
import com.twtstudio.service.dishesreviews.base.BaseListAdapter
import com.twtstudio.service.dishesreviews.dish.view.viewholders.CommentViewHolder

class CommentAdapter(list: List<Any>, context: Context, owner: LifecycleOwner) : BaseListAdapter(list, context, owner) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return CommentViewHolder(inflater.inflate(R.layout.dishes_reviews_item_dish_comment, parent, false), owner)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }
}