package com.lana.cc.device.user.ui.fragment.news

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.DialogManageNewsBinding
import com.lana.cc.device.user.databinding.FragmentNewsBinding
import com.lana.cc.device.user.model.api.news.News
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.fragment.newsdetail.INTENT_KEY_NEWS_CONTENT
import com.lana.cc.device.user.ui.fragment.newsdetail.INTENT_KEY_NEWS_URl
import com.lana.cc.device.user.ui.utils.getImageFromServer
import com.lana.cc.device.user.util.showSingleAlbum
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import java.io.File

const val NEWS_TYPE_TEXT = 1
const val NEWS_TYPE_URL = 2

class NewsFragment : BaseFragment<FragmentNewsBinding, NewsViewModel>(
    NewsViewModel::class.java, R.layout.fragment_news
) {

    //在弹窗中已选择的图片文件
    private var avatarFile: File? = null
    //全局弹窗binding
    private var globalManageNewsBinding: DialogManageNewsBinding? = null

    //创建全局弹窗的binding的方法
    private fun getManageNewsDialogBinding(news: News? = null): DialogManageNewsBinding? {
        val dialogBinding = DataBindingUtil.inflate<DialogManageNewsBinding>(
            LayoutInflater.from(context),//一个Inflater对象，打开新布局都需要使用Inflater对象
            R.layout.dialog_manage_news,//弹窗的layout文件
            null,//填null 无需多了解
            false//填false无需多了解
        )
        //资讯弹窗的title
        dialogBinding.etTitle.setText(news?.title)
        //资讯弹窗的图片
        dialogBinding.imageUrl = getImageFromServer(news?.image)
        //添加封面的图片点击事件
        dialogBinding?.newsImage?.setOnClickListener {
            showSingleAlbum()
        }
        //单选框监听
        dialogBinding.rgNews.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == R.id.rb_text) {
                dialogBinding.linText.visibility = View.VISIBLE
                dialogBinding.linUrl.visibility = View.GONE
            } else {
                dialogBinding.linText.visibility = View.GONE
                dialogBinding.linUrl.visibility = View.VISIBLE
            }
        }
        if (news?.type == NEWS_TYPE_TEXT)//是原创文章类型
        {
            dialogBinding.rbText.isChecked = true //设置原创文章单选按钮选中状态
            dialogBinding.linUrl.visibility = View.GONE //设置网址linearLayout不可见
            dialogBinding.etContent.setText(news.content) //设置编辑文章的内容
        } else {//是网址（url）文章类型
            dialogBinding.rbUrl.isChecked = true //设置网址文章单选按钮选中状态
            dialogBinding.linText.visibility = View.GONE //设置网址linearLayout不可见
            dialogBinding.etUrl.setText(news?.content) //设置编辑的网址
        }
        globalManageNewsBinding = dialogBinding
        return dialogBinding
    }

    //初始化View的方法
    override fun initView() {

        //刷新状态监听
        viewModel.isRefreshing.observeNonNull {
            binding.swipe.isRefreshing = it //将刷新控件的状态置为当前刷新的状态it
            binding.root.isEnabled = !it //将root布局的可点击状态（实际上isEnabled是可用状态）置为当前刷新的状态it
        }

        //新闻列表数据变化监听
        viewModel.newsList.observeNonNull {
            //调用新闻列表适配器的replaceData方法替换数据
            (binding.recyclerNews.adapter as NewsListAdapter).replaceData(it)
        }

        fun showManageChooseDialog(news: News) {
            //判断是否是OSS用户
            if (viewModel.isOss.value == true) {
                //是OSS用户 弹出编辑+删除的选择弹框
                showManageChooseDialog(
                    onEditClick = {
                        //点击编辑
                        showEditNewsDialog(news) {
                            viewModel.editNews(
                                news.id,
                                news.image,
                                globalManageNewsBinding?.etTitle?.text.toString(),
                                if (news.type == NEWS_TYPE_URL) globalManageNewsBinding?.etUrl?.text.toString()
                                else globalManageNewsBinding?.etContent?.text.toString(),
                                avatarFile,
                                if (globalManageNewsBinding?.checkTop?.isChecked == true) 1 else 0
                            )
                        }
                    },
                    onDeleteClick = {
                        //点击删除并确认
                        viewModel.deleteNews(newsId = news.id)
                    })
            }
        }

        fun jumpToDetail(news: News?) {
            //banner的点击事件，点击跳转至详情页
            if (news?.type == NEWS_TYPE_TEXT)
                jumpToDetail("", news?.content ?: "")
            else jumpToDetail(news?.content ?: "", "")
        }

        //置顶列表数据变化监听
        viewModel.topList.observeNonNull {
            if (it.isNotEmpty()) {
                //设置指定的banner数据
                binding.banner.run {
                    pause() //先暂停轮播
                    setPages(it.toList()) {
                        BannerViewHolder(it.toMutableList(),
                            onItemClick = { news ->
                                //banner的点击事件，点击跳转至详情页
                                jumpToDetail(news)
                            },
                            onItemLongClick = {
                                showManageChooseDialog(it!!)
                            })
                    } //设置数据
                    start() //开启轮播
                }
            }
        }

        //配置新闻列表
        binding.recyclerNews.apply {
            adapter = NewsListAdapter(
                onCellClick = { news ->
                    //新闻列表点击事件，点击跳转至详情页面
                    jumpToDetail(news)
                },
                onCellLongClick = { news ->
                    showManageChooseDialog(news)
                }
            )
        }

        //OSS管理员添加News按钮
        binding.btnAdd.setOnClickListener {
            showAddNewsDialog {
                viewModel.addNews(
                    globalManageNewsBinding?.etTitle?.text.toString(),
                    if (globalManageNewsBinding?.rbUrl?.isChecked == true) NEWS_TYPE_URL else NEWS_TYPE_TEXT,
                    if (globalManageNewsBinding?.rbUrl?.isChecked == true) globalManageNewsBinding?.etUrl?.text.toString()
                    else globalManageNewsBinding?.etContent?.text.toString(),
                    avatarFile,
                    if ((globalManageNewsBinding?.checkTop?.isChecked) == true) 1 else 0
                )
            }
        }

        //监听刷新控件
        binding.swipe.setOnRefreshListener {
            viewModel.fetchNews()
        }
    }

    override fun initData() {
        viewModel.fetchNews()
        viewModel.fetchClassification()
    }


    //跳转至新闻详情页面
    private fun jumpToDetail(url: String, content: String) {
        findNavController().navigate(
            R.id.action_NewsFragment_to_NewsDetailFragment,
            bundleOf(
                Pair(INTENT_KEY_NEWS_URl, url),
                Pair(INTENT_KEY_NEWS_CONTENT, content)
            )
        )
    }

    //添加资讯的弹窗
    private fun showAddNewsDialog(
        onConfirmAction: () -> Unit
    ) = showViewDialog(getManageNewsDialogBinding()?.root, onConfirmAction)

    //修改资讯的弹窗
    private fun showEditNewsDialog(
        news: News,
        onConfirmAction: () -> Unit
    ) = showViewDialog(
        getManageNewsDialogBinding(news)?.apply {
            rgNews.visibility = View.GONE //修改弹窗中隐藏更改类型的选项
        }?.root,
        onConfirmAction
    )

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
                globalManageNewsBinding?.imageUrl = images[0].path
            }
        }
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



