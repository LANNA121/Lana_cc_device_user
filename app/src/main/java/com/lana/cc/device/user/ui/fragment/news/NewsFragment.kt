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

		viewModel.newsList.observeNonNull {
			(binding.recyclerNews.adapter as NewsListAdapter).replaceData(it)
		}

		viewModel.photographyList.observeNonNull {
			binding.banner.run {
				pause()
				setPages(it.toList()) { BannerViewHolder(it) }
				start()
			}
		}

		binding.recyclerNews.apply {
			layoutManager = LinearLayoutManager(context)
			adapter = NewsListAdapter(
				viewModel.newsList.value!!
			) { news ->
				//jumpToNewsDetail(context, news.url)
			}
		}

		binding.swipe.setOnRefreshListener {
			refresh()
		}

	}

	override fun initData() {
		refresh()
	}
	
	private fun refresh() {
		context!!.checkNet().doOnComplete {
			viewModel.getNews()
			viewModel.getPhotography()
		}.doOnError {
			viewModel.isRefreshing.postValue(false)
		}.bindLife()
		
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



