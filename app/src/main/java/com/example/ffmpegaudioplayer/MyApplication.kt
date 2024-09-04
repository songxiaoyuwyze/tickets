package com.example.ffmpegaudioplayer

import android.app.Application
import com.example.ffmpegaudioplayer.utils.AppConfig

class MyApplication:Application() {

	override fun onCreate() {
		super.onCreate()
		AppConfig.initialize(this)
	}
}
