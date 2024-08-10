package com.example.ffmpegaudioplayer

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

object DateUtils {

	val dateArray = initData()

	private fun initData():MutableList<String>{
		val list = mutableListOf<String>()
		val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

		val calendar = Calendar.getInstance()
		calendar.firstDayOfWeek = Calendar.MONDAY // 设置一周的第一天为星期一

		val today = calendar.get(Calendar.DAY_OF_WEEK)
		calendar.add(Calendar.DAY_OF_YEAR, Calendar.MONDAY - today) // 获取本周的第一天

		for (i in 0 until 7) {
			val date = calendar.time
			val formattedDate = dateFormat.format(date)
			list.add(formattedDate)
			calendar.add(Calendar.DAY_OF_YEAR, 1) // 递增一天
		}
		return list
	}
}
