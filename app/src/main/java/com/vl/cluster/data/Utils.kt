package com.vl.cluster.data

import android.content.Context
import com.vl.cluster.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

private val DEFAULT_LOCALE: Locale get() = Locale.getDefault()

fun getDatetime(context: Context, unixSec: Long): String = Calendar.getInstance(DEFAULT_LOCALE).run {
    val now = Calendar.getInstance(DEFAULT_LOCALE)
    fun isToday() = get(Calendar.YEAR) == now.get(Calendar.YEAR)
            && get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
    fun isYesterday() = get(Calendar.YEAR) == now.get(Calendar.YEAR) // same year
            && get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR) - 1 // and previous day
            || get(Calendar.YEAR) == now.get(Calendar.YEAR) - 1 // or it was last day of the previous year
            && get(Calendar.DAY_OF_YEAR) == getActualMaximum(Calendar.DAY_OF_YEAR)
            && now.get(Calendar.DAY_OF_YEAR) == now.getActualMinimum(Calendar.DAY_OF_YEAR) // and now it's first day of year
    fun thisYear() = get(Calendar.YEAR) == now.get(Calendar.YEAR)

    timeInMillis = unixSec * 1000
    SimpleDateFormat(when {
        isToday() -> "H:mm"
        isYesterday() -> "${context.getString(R.string.yesterday)} H:mm"
        thisYear() -> "d MMM ${context.getString(R.string.at_o_clock)} H:mm"
        else -> "d MMM y ${context.getString(R.string.at_o_clock)} H:mm"
    }, DEFAULT_LOCALE).format(time)
}