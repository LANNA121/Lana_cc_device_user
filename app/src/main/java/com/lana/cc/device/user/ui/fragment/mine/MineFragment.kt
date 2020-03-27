package com.lana.cc.device.user.ui.fragment.mine

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.DialogUserBinding
import com.lana.cc.device.user.databinding.FragmentMineBinding
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.mine.Profile
import com.lana.cc.device.user.ui.activity.showLoginActivity
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.widget.DatePopView
import com.lana.cc.device.user.util.showSingleAlbum
import com.lana.cc.device.user.util.string2Date
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import java.io.File

class MineFragment : BaseFragment<FragmentMineBinding, MineViewModel>(
    MineViewModel::class.java, layoutRes = R.layout.fragment_mine
) {
    override fun initView() {

        viewModel.exchangeGoodsList.observeNonNull {
            (binding.recGoodsHistory.adapter as GoodsHistoryListAdapter).replaceData(it)
        }

        //头像点击事件监听，点击进入相册选图
        binding.portrait.setOnClickListener {
            //打开选图
            showSingleAlbum()
        }

        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            val searchUid = binding.etSearch.text.toString()
            if (searchUid.isNotEmpty()) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH)
                    viewModel.fetchUserProfile(searchUid.toInt())
            }

            false
        }

        //设置按钮点击事件监听 点击调用弹窗
        binding.imgSetting.setOnClickListener {
            viewModel.profile.value?.run {
                showUserInfoDialog(this) { birthLong, nickName, signature ->
                    //lambda表达式
                    //在kotlin中 当lambda表达式作为方法最后一个参数时可将这个表达式放在参数括号外
                    //使用lambda之前是这样的 showUserInfoDialog(this,{}) ,{}里面就去填入你要做的操作，
                    // 比如这里时在弹窗的完成按钮点击之后调用action，那么就是{}内的代码会在完成按钮点击后执行
                    //点击后就去修改信息，修改之前先判断我是否有修改信息
                    viewModel.updateUserProfile(birthLong, nickName, signature)
                }
            }
        }

        //recyclerView初始化
        binding.recGoodsHistory.run {
            layoutManager = LinearLayoutManager(context)
            adapter = GoodsHistoryListAdapter(
                onClick = {
                    //兑换记录单项点击事件
                },
                onFinishClick = {
                    //兑换记录单项点击事件
                    viewModel.finishBill(it.id ?: "")
                }
            )
        }

        //刷新控件监听
        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchUserProfile()
            viewModel.fetchExchangeGoodsHistory()
        }

        //注销按钮点击事件
        binding.btnLogout.setOnClickListener {
            SharedPrefModel.hasLogin = false
            SharedPrefModel.token = ""
            activity?.run {
                showLoginActivity(this, true)
            }
        }

        //监听viewModel中的isRefreshing状态
        viewModel.isRefreshing.observeNonNull {
            binding.refreshLayout.isRefreshing = it
        }

    }

    override fun initData() {
        viewModel.fetchUserProfile()
        viewModel.fetchExchangeGoodsHistory()
    }


    //弹窗方法
    @SuppressLint("SetTextI18n")
    private fun showUserInfoDialog(
        profile: Profile, action: (
            birthLong: Long, nickName: String, signature: String
        ) -> Unit
    ) {
        //dataBinding 建议查看官方文档
        val dialogBinding =
            DataBindingUtil.inflate<DialogUserBinding>(
                LayoutInflater.from(context),//一个Inflater对象，打开新布局都需要使用Inflater对象
                R.layout.dialog_user,//弹窗的layout文件
                null,//填null 无需多了解
                false//填false无需多了解
            )
        dialogBinding.profile = profile

        //生日文本
        dialogBinding.tvBirthday.setOnClickListener {
            DatePopView(
                context!!,
                profile.birthday
            ) { year, month, day, birthLong ->
                dialogBinding.tvBirthday.text = "${year}年${month}月${day}日"
            }.show()
        }

        //安卓原生弹窗  设置信息界面
        AlertDialog.Builder(context!!).setView(
            dialogBinding.root
        ).setCancelable(true)
            .setPositiveButton("完成") { _, _ ->
                //将方法参数中的action行为 传入这里 即达到传入的action在点击之后调用
                action.invoke(
                    string2Date(dialogBinding.tvBirthday.text.toString()).time,
                    dialogBinding.tvName.text.toString(),
                    dialogBinding.tvSignature.text.toString()
                )
            }
            .create()
            .show()
    }

    //选图后的回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val images: List<LocalMedia>
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                // 图片选择结果回调
                images = PictureSelector.obtainMultipleResult(data)
                viewModel.avatarFile.value = File(images[0].path)
                viewModel.uploadAvatar()
            }
        }
    }


}

