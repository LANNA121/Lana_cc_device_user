package com.lana.cc.device.user.ui.fragment.search

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.manager.api.RubbishService
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.search.Category
import com.lana.cc.device.user.model.api.search.ClassificationRequestModel
import com.lana.cc.device.user.model.api.search.SearchKeyConclusion
import com.lana.cc.device.user.ui.base.BaseViewModel
import com.lana.cc.device.user.util.switchThread
import io.reactivex.Single
import org.kodein.di.generic.instance

class SearchViewModel(application: Application) : BaseViewModel(application) {

    private val rubbishService by instance<RubbishService>()

    val searchList = MutableLiveData(emptyList<SearchKeyConclusion>())

    val searchKey = MutableLiveData("")

    fun searchKey(key: String = searchKey.value ?: "") {
        rubbishService.searchClassByName(key)
            .switchThread()
            .autoProgressDialog()
            .doOnSuccess {
                val resultData = it.data?.map { searchKeyConclusion ->
                    SearchKeyConclusion(
                        searchKeyConclusion.name,
                        searchKeyConclusion.sortId
                    ).apply {
                        category = SharedPrefModel.classficationMap[searchKeyConclusion.sortId]
                            ?: Category.getNull()
                    }
                } ?: emptyList()
                searchList.postValue(resultData)
            }.catchApiError()
            .bindLife()
    }


    fun addClassification(
        searchKeyConclusion: SearchKeyConclusion
    ) {
        rubbishService.addCategory(
            ClassificationRequestModel(
                searchKeyConclusion.name,
                searchKeyConclusion.sortId
            )
        ).doOnApiSuccess {
            searchKey()
        }

    }

    fun editClassification(
        className: String,
        classNUm: Int
    ) {
        rubbishService.editCategory(
            ClassificationRequestModel(
                className,
                classNUm
            )
        )
            .doOnApiSuccess {
                searchKey()
            }

    }

    fun deleteClassification(
        searchKeyConclusion: SearchKeyConclusion
    ) {
        rubbishService.deleteCategory(searchKeyConclusion.name)
            .doOnApiSuccess {
                searchKey()
            }

    }

    private fun <T> Single<T>.doOnApiSuccess(action: ((T) -> Unit)?) {
        switchThread()
            .doOnSuccess {
                action?.invoke(it)
            }.catchApiError()
            .bindLife()
    }
}