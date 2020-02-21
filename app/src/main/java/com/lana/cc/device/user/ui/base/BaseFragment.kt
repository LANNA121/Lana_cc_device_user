package com.lana.cc.device.user.ui.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.lana.cc.device.user.util.DialogUtil
import io.reactivex.Completable
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import com.lana.cc.device.user.BR

abstract class BaseFragment<Bind : ViewDataBinding, VM : BaseViewModel>
constructor(
    private val clazz: Class<VM>,
    private val bindingCreator: (LayoutInflater, ViewGroup?) -> Bind
) : Fragment(), BindLife, KodeinAware {

    constructor(clazz: Class<VM>, @LayoutRes layoutRes: Int) : this(clazz, { inflater, group ->
        DataBindingUtil.inflate(inflater, layoutRes, group, false)
    })

    protected open val viewModel: VM by lazy {
        ViewModelProviders.of(this).get(clazz)
    }

    protected lateinit var binding: Bind

    override val kodein by kodein()

    override val compositeDisposable = CompositeDisposable()

    //method

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = bindingCreator.invoke(layoutInflater, container)
        binding.lifecycleOwner = this
        binding.setVariable(BR.vm, viewModel)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initEventObserver()
        initView()
        initDataAlways()
        if (!viewModel.vmInit) initData()
        viewModel.vmInit = true
    }


    private fun initEventObserver() {
        viewModel.apiError.bindDialog(context!!, this)
        viewModel.dialogEvent.bindDialog(context!!, this)
        viewModel.progressDialog.observeNonNull {
            if (it) DialogUtil.showProgressDialog(context)
            else DialogUtil.hideProgressDialog()
        }
    }

    abstract fun initView()
    //initData will invoke only after Fragment first created.
    abstract fun initData()

    open fun initDataAlways() {}

    //ext

    protected fun <T> LiveData<T>.observe(observer: (T?) -> Unit) where T : Any =
        observe(viewLifecycleOwner, Observer<T> { v -> observer(v) })

    protected fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        this.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                observer(it)
            }
        })
    }

    fun Context.checkNet(): Completable =
        Completable.create { emitter ->
            /*            if (!isNetworkAvailable()) emitter.onError(UiError(context!!.getString(R.string.net_unavailable)))
                        else emitter.onComplete()*/
        }

    override fun onDestroy() {
        super.onDestroy()
        destroyDisposable()
    }
}
