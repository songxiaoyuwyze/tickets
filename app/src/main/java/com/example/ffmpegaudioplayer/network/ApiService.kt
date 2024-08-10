package com.example.ffmpegaudioplayer.network

import CreateOrderResponse
import com.example.ffmpegaudioplayer.bean.OrderRequest
import com.example.ffmpegaudioplayer.bean.ReservationResponse
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Url


interface ApiService {

	@GET
	@Headers(
		"Host: www.ruanjiezh.cn:8081",
		"Accept: application/json, text/plain, */*",
		"User-Agent: Mozilla/5.0 (Linux; Android 14; SM-S9180 Build/UP1A.231005.007; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/126.0.6478.188 Mobile Safari/537.36 XWEB/1260079 MMWEBSDK/20240501 MMWEBID/1661 MicroMessenger/8.0.50.2701(0x28003256) WeChat/arm64 Weixin NetType/WIFI Language/zh_CN ABI/arm64",
		"Origin: http://www.ruanjiezh.cn",
		"X-Requested-With: com.tencent.mm",
		"Referer: http://www.ruanjiezh.cn/",
		"Accept-Language: zh-CN,zh;q=0.9,en-US;q=0.8,en;q=0.7"
	)
	suspend  fun getData(@Url url: String): ReservationResponse


	@POST("api/mobile/order/create")
	suspend fun createOrder(@Body orderRequest: OrderRequest): CreateOrderResponse
}
