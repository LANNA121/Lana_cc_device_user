package com.lana.cc.device.user.ui.fragment.news

import androidx.recyclerview.widget.LinearLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentNewsBinding
import com.lana.cc.device.user.ui.base.BaseFragment

class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>(
    NewsViewModel::class.java, R.layout.fragment_news
) {

    override fun initView() {

        //刷新状态监听
        viewModel.isRefreshing.observeNonNull {
            binding.swipe.isRefreshing = it
            binding.root.isEnabled = !it
        }

        //新闻列表数据变化监听
        viewModel.newsList.observeNonNull {
            //调用新闻列表适配器的replaceData方法替换数据
            (binding.recyclerNews.adapter as NewsListAdapter).replaceData(it)
        }

        viewModel.topList.observeNonNull {
            if (it.isNotEmpty()) {
                binding.banner.run {
                    pause()
                    setPages(it.toList()) { BannerViewHolder(it.toMutableList()) }
                    start()
                }
            }
        }

        //配置新闻列表
        binding.recyclerNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = NewsListAdapter { news ->

            }
        }

        //监听刷新控件
        binding.swipe.setOnRefreshListener {
            refresh()
        }
    }

    override fun initData() {
        refresh()
    }

    private fun refresh() {
        viewModel.getNews()
    }

    override fun onResume() {
        super.onResume()
        binding.banner.start()
    }

    override fun onPause() {
        super.onPause()
        binding.banner.pause()
    }


}



