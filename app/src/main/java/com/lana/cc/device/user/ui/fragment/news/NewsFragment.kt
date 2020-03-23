package com.lana.cc.device.user.ui.fragment.news

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.DialogManageNewsBinding
import com.lana.cc.device.user.databinding.FragmentNewsBinding
import com.lana.cc.device.user.model.api.news.News
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.fragment.newsdetail.INTENT_KEY_NEWS_URl
import com.lana.cc.device.user.util.showSingleAlbum
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import java.io.File

class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>(
    NewsViewModel::class.java, R.layout.fragment_news
) {

    private var avatarFile: File? = null
    private lateinit var globalManageNewsBinding: DialogManageNewsBinding

    private fun getManageNewsDialogBinding() =
        DataBindingUtil.inflate<DialogManageNewsBinding>(
            LayoutInflater.from(context),//一个Inflater对象，打开新布局都需要使用Inflater对象
            R.layout.dialog_manage_news,//弹窗的layout文件
            null,//填null 无需多了解
            false//填false无需多了解
        )


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

        fun showManegeChooseDialog(news: News) {
            //判断是否是OSS用户
            if (viewModel.isOss.value == true) {
                //是OSS用户才能编辑
                showManageChooseDialog(
                    onEditClick = {
                        //点击编辑
                        showEditNewsDialog(news) { title, newsUrl, file, needTop ->
                            viewModel.editNews(
                                news.id,
                                news.image,
                                title,
                                newsUrl,
                                file,
                                needTop
                            )
                        }
                    },
                    onDeleteClick = {
                        //点击删除并确认
                        viewModel.deleteNews(newsId = news.id)
                    })
            }

        }

        //指定列表数据变化监听
        viewModel.topList.observeNonNull {
            if (it.isNotEmpty()) {
                //设置指定的banner数据
                binding.banner.run {
                    pause() //先暂停轮播
                    setPages(it.toList()) {
                        BannerViewHolder(it.toMutableList(),
                            onItemClick = { news ->
                                //banner的点击事件，点击跳转至详情页
                                jumpToDetail(news?.newsUrl ?: "")
                            },
                            onItemLongClick = {
                                showManegeChooseDialog(it!!)
                            })
                    } //设置数据
                    start() //开启轮播
                }
            }
        }

        //配置新闻列表
        binding.recyclerNews.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = NewsListAdapter(
                onCellClick = { news ->
                    //新闻列表点击事件，点击跳转至详情页面
                    jumpToDetail(news.newsUrl)
                },
                onCellLongClick = { news ->
                    showManegeChooseDialog(news)
                }
            )
        }

        //OSS管理员添加News按钮
        binding.btnAdd.setOnClickListener {
            showAddNewsDialog { title, newsUrl, needTop ->
                viewModel.addNews(title, newsUrl, avatarFile, needTop)
            }
        }

        //监听刷新控件
        binding.swipe.setOnRefreshListener {
            refresh()
        }
    }

    override fun initData() {
        refresh()
        viewModel.fetchClassification()
    }

    private fun refresh() {
        viewModel.fetchNews()
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
    private fun jumpToDetail(url: String) {
        findNavController().navigate(
            R.id.action_NewsFragment_to_NewsDetailFragment,
            bundleOf(Pair(INTENT_KEY_NEWS_URl, url))
        )
    }

    //添加news的弹窗方法
    @SuppressLint("SetTextI18n")
    private fun showAddNewsDialog(
        onConfirmAction: (String, String, Int) -> Unit
    ) {
        val dialogBinding = getManageNewsDialogBinding()
        globalManageNewsBinding = dialogBinding
        //添加封面的图片点击事件
        dialogBinding.newsImage.setOnClickListener {
            showSingleAlbum()
        }
        //设置News弹窗
        showViewDialog(dialogBinding.root) {
            //将方法参数中的action行为 传入这里 即达到传入的action在点击之后调用
            onConfirmAction(
                dialogBinding.etTitle.text.toString(),
                dialogBinding.etUrl.text.toString(),
                if (dialogBinding.checkTop.isChecked) 1 else 0
            )
        }
    }

    //修改news的弹窗方法
    @SuppressLint("SetTextI18n")
    private fun showEditNewsDialog(
        news: News,
        onConfirmAction: (String, String, File?, Int) -> Unit
    ) {
        val dialogBinding = getManageNewsDialogBinding()
        globalManageNewsBinding = dialogBinding
        dialogBinding.etTitle.setText(news.title)
        dialogBinding.etUrl.setText(news.newsUrl)
        dialogBinding.imageUrl = news.newsUrl
        //添加封面的图片点击事件
        dialogBinding.newsImage.setOnClickListener {
            showSingleAlbum()
        }
        //设置News弹窗
        showViewDialog(dialogBinding.root) {
            //将方法参数中的action行为 传入这里 即达到传入的action在点击之后调用
            onConfirmAction(
                dialogBinding.etTitle.text.toString(),
                dialogBinding.etUrl.text.toString(),
                avatarFile,
                if (dialogBinding.checkTop.isChecked) 1 else 0
            )
        }
    }

    //选图后的回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val images: List<LocalMedia>
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                // 图片选择结果回调
                images = PictureSelector.obtainMultipleResult(data)
                //根据返回的图片path路径生成文件（File）对象
                avatarFile = File(images[0].path)
                //更改弹窗中的图片
                globalManageNewsBinding.imageUrl = images[0].path
            }
        }
    }

}



