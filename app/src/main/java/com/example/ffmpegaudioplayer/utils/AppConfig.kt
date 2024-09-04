package com.example.ffmpegaudioplayer.utils

import android.content.Context
import com.example.ffmpegaudioplayer.bean.UserInfo
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

object AppConfig {

	val userList = mutableListOf<UserInfo>()
	private lateinit var appContext: Context

	fun initialize(context: Context) {
		appContext = context.applicationContext
		val fromJson = Gson().fromJson<MutableList<UserInfo>>(
			getFromAssets(appContext, "default_user_info.json"),
			object : TypeToken<MutableList<UserInfo>>() {}.type
		)
		userList.addAll(fromJson)
	}




	private fun getFromAssets(context: Context,fileName: String): String {
		return try {
			val inputStream: InputStream = context.assets.open(fileName)
			val size: Int = inputStream.available()
			val buffer = ByteArray(size)
			inputStream.read(buffer)
			inputStream.close()
			String(buffer, Charset.defaultCharset())
		} catch (e: IOException) {
			e.printStackTrace()
			""
		}
	}

}
