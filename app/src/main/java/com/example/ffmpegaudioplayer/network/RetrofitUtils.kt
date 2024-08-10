package com.example.ffmpegaudioplayer.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitUtils {

	private val BASE_URL = "http://www.ruanjiezh.cn:8081/"
	private val TIME_OUT_SEC = 90

	var okHttpClient: OkHttpClient = OkHttpClient.Builder()
		.connectTimeout(30, TimeUnit.SECONDS) // 设置连接超时时间为30秒
		.readTimeout(30, TimeUnit.SECONDS) // 设置读取超时时间为30秒
		.build()


	val instance = Retrofit.Builder()
		.baseUrl(BASE_URL)
		.client(okHttpClient)
		.addConverterFactory(GsonConverterFactory.create())
		.addCallAdapterFactory(CoroutineCallAdapterFactory())
		.build()
}
