package com.example.ffmpegaudioplayer.bean

data class ReservationResponse(
	val status: Boolean,
	val data: ReservationData
)

data class ReservationData(
	val date: String,
	val week: String,
	val status: Int,
	val items: List<ReservationItem>,
	val spaceArray: List<String>,
	val timeArray: List<TimeSlot>
)

data class ReservationItem(
	val reservationId: String,
	val reservationDate: String,
	val spaceType: Int,
	val spaceId: String,
	val spaceName: String,
	val startTime: String,
	val endTime: String,
	val reservationStatus: Int,
	val isBooked: Boolean,
	val money: Int
)

data class TimeSlot(
	val startTime: String,
	val endTime: String,
	val startTimeStr: String,
	val endTimeStr: String
)

