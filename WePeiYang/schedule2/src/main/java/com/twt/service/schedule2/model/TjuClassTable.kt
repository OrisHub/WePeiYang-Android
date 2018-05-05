package com.twt.service.schedule2.model

import com.twt.service.schedule2.extensions.currentUnixTime
import com.twt.service.schedule2.extensions.getDayOfWeek
import com.twt.service.schedule2.extensions.getRealWeekInt
import com.twt.service.schedule2.extensions.trim

/**
 * Created by retrox on 2018/3/26.
 */
class TjuClassTable(val classtable: Classtable) : AbsClasstableProvider {

    val termStart: Long = classtable.termStart
    val courses: List<Course> = classtable.courses

    override fun getCourseByDay(unixTime: Long): List<Course> {
        val dayOfWeek = getDayOfWeek(termStart, unixTime)
        val week = getWeekByTime(unixTime)
        val coursesLocal = getCourseByWeekWithTime(unixTime)
        val list = coursesLocal.onEach {
            it.arrange.trim(dayOfWeek)
        }.onEach {
            it.dayAvailable = it.isContainDay(dayOfWeek)
            it.weekAvailable = it.isWeekAvailable(weekInt = week)
        }.filter { it.dayAvailable }

        return list
    }

    override fun getCourseNextDay(unixTime: Long): List<Course> {
        TODO()
    }

    override fun getTodayCourse(): List<Course> {
        TODO()
    }

    override fun getTomorrowCourse(): List<Course> {
        TODO()
    }

    override fun getCourseByTime(unixTime: Long): Course? {
        TODO()
    }

    override fun getNextCourseByTime(unixTime: Long): Course {
        TODO()
    }

    override fun checkCourseConflict(course: Course): Course? {
        TODO()
    }

    override fun getCourseByWeek(week: Int): List<Course> {
        return courses.onEach { it.refresh() } // 去除Arrange被Trim的情况
    }

    override fun getCourseByWeekWithTime(unixTime: Long): List<Course> {
        val week = getWeekByTime(unixTime)
        return getCourseByWeek(week)
    }

    override fun getWeekByTime(unixTime: Long): Int = getRealWeekInt(termStart, unixTime)

    override fun getCurrentWeek(): Int = getWeekByTime(currentUnixTime)

}