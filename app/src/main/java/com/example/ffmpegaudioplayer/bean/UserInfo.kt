package com.example.ffmpegaudioplayer.bean

import android.os.Parcelable
import com.google.android.material.search.SearchView.Behavior
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserInfo(var openId:String = "oIVg65tbT-9AL0oCn9HPQ3OohsC0",var userName:String = "宋小宇",var userPhone:String = "18514430063",var hasVanue:Boolean = false): Parcelable
