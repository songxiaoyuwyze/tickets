package com.example.ffmpegaudioplayer.adapter

import android.graphics.Color
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ffmpegaudioplayer.R
import com.example.ffmpegaudioplayer.bean.ReservationData
import com.example.ffmpegaudioplayer.bean.ReservationItem

class ReservationAdapter(private val data: ReservationData) : RecyclerView.Adapter<ReservationAdapter.ReservationViewHolder>() {

	var currentPosition = -1
	val posSet = hashSetOf<Int>()
	var listener:ItemClickListener? = null
	init {
	    val count = (data.spaceArray.size+1) * (data.timeArray.size+1)
	}
	fun setOnclickListener(l:ItemClickListener){
		listener = l
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
		val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
		val reservationViewHolder = ReservationViewHolder(view)
		view.setOnClickListener {
			currentPosition = reservationViewHolder.adapterPosition
			posSet.add(getRealPosition(currentPosition,data.spaceArray.size))
			listener?.onClick(reservationViewHolder.adapterPosition)
		}
		return reservationViewHolder
	}

	override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
		if (position == 0){
			holder.setRowMessage("时间/场次")
		}else if (position <= data.spaceArray.size){
			holder.setRowMessage(data.spaceArray[position-1])
		}else if ((position) % (data.spaceArray.size+1) == 0 && ((position) / (data.spaceArray.size+1) <= data.timeArray.size)){
			val num = (((position) / (data.spaceArray.size+1))-1)
			holder.setRowMessage(data.timeArray[num].startTime.takeLast(8))
		}else{
			val realPositon = getRealPosition(position,data.spaceArray.size)
			Log.i("1111",realPositon.toString())
			holder.setMessage(data.items[realPositon],posSet.contains(realPositon))
		}

	}

	override fun getItemCount(): Int {
		return (data.spaceArray.size+1) * (data.timeArray.size+1)
	}

	class ReservationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		val text = itemView.findViewById<TextView>(R.id.textView)
		fun bind(rowData: List<String>) {
			// 绑定数据到视图
			// rowData 包含该行的数据，根据需求填充到相应的视图组件中
		}
		fun setRowMessage(s:String){
			text.text = s
			text.setTextColor(Color.WHITE)
			text.setBackgroundColor(Color.GRAY)
			itemView.isEnabled = false
			itemView.isClickable = false
		}
		fun setColumn(s:String){

		}
		fun hasReservationId(has:Boolean){
			if (has){
				text.setTextColor(Color.GRAY)
				text.setBackgroundColor(Color.WHITE)
				itemView.isEnabled = true
				itemView.isClickable = true
			}else{
				text.setTextColor(Color.WHITE)
				text.setBackgroundColor(Color.GRAY)
				itemView.isEnabled = false
				itemView.isClickable = false
			}
		}

		fun setMessage(item: ReservationItem,isSelected: Boolean) {
			text.text = item.money.toString()
			hasReservationId(item.reservationStatus == 1)
			if(isSelected){
				text.setBackgroundColor(Color.GREEN)
			}
		}
	}


}

interface ItemClickListener{
	fun onClick(pos:Int)
}
fun getRealPosition(allPosition :Int,rowCount:Int):Int{
	return allPosition - ((allPosition) / (rowCount+1)) - 5
}
