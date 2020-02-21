package com.lana.cc.device.user.ui.base

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.observe
import com.lana.cc.device.user.R
import com.lana.cc.device.user.model.base.ApiException
import com.lana.cc.device.user.ui.activity.LoginActivity
import com.lana.cc.device.user.util.ApiUtil
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

import java.io.IOException

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
                (error as? ApiException)?.messageStr ?: context.getString(R.string.api_error_network_error)
            )
            .setCancelable(
                when {
                    ApiUtil.isLoginUnable(error) -> false
                    else -> true
                }
            )
            .setPositiveButton("ok") { d, i ->
                when {
                    ApiUtil.isLoginUnable(error) -> {
                        context.startActivity(
                            LoginActivity.createIntent(context)
                        )
                        (context as? Activity)?.finish()
                    }
                }
            }
            .setOnDismissListener { event.getContentIfNotHandled() }
            .show()
    }
}