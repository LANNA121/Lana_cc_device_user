package com.lana.cc.device.user.ui.fragment.register

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.FragmentRegisterBinding
import com.lana.cc.device.user.ui.activity.MainActivity


class RegisterFragment : BaseFragment<FragmentRegisterBinding, RegisterViewModel>(
    RegisterViewModel::class.java, layoutRes = R.layout.fragment_register
) {
    override fun initView() {

/*        viewModel.userId.observeNonNull {
            binding.goMainButton.isEnabled = it.isNotEmpty()
                    && viewModel.password.value!!.isNotEmpty()
        }

        viewModel.password.observeNonNull {
            binding.goMainButton.isEnabled = it.isNotEmpty()
                    && viewModel.userId.value!!.isNotEmpty()
        }*/

        binding.btnBack.setOnClickListener {
            findNavController().popBackStack()
        }

        viewModel.isLoginSuccess.observeNonNull {
            if (it) {
                startActivity(Intent(context, MainActivity::class.java))
                activity!!.finish()
            }
        }

    }

    override fun initData() {
        viewModel.init()
    }
}