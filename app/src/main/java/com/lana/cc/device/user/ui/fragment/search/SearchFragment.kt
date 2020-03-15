package com.lana.cc.device.user.ui.fragment.search

import android.content.Intent
import androidx.recyclerview.widget.LinearLayoutManager
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentSearchBinding
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.ui.activity.ContentActivity

class SearchFragment : BaseFragment<FragmentSearchBinding, SearchViewModel>(
    SearchViewModel::class.java, layoutRes = R.layout.fragment_search

) {
    override fun initView() {

        //结果列表
        binding.recSearchList.run {
            layoutManager = LinearLayoutManager(context)
            adapter = SearchListAdapter {}
        }

        //列表数据监听
        viewModel.searchList.observeNonNull {
            (binding.recSearchList.adapter as SearchListAdapter).replaceData(it)
        }

        //监听输入框的文字变化
        viewModel.searchKey.observeNonNull {
            if (viewModel.searchKey.value!!.isNotEmpty()) {
                viewModel.searchKey(it)
            }
        }


        //拍照搜索按钮
        binding.btnCamera.setOnClickListener {
            jumpToCameraSearch()
        }

        //返回按钮
        binding.btnBack.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initData() {
        viewModel.getCity()
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
}