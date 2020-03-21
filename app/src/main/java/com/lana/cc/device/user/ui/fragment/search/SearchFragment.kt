package com.lana.cc.device.user.ui.fragment.search

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.jakewharton.rxbinding2.widget.RxTextView
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.DialogManageClassificationBinding
import com.lana.cc.device.user.databinding.FragmentSearchBinding
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.search.SearchKeyConclusion
import com.lana.cc.device.user.ui.activity.ContentActivity
import com.lana.cc.device.user.ui.base.BaseFragment
import java.util.concurrent.TimeUnit

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    SearchViewModel::class.java, layoutRes = R.layout.fragment_search
) {
    private fun getDialogClassificationBinding(): DialogManageClassificationBinding? {
        return DataBindingUtil.inflate(
            LayoutInflater.from(context),//一个Inflater对象，打开新布局都需要使用Inflater对象
            R.layout.dialog_manage_classification,//弹窗的layout文件
            null,//填null 无需多了解
            false//填false无需多了解
        )
    }

    private var chosenSortId: Int = 1

    override fun initView() {

        //结果列表
        binding.recSearchList.run {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchListAdapter(
                onClick = {},
                onCellLongClick = {
                    if (viewModel.isOss.value == true) {
                        showManageChooseDialog(
                            onEditClick = {
                                showEditClassificationDialog(it) { name, sortId ->
                                    viewModel.editClassification(
                                        SearchKeyConclusion(
                                            name,
                                            sortId
                                        )
                                    )
                                }
                            },
                            onDeleteClick = { viewModel.deleteClassification(it) }
                        )
                    }
                }
            )
        }

        //监听输入框的文字变化 1秒钟延时 防止慢速连续输入多个字符时搜索多次
        RxTextView.textChanges(binding.searchEdit)
            .debounce(1, TimeUnit.SECONDS)
            .skip(1)
            .doOnNext {
                if ((viewModel.searchKey.value ?: "").isNotEmpty())
                    viewModel.searchKey(viewModel.searchKey.value ?: "")
            }.bindLife()

        //列表数据监听
        viewModel.searchList.observeNonNull {
            (binding.recSearchList.adapter as SearchListAdapter).replaceData(it)
        }

        //拍照搜索按钮
        binding.btnCamera.setOnClickListener {
            jumpToCameraSearch()
        }

        //添加分类信息
        binding.addClassification.setOnClickListener {
            showAddClassificationDialog { name, sortId ->
                viewModel.addClassification(
                    SearchKeyConclusion(
                        name,
                        sortId
                    )
                )
            }
        }

        //返回按钮
        binding.btnBack.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initData() {

    }

    //跳转至拍照搜索界面
    private fun jumpToCameraSearch() {
        startActivity(
            ContentActivity.createIntent(
                context!!,
                ContentActivity.Destination.CameraSearch
            )
        )
    }

    override fun onResume() {
        super.onResume()
        viewModel.searchKey.postValue(SharedPrefModel.getUserModel().receiveSearchKey)
    }

    //从照相搜索界面回来 画面回调onResume 在这里初始化搜索的字段
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //将搜索关键字赋值未照相搜索识别后返回的关键字
        viewModel.searchKey.postValue(data?.getStringExtra("searchKey"))
    }

    //添加news的弹窗方法
    @SuppressLint("SetTextI18n")
    private fun showAddClassificationDialog(onConfirmAction: (String, Int) -> Unit) {
        val dialogBinding = getDialogClassificationBinding()
        dialogBinding?.initDialog()
        //设置News弹窗
        showViewDialog(dialogBinding?.root) {
            //将方法参数中的action行为 传入这里 即达到传入的action在点击之后调用
            onConfirmAction(
                dialogBinding?.etName?.text.toString(),
                chosenSortId
            )
        }
    }

    //修改news的弹窗方法
    @SuppressLint("SetTextI18n")
    private fun showEditClassificationDialog(
        searchKeyConclusion: SearchKeyConclusion,
        onConfirmAction: (String, Int) -> Unit
    ) {
        val dialogBinding = getDialogClassificationBinding()
        dialogBinding?.initDialog(searchKeyConclusion)
        //设置News弹窗
        showViewDialog(dialogBinding?.root) {
            //将方法参数中的action行为 传入这里 即达到传入的action在点击之后调用
            onConfirmAction(
                dialogBinding?.etName?.text.toString(),
                chosenSortId
            )
        }
    }

    private fun DialogManageClassificationBinding.initDialog(searchKeyConclusion: SearchKeyConclusion? = null) {
        etName.setText(searchKeyConclusion?.name)
        val spinnerList = SharedPrefModel.classficationMap.values.toList().map { it.name }
        spinnerClassification.run {
            setSelection(searchKeyConclusion?.sortId ?: 1)
            adapter =
                ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, spinnerList)
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {}
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    chosenSortId = position
                }
            }
        }
    }


}