package com.lana.cc.device.user.ui.base

import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.lana.cc.device.user.R
import com.lana.cc.device.user.manager.base.ServerError
import com.lana.cc.device.user.model.api.ApiException
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

import java.io.IOException
import java.net.SocketTimeoutException

class ApiErrorLiveEvent : LiveEvent<Event<Throwable>>()

fun <T> Single<T>.catchApiError(liveEvent: ApiErrorLiveEvent): Single<T> = doOnError {
    if (it is IOException) liveEvent.value = Event(it)
}

fun <T> Observable<T>.catchApiError(liveEvent: ApiErrorLiveEvent): Observable<T> = doOnError {
    if (it is IOException) liveEvent.value = Event(it)
}

fun Completable.catchApiError(liveEvent: ApiErrorLiveEvent): Completable = doOnError {
    if (it is IOException) liveEvent.value = Event(it)
}


fun ApiErrorLiveEvent.bindDialog(context: Context, owner: LifecycleOwner) {
    observe(owner) { event ->
        if (event.hasBeenHandled) return@observe

        val error = event.peekContent()
        AlertDialog.Builder(context)
            //.setTitle(error.errorModel.message)
            .setMessage(
                when (error) {
                    is ApiException -> error.messageStr
                    is SocketTimeoutException -> context.getString(R.string.api_error_server_error)
                    is ServerError -> context.getString(R.string.server_error)
                    else -> "未知异常"
                }
            )
            .setCancelable(true)
            .setPositiveButton("ok") { _, _ -> }
            .setOnDismissListener { event.getContentIfNotHandled() }
            .show()
    }
}