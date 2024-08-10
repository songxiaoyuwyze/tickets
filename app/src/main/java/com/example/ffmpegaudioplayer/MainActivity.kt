package com.example.ffmpegaudioplayer

import android.os.Bundle
import android.os.Environment
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.ffmpegaudioplayer.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainActivity : AppCompatActivity() {



	private val TAG = "MainActivity"
	lateinit var mViewBinding : ActivityMainBinding
	lateinit var fragmentAdapter: MyFragmentPagerAdapter
	val titleList = listOf("周一","周二","周三","周四","周五","周六","周日",)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		mViewBinding = ActivityMainBinding.inflate(layoutInflater)




		enableEdgeToEdge()
		setContentView(mViewBinding.root)
		fragmentAdapter = MyFragmentPagerAdapter(this,titleList)
		mViewBinding.pager.adapter = fragmentAdapter
		TabLayoutMediator(mViewBinding.tebLayout,mViewBinding.pager){tab,position ->
			tab.text = titleList[position]
		}.attach()
		ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
			val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
			v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
			insets
		}



	}



	fun decodeAudio(){
		val src = "${Environment.getExternalStorageDirectory()}/yumu.mp3"
		val out = "${Environment.getExternalStorageDirectory()}/out.pcm"
		decodeAudio(src, out)
	}

	external fun decodeAudio(src:String,out:String)

	companion object {
		init {
			System.loadLibrary("swresample")
			System.loadLibrary("avutil")
			System.loadLibrary("avcodec")
			System.loadLibrary("avfilter")
			System.loadLibrary("swscale")
			System.loadLibrary("avformat")
			System.loadLibrary("ffmpegaudioplayer")
		}
	}

}
