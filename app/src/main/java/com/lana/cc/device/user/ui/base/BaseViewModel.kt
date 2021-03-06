package com.lana.cc.device.user.ui.base

import android.app.Application
import androidx.annotation.StringRes
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.lana.cc.device.user.R
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.guide.register.ROLE_OSS
import com.lana.cc.device.user.util.DialogUtil
import com.lana.cc.device.user.util.autoProgressDialog
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

abstract class BaseViewModel(app: Application) : AndroidViewModel(app),
    LifecycleObserver,
    BindLife,
    KodeinAware {
    override val kodein by kodein()
    override val compositeDisposable: CompositeDisposable by lazy { CompositeDisposable() }
    val apiError = ApiErrorLiveEvent()
    val dialogEvent = DialogLiveEvent()
    val progressDialog = MutableLiveData(false)
    var vmInit = false
    val isOss by lazy {
        MutableLiveData(SharedPrefModel.getUserModel().role == ROLE_OSS)
    }

    protected fun <T> Single<T>.catchApiError(): Single<T> = catchApiError(apiError)
    protected fun <T> Single<T>.autoProgressDialog(): Single<T> = autoProgressDialog(progressDialog)

    protected fun <T> Observable<T>.catchApiError(): Observable<T> = catchApiError(apiError)

    protected fun Completable.catchApiError(): Completable = catchApiError(apiError)
    protected fun Completable.autoProgressDialog(): Completable = autoProgressDialog(progressDialog)

    protected fun showDialog(
        @StringRes title: Int = R.string.dialog_title,
        @StringRes msg: Int = R.string.dialog_msg,
        @StringRes positiveButtonText: Int = R.string.dialog_ok,
        @StringRes negativeButtonText: Int = R.string.dialog_cancel,
        @DialogUtil.ButtonType positiveButton: Long = DialogUtil.BUTTON_TYPE_OK,
        @DialogUtil.ButtonType negativeButton: Long = DialogUtil.BUTTON_TYPE_CANCEL
    ) {
        dialogEvent.value = Event(
            DialogEvent(
                title,
                msg,
                positiveButtonText,
                negativeButtonText,
                positiveButton,
                negativeButton
            )
        )
    }


    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }
}