package com.lana.cc.device.user.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

fun long2DateString(timeStamp: Long): String {
    val format = SimpleDateFormat("yyyy年MM月dd日")
    val date = Date(timeStamp)
    return format.format(date)
}

fun long2MinTimeString(timeStamp: Long): String {
    val format = SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss")
    val date = Date(timeStamp)
    return format.format(date)
}

@BindingAdapter("time")
fun long2Date(textView: TextView, birthLong: Long) {
    textView.text = long2DateString(birthLong)
}

@BindingAdapter("minTime")
fun long2Min(textView: TextView, birthLong: Long) {
    textView.text = long2MinTimeString(birthLong)
}


fun string2Date(string: String): Date {
    val format = SimpleDateFormat("yyyy年MM月dd日")
    var date = Date()
    try {
        date = format.parse(string)!!
    } catch (e: Exception) {
        Timber.d("转换日期失败：${e.message}")
    }
    return date
}

fun getNowString(): String {
    val formatter = SimpleDateFormat("yyyy年MM月dd日   HH:mm:ss")
    val curDate = Date(System.currentTimeMillis())
    //获取当前时间
    return formatter.format(curDate)
}

fun getAgeByLong(time:Long):Int =
    getAgeByBirth(Date(time))


fun getAgeByBirth(birthDay: Date): Int {
    var age: Int
    val cal = Calendar.getInstance()
    if (cal.before(birthDay)) { //出生日期晚于当前时间，无法计算
        throw  IllegalArgumentException(
            "The birthDay is before Now.It's unbelievable!"
        )
    }
    val yearNow = cal.get(Calendar.YEAR)  //当前年份
    val monthNow = cal.get(Calendar.MONTH)  //当前月份
    val dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH) //当前日期
    cal.time = birthDay
    val yearBirth = cal.get(Calendar.YEAR)
    val monthBirth = cal.get(Calendar.MONTH)
    val dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH)
    age = yearNow - yearBirth   //计算整岁数
    if (monthNow <= monthBirth) {
        if (monthNow == monthBirth) {
            if (dayOfMonthNow < dayOfMonthBirth) age--//当前日期在生日之前，年龄减一
        } else {
            age-- //当前月份在生日之前，年龄减一
        }
    }
    return age
}

