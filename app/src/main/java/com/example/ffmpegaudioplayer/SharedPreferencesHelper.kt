package com.example.ffmpegaudioplayer

import android.content.Context

object SharedPreferencesHelper {
	private const val PREF_FILE_NAME = "MyPrefsFile"
	const val OPEN_ID = "open_id"
	const val NAME = "name"
	const val PHONE_NUM = "phone_num"



	// 保存 String 类型的值到 SharedPreferences
	fun saveString(context: Context, key: String, value: String) {
		val sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
		val editor = sharedPreferences.edit()
		editor.putString(key, value)
		editor.apply()
	}

	// 从 SharedPreferences 中获取 String 类型的值，如果不存在则返回默认值
	fun getString(context: Context, key: String, defaultValue: String): String {
		val sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, Context.MODE_PRIVATE)
		return sharedPreferences.getString(key, defaultValue) ?: defaultValue
	}
}


