package com.lana.cc.device.user.ui.fragment.newsdetail


import android.annotation.SuppressLint
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.navigation.fragment.findNavController
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentNewsDetailBinding
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.utils.getImageFromServer

const val INTENT_KEY_NEWS_IMAGE_URl = "imageUrl"
const val INTENT_KEY_NEWS_URl = "newsUrl"
const val INTENT_KEY_NEWS_CONTENT = "newsContent"

class NewsDetailFragment : BaseFragment<FragmentNewsDetailBinding, NewsDetailViewModel>(
    NewsDetailViewModel::class.java, R.layout.fragment_news_detail
) {
    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {

        //设置webView的一些属性
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onReceivedSslError(
                view: WebView?,
                handler: SslErrorHandler,
                error: SslError?
            ) {
                //当加载html时，如果时https请求，没有证书会报错，安卓默认会webView显示空白页面，所以需要忽略错误，让网页正常显示
                handler.proceed()//忽略证书的错误继续Load页面内容，不会显示空白页面
                super.onReceivedSslError(view, handler, error)
            }
        }
        binding.webView.settings.run {
            javaScriptEnabled = true
            mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        binding.btnBack.setOnClickListener {
            activity?.finish()
        }
    }

    override fun initData() {
        //webView加载网页
        val url = activity?.intent?.getStringExtra(INTENT_KEY_NEWS_URl)
        val contentText = activity?.intent?.getStringExtra(INTENT_KEY_NEWS_CONTENT)
        binding.imgUrl =
            getImageFromServer(activity?.intent?.getStringExtra(INTENT_KEY_NEWS_IMAGE_URl))
        if (url?.isNotEmpty() == true) {
            binding.webView.loadUrl(
                //从Fragment自己的属性arguments中拿到上个页面传过来的url的String
                arguments?.getString(INTENT_KEY_NEWS_URl)
            )
        } else {
            binding.webView.loadData(
                contentText,
                "text/html",
                "utf-8"
            )
        }
    }

}