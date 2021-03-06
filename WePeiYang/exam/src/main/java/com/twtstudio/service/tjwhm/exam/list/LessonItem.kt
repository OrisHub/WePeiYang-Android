package com.twtstudio.service.tjwhm.exam.list

import android.content.Context
import android.content.Intent
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.twt.wepeiyang.commons.ui.rec.Item
import com.twt.wepeiyang.commons.ui.rec.ItemController
import com.twtstudio.service.tjwhm.exam.R
import com.twtstudio.service.tjwhm.exam.commons.GoneAnimatorListener
import com.twtstudio.service.tjwhm.exam.commons.NoneAnimatorListener
import com.twtstudio.service.tjwhm.exam.problem.ProblemActivity
import org.jetbrains.anko.layoutInflater

class LessonItem(val context: Context, val listActivityInterface: ListActivityInterface, val lessonBean: LessonBean, var isExpand: Boolean) : Item {
    override val controller: ItemController
        get() = Controller

    constructor(lessonItem: LessonItem) : this(lessonItem.context, lessonItem.listActivityInterface, lessonItem.lessonBean, false)

    companion object Controller : ItemController {
        override fun onCreateViewHolder(parent: ViewGroup): RecyclerView.ViewHolder =
                LessonViewHolder(parent.context.layoutInflater.inflate(R.layout.exam_item_list, parent, false))

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: Item) {
            holder as LessonViewHolder
            item as LessonItem
            holder.apply {
                tvEnterContest?.visibility = View.GONE
                tvEnterPractice?.visibility = View.GONE
                tvTitle?.text = item.lessonBean.course_name
                itemView.setOnClickListener {
                    if (item.isExpand) {
                        tvEnterContest?.apply {
                            animate().translationY(-8f)
                                    .alpha(0f)
                                    .setDuration(200)
                                    .setListener(GoneAnimatorListener(this))
                        }
                        tvEnterPractice?.apply {
                            animate().translationY(-8f)
                                    .alpha(0f)
                                    .setListener(GoneAnimatorListener(this))
                        }
                        ivExpend?.animate()?.rotation(0f)
                        item.isExpand = false

                    } else {
                        tvEnterContest?.apply {
                            visibility = View.VISIBLE
                            alpha = 0f
                            animate().translationYBy(8f)
                                    .alpha(1f)
                                    .setDuration(200)
                                    .setListener(NoneAnimatorListener)
                        }
                        tvEnterPractice?.apply {
                            visibility = View.VISIBLE
                            alpha = 0f
                            animate().translationYBy(8f)
                                    .alpha(1f)
                                    .setDuration(200)
                                    .setListener(NoneAnimatorListener)
                        }
                        ivExpend?.animate()
                                ?.rotation(90.0f)
                        item.isExpand = true
                    }
                }

                val intent = Intent(item.context, ProblemActivity::class.java)
                intent.putExtra(ProblemActivity.LESSON_ID_KEY, item.lessonBean.course_id)
                tvEnterPractice?.setOnClickListener {
                    val popup = TypeSelectPopup(item.context, item.listActivityInterface, Pair(tvEnterPractice.x, tvEnterPractice.y), item.lessonBean.course_id, false)
                    popup.show()
                }

                tvEnterContest?.setOnClickListener {
                    intent.putExtra(ProblemActivity.MODE_KEY, ProblemActivity.CONTEST)
                    item.context.startActivity(intent)
                    item.listActivityInterface.initList()
                }
            }
        }
    }

    private class LessonViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView? = itemView?.findViewById(R.id.tv_list_lesson_title)
        val ivExpend: ImageView? = itemView?.findViewById(R.id.iv_list_expend)
        val tvEnterContest: TextView? = itemView?.findViewById(R.id.tv_enter_contest)
        val tvEnterPractice: TextView? = itemView?.findViewById(R.id.tv_enter_practice)
    }
}

fun MutableList<Item>.lessonItem(context: Context, listActivityInterface: ListActivityInterface, lessonBean: LessonBean, isExpand: Boolean) = add(LessonItem(context, listActivityInterface, lessonBean, isExpand))
