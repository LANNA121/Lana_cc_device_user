package com.lana.cc.device.user.ui.fragment.search


import androidx.recyclerview.widget.LinearLayoutManager
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentSearchBinding


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
            //关闭当前activity 因为这个Fragment是附着在ContentActivity里打开的 所以直接关闭ContentActivity
            activity?.finish()
        }

    }


    override fun initData() {
        viewModel.getCity()
    }

    private fun jumpToCameraSearch() {

    }


}