package com.example.ffmpegaudioplayer.fragment

import android.content.Context
import android.os.Binder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ffmpegaudioplayer.DateUtils
import com.example.ffmpegaudioplayer.R
import com.example.ffmpegaudioplayer.SharedPreferencesHelper
import com.example.ffmpegaudioplayer.SharedPreferencesHelper.NAME
import com.example.ffmpegaudioplayer.SharedPreferencesHelper.OPEN_ID
import com.example.ffmpegaudioplayer.SharedPreferencesHelper.PHONE_NUM
import com.example.ffmpegaudioplayer.adapter.ItemClickListener
import com.example.ffmpegaudioplayer.adapter.ReservationAdapter
import com.example.ffmpegaudioplayer.adapter.getRealPosition
import com.example.ffmpegaudioplayer.bean.OrderRequest
import com.example.ffmpegaudioplayer.bean.ReservationData
import com.example.ffmpegaudioplayer.bean.ReservationItem
import com.example.ffmpegaudioplayer.bean.ReservationResponse
import com.example.ffmpegaudioplayer.databinding.FragmentDayBinding
import com.example.ffmpegaudioplayer.network.ApiService
import com.example.ffmpegaudioplayer.network.RetrofitUtils
import com.example.ffmpegaudioplayer.utils.AppConfig
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DayOfFragment(val offsetDay: Int) : Fragment() {

	lateinit var mViewBinding: FragmentDayBinding
	val TAG = "DayOfFragment"

	var currentpos = -1
	var posSet = hashSetOf<Int>()
	//{
	//	"openId": "oIVg65odcD8DsN5wfSFsho7s33aI",
	//	"userName": "谢大帅",
	//	"userPhone": "17710910821"
	//}
	//{
	//	"openId": "oIVg65iUSLDKEPhcllKJV58lY-D0",
	//	"reservationId": "1274772064187719714",
	//	"userName": "吴京",
	//	"userPhone": "15832003959"
	//}
	var openId = ""
	var name = ""
	var phoneNumber = ""
	val apiService = RetrofitUtils.instance.create(ApiService::class.java)
	var response : ReservationResponse? =null
	override fun onCreateView(
		inflater: LayoutInflater,
		container: ViewGroup?,
		savedInstanceState: Bundle?
	): View? {
		mViewBinding = FragmentDayBinding.inflate(inflater, container, false)
		return mViewBinding.root
	}




	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

		super.onViewCreated(view, savedInstanceState)

		initData()
		lifecycleScope.launch {
			try {
				response =
					apiService.getData("api/mobile/reservation/tag/" + DateUtils.dateArray[offsetDay])
				// 在这里处理响应数据
				Log.i(TAG, response.toString())
				val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
				response?.let {
					recyclerView.layoutManager =
						GridLayoutManager(context, it.data.spaceArray.size + 1)
					val adapter = ReservationAdapter(it.data)
					recyclerView.adapter = adapter
					adapter.setOnclickListener(object : ItemClickListener {
						override fun onClick(pos: Int) {
							adapter.notifyDataSetChanged()
							currentpos = getRealPosition(pos,it.data.spaceArray.size)
							posSet.add(currentpos)
//							it.data.items[currentpos]
							lifecycleScope.launch {
								response?.let {resp->
									if (currentpos == -1) return@launch
									val ids = mutableListOf<ReservationItem>()
									ids.add(resp.data.items[currentpos])
									createOrder(ids)
								}
							}
						}
					})

				}

			} catch (e: Exception) {
				e.message?.let {
					Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
				}
			}
		}

		initClickListener()
	}

	private fun initData() {
		 openId = SharedPreferencesHelper.getString(
			requireContext(),
			OPEN_ID,
			"oIVg65tbT-9AL0oCn9HPQ3OohsC0"
		)
		 name = SharedPreferencesHelper.getString(requireContext(), NAME, "宋")
		 phoneNumber =
			SharedPreferencesHelper.getString(requireContext(), PHONE_NUM, "18514430063")
	}

	private fun initClickListener() {
		mViewBinding.btn1.setOnClickListener {
			lifecycleScope.launch {
				response?.let {
					val take = it.data.items.take(4)
					createOrder(take)
				}
			}
		}
		mViewBinding.btn2.setOnClickListener {
			lifecycleScope.launch {
				response?.let {
					val take = it.data.items.take(4)
					createOrder(take)
				}
			}
		}
		mViewBinding.btn3.setOnClickListener {
			lifecycleScope.launch {
				response?.let {
					val take = it.data.items.slice(52..55)
					createOrder(take)
				}
			}
		}
		mViewBinding.btn4.setOnClickListener {
			lifecycleScope.launch {
				response?.let {resp->
					if (currentpos == -1) return@launch
					val ids = mutableListOf<ReservationItem>()
					posSet.forEach {
						ids.add(resp.data.items[it])
					}
					createOrder(ids)
				}
			}
		}

		mViewBinding.btnSetting.setOnClickListener {
			showCustomDialog(requireContext())
		}
	}

	fun showloading(){
		mViewBinding.rlLoading.visibility = View.VISIBLE
	}

	fun hideloading(){
		mViewBinding.rlLoading.visibility = View.GONE

	}


	fun showCustomDialog(context: Context) {
		val dialogBuilder = AlertDialog.Builder(context)
		val dialog = dialogBuilder.create()
		val view = layoutInflater.inflate(R.layout.dialog_setting, mViewBinding.root, false)
		dialog.setView(view)
		dialog.show()
		val etId = view.findViewById<EditText>(R.id.et_id)
		val etName = view.findViewById<EditText>(R.id.et_name)
		val etPhone = view.findViewById<EditText>(R.id.et_phone)
		val btnCancel = view.findViewById<Button>(R.id.btn_cancel)
		val btnOk = view.findViewById<Button>(R.id.btn_confirm)

		// 从 SharedPreferences 中获取值

		etId.setText(openId)
		etName.setText(name)
		etPhone.setText(phoneNumber)
		btnOk.setOnClickListener {
			// 使用示例
			// 保存值到 SharedPreferences
			openId = etId.text.toString()
			name = etName.text.toString()
			phoneNumber = etPhone.text.toString()
			SharedPreferencesHelper.saveString(context, OPEN_ID, openId)
			SharedPreferencesHelper.saveString(context, NAME, name)
			SharedPreferencesHelper.saveString(context, PHONE_NUM, phoneNumber)
			dialog.dismiss()
		}
		btnCancel.setOnClickListener {
			dialog.dismiss()
		}
	}
	private suspend fun createOrder(data:List<ReservationItem>) {
		try {
				withContext(Dispatchers.IO){
					data.forEach {d ->
						if (d.reservationStatus == 1){
							AppConfig.userList.forEach { user->
								val task = async {  apiService.createOrder(
									OrderRequest(
										openId = user.openId,
										reservationId = d.reservationId,
										userName = user.userName,
										userPhone = user.userPhone
									)
								) }
								task.await()
//								if (result.status){
//									AppConfig.userList.forEach {
//										if (it.openId == result.data.prepayData.appId){
//											it.hasVanue = true
//											this.cancel()
//										}
//									}
//								}
							}
						}
					}
			}


			// 处理响应数据
		} catch (e: Exception) {
			e.message?.let {
				Log.i("1111",it)
			}
			// 处理异常

		}

	}

	companion object {
		@JvmStatic
		fun newInstance(offsetDay: Int) = DayOfFragment(offsetDay)
	}


}
