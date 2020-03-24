package com.lana.cc.device.user.ui.fragment.manageuser

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.DialogManageUserBinding
import com.lana.cc.device.user.databinding.FragmentManageUserBinding
import com.lana.cc.device.user.manager.sharedpref.SharedPrefModel
import com.lana.cc.device.user.model.api.mine.Profile
import com.lana.cc.device.user.ui.activity.showLoginActivity
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.utils.getImageFromServer
import com.lana.cc.device.user.util.showSingleAlbum
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.entity.LocalMedia
import java.io.File

class ManageUserFragment : BaseFragment<FragmentManageUserBinding, ManageUserViewModel>(
    ManageUserViewModel::class.java, R.layout.fragment_manage_user
) {


    //创建全局弹窗的binding的方法
    private fun getManageUserDialogBinding(profile: Profile? = null): DialogManageUserBinding? {
        val dialogBinding = DataBindingUtil.inflate<DialogManageUserBinding>(
            LayoutInflater.from(context),//一个Inflater对象，打开新布局都需要使用Inflater对象
            R.layout.dialog_manage_user, //弹窗的layout文件
            null,//填null 无需多了解
            false//填false无需多了解
        )
        dialogBinding.profile = profile
        dialogBinding.imageUrl = getImageFromServer(profile?.avatar)
        dialogBinding.imgAvatar.setOnClickListener {
            showSingleAlbum()
        }
        globalManageUserBinding = dialogBinding
        return dialogBinding
    }

    //全局弹窗binding
    private var globalManageUserBinding: DialogManageUserBinding? = null

    override fun initView() {
        viewModel.isRefreshing.observeNonNull {
            binding.refreshLayout.isRefreshing = it
        }
        viewModel.userList.observeNonNull {
            (binding.recUser.adapter as UserListAdapter).replaceData(it)
        }
        viewModel.avatarFile.observeNonNull {
            globalManageUserBinding?.imageUrl = it.path
        }
        binding.refreshLayout.setOnRefreshListener {
            viewModel.fetchUsers()
        }
        binding.recUser.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = UserListAdapter {
                //onCellClick
                showEditUserDialog(it) {
                    viewModel.editUserProfile(
                        birthLong = 0,
                        nickName = globalManageUserBinding?.etNickName?.text.toString(),
                        signature = globalManageUserBinding?.etSignature?.text.toString()
                    )
                }
            }
        }
        binding.btnLogout.setOnClickListener {
            SharedPrefModel.hasLogin = false
            SharedPrefModel.token = ""
            activity?.run {
                showLoginActivity(this,true)
            }
        }

        //输入框变化
        binding.etSearch.addTextChangedListener {
            //变为空 重新拉取所有用户
            if (viewModel.searchKey.value?.isEmpty() == true)
                viewModel.fetchUsers()
            //有变化且不为空 筛选出以文字开头的账号的用户
            else filterAsUserName()

        }


        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //键盘的搜索按钮点击事件
                filterAsUserName()
            }
            false
        }

    }

    private fun filterAsUserName() {
        viewModel.userList.postValue(viewModel.oldUserList.filter {
            it.userName?.startsWith(
                viewModel.searchKey.value ?: ""
            ) ?: false
        })
    }

    override fun initData() {
        viewModel.fetchUsers()
    }

    //添加资讯的弹窗
    private fun showEditUserDialog(
        profile: Profile,
        onConfirmAction: () -> Unit
    ) = showViewDialog(getManageUserDialogBinding(profile)?.root, onConfirmAction)

    //选图后的回调
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val images: List<LocalMedia>
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PictureConfig.CHOOSE_REQUEST) {
                // 图片选择结果回调
                images = PictureSelector.obtainMultipleResult(data)
                viewModel.avatarFile.value = File(images[0].path)
            }
        }
    }


}



