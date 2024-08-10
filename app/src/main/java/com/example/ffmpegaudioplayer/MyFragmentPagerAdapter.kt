package com.example.ffmpegaudioplayer


import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx. viewpager2.adapter. FragmentStateAdapter
import com.example.ffmpegaudioplayer.fragment.DayOfFragment


class MyFragmentPagerAdapter(fragmentActivity: FragmentActivity, var title:List<String>) :FragmentStateAdapter(fragmentActivity) {
	override fun getItemCount() = title.size
	override fun createFragment(position: Int): Fragment {
			return DayOfFragment.newInstance(position)
	}

}
