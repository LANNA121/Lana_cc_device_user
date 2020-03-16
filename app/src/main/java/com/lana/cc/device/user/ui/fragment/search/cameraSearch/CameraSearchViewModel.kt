package com.lana.cc.device.user.ui.fragment.search.cameraSearch

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.BaiDuIdentifyService
import com.lana.cc.device.user.model.api.baiduidentify.Thing
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.util.switchThread
import org.kodein.di.generic.instance


class CameraSearchViewModel(application: Application) : BaseViewModel(application) {
	
	val thingList = MutableLiveData(mutableListOf<Thing>())
	
	private val baiduIdentifyService by instance<BaiDuIdentifyService>()
	
	//识别图片物体
	fun identifyThingName(imgArrayStr: String) {
		baiduIdentifyService.getToken()
			.flatMap {
				baiduIdentifyService.getIdentifyThing(
					it.accessToken,
					imgArrayStr
				)
			}
			.switchThread()
			.catchApiError()
			.doOnSuccess {
				it.result.sortByDescending {thing ->
					thing.score
				}
				thingList.postValue(it.result)
			}.bindLife()
		
	}
	
	
}