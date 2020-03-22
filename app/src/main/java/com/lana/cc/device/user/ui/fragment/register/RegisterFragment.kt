package com.lana.cc.device.user.ui.fragment.register

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentRegisterBinding
import com.lana.cc.device.user.ui.activity.showMainActivity
import com.lana.cc.device.user.util.showSingleAlbum
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import java.io.File

class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>(
    RegisterViewModel::class.java, layoutRes = R.layout.fragment_register
) {
    override fun initView() {

        //性别图片 点击切换性别数据（男：m  女：f）
        binding.imgGender.setOnClickListener {
            if (viewModel.gender.value == "M") {
                viewModel.gender.value = "F"
            } else viewModel.gender.value = "M"

        }

        //头像点击事件监听，点击进入相册选图
        binding.imgAvatar.setOnClickListener {
            //打开选图
            showSingleAlbum()
        }

        //跳转到登录按钮
        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        //注册并登录按钮的点击事件
        binding.btnRegisterAndLogin.setOnClickListener {
            //弹出Toast的方法 接受一个字符串
            fun showToast(str: String) = Toast.makeText(context!!, str, Toast.LENGTH_SHORT).show()
            //判判断输入是否合法
            when {
                viewModel.userEmail.value.isNullOrEmpty() -> showToast("用户名不能为空~")
                viewModel.password.value != viewModel.rePassword.value -> showToast("两次输入密码不一致~")
                viewModel.password.value?.length?:0 < 6 -> showToast("密码必须大于6位哦~")
                else -> viewModel.registerAndLogin()
            }
        }

        //登录是否成功状态监听
        viewModel.isLoginSuccess.observeNonNull {
            if (it) {
                //跳转至主界面
                showMainActivity(activity as AppCompatActivity)
            }
        }
    }

    override fun initData() {

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