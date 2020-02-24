package com.lana.cc.device.user.ui.fragment.login

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentLoginBinding
import com.lana.cc.device.user.ui.activity.showMainActivity

//登录的Fragment，BaseFragment 的子类 ，定义其中的 布局 Binding 和 ViewModel 的类型
class LoginFragment : BaseFragment<FragmentLoginBinding, LoginViewModel>(
    LoginViewModel::class.java, layoutRes = R.layout.fragment_login
) {

    //初始化控件
    override fun initView() {

        //注册按钮 点击事件 点击跳转至注册界面 （action_LoginFragment_to_RegisterFragment）
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_LoginFragment_to_RegisterFragment)
        }

        //登录按钮 点击事件 点击调用 ViewModel 的 login 发起登录网络请求
        binding.btnLogin.setOnClickListener {
            //判断邮箱长度是否大于10位， 密码长度是否大于6位
            if ((viewModel.password.value ?: "").length >= 6) viewModel.login()
            else Toast.makeText(context!!, "密码必须6位以上", Toast.LENGTH_SHORT).show()
        }

        //是否成功登录的状态监听 成功便将信息存在本地 并跳转至主界面
        viewModel.isLoginSuccess.observeNonNull {
            if (it) {
                //跳转至主界面
                showMainActivity(activity as AppCompatActivity)
            }
        }
    }

    //初始化ViewModel
    override fun initData() {
        viewModel.init()
    }


}