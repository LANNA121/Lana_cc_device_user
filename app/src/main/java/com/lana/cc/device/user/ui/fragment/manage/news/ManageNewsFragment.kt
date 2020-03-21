package com.lana.cc.device.user.ui.fragment.manage.news

import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentManageNewsBinding
import com.lana.cc.device.user.databinding.FragmentNewsBinding
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.fragment.news.BannerViewHolder
import com.lana.cc.device.user.ui.fragment.newsdetail.INTENT_KEY_NEWS_URl

class ManageNewsFragment : BaseFragment<FragmentManageNewsBinding, ManageNewsViewModel>(
    ManageNewsViewModel::class.java, R.layout.fragment_manage_news
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
            (binding.recyclerNews.adapter as ManageNewsListAdapter).replaceData(it)
        }

        //指定列表数据变化监听
        viewModel.topList.observeNonNull {
            if (it.isNotEmpty()) {
                //设置指定的banner数据
                binding.banner.run {
                    pause() //先暂停轮播
                    setPages(it.toList()) {
                        BannerViewHolder(it.toMutableList(),
                            onItemClick = {news ->
                                //banner的点击事件，点击跳转至详情页
                                jumpToDetail(news?.newsUrl?:"")
                            },
                            onItemLongClick = {

                            })
                    } //设置数据
                    start() //开启轮播
                }
            }
        }

        //配置新闻列表
        binding.recyclerNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = ManageNewsListAdapter { news ->
                //新闻列表点击事件，点击跳转至详情页面
                jumpToDetail(news.newsUrl)
            }
        }

        //监听刷新控件
        binding.swipe.setOnRefreshListener {
            refresh()
        }
    }

    override fun initData() {
        refresh()
        viewModel.fetchClassfication()
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

    //跳转至新闻详情页面
    private fun jumpToDetail(url:String){
        findNavController().navigate(
            R.id.action_NewsFragment_to_NewsDetailFragment,
            bundleOf(Pair(INTENT_KEY_NEWS_URl,url))
        )
    }


}



