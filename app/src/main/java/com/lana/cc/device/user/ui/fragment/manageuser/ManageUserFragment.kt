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
import com.lana.cc.device.user.model.api.guide.register.ROLE_OSS
import com.lana.cc.device.user.model.api.guide.register.ROLE_USER
import com.lana.cc.device.user.model.api.mine.Profile
import com.lana.cc.device.user.ui.activity.showLoginActivity
import com.lana.cc.device.user.ui.base.BaseFragment
import com.lana.cc.device.user.ui.fragment.mine.GoodsHistoryListAdapter
import com.lana.cc.device.user.ui.utils.getImageFromServer
import com.lana.cc.device.user.ui.widget.DatePopView
import com.lana.cc.device.user.util.showSingleAlbum
import com.lana.cc.device.user.util.string2Date
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

        fun toggleEyeStatus() {
            dialogBinding.showPassword = !(dialogBinding?.showPassword ?: false)
        }
        dialogBinding.profile = profile
        if (profile?.role == ROLE_OSS) {//选中的user是管理员
            dialogBinding.rbOss.isChecked = true //将管理员单选设置为选中
        } else {//选中的user是普通用户
            dialogBinding.rbUser.isChecked = true //将普通用户单选设置为选中
        }
        dialogBinding.imageUrl = getImageFromServer(profile?.avatar)
        dialogBinding.imgAvatar.setOnClickListener {
            showSingleAlbum()
        }
        //生日文本
        dialogBinding.tvBirthday.setOnClickListener {
            DatePopView(
                context!!,
                profile?.birthday
            ) { year, month, day, birthLong ->
                dialogBinding.tvBirthday.text = "${year}年${month}月${day}日"
            }.show()
        }
        //显示/隐藏眼睛
        dialogBinding.btnEye.setOnClickListener { toggleEyeStatus() }
        //清除用户数据
        dialogBinding.btnRemoveUser.setOnClickListener {
            viewModel.removeUser(profile?.uid)
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
        viewModel.removeSuccess.observeNonNull {
            if (!it.hasBeenHandled) {
                if (it.peekContent()) {
                    currentDialog?.dismiss()
                }
            }
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
                        uid = it.uid,
                        birthLong = string2Date(globalManageUserBinding?.tvBirthday?.text.toString()).time,
                        password = if (it.password == globalManageUserBinding?.etPassword?.text.toString()) "" else globalManageUserBinding?.etPassword?.text.toString(),
                        nickName = globalManageUserBinding?.etNickName?.text.toString(),
                        signature = globalManageUserBinding?.etSignature?.text.toString(),
                        role = if (globalManageUserBinding?.rbOss?.isChecked == true) ROLE_OSS
                        else ROLE_USER
                    ) {
                        //修改成功之后拉一下整体数据
                        viewModel.fetchUsers()
                    }
                }
            }
        }

        //注销按钮监听
        binding.btnLogout.setOnClickListener {
            SharedPrefModel.hasLogin = false
            SharedPrefModel.token = ""
            activity?.run {
                showLoginActivity(this, true)
            }
        }

        //输入框变化
        binding.etSearch.addTextChangedListener {
            //变为空 重新拉取所有用户
            //判断输入框中的文字是否为空（searchKey在布局文件中使用dataBinding进行双向绑定（@={vm.searchKey}）
            // 所以一旦输入框文字变化 这里的searchKey 也变化）
            if (viewModel.searchKey.value?.isEmpty() == true)
                viewModel.fetchUsers()
            //有变化且不为空 筛选出以文字开头的账号的用户
            else filterAsUserName()
        }

        //输入框键盘事件监听（仅在需要输入框中的回车位置的按键事件时使用此方法）
        binding.etSearch.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                //键盘的搜索按钮点击事件
                filterAsUserName()
            }
            false
        }

    }

    //拉取所有用户的个人信息
    private fun filterAsUserName() {
        viewModel.userList.postValue(viewModel.oldUserList.filter {
            it.userName?.startsWith(
                viewModel.searchKey.value ?: ""
            ) ?: false
        })
    }

    //初始化数据
    override fun initData() {
        viewModel.fetchUsers()
    }

    //修改用户的弹窗
    private fun showEditUserDialog(
        profile: Profile,
        onConfirmAction: () -> Unit
    ) {
        val dialogBinding = getManageUserDialogBinding(profile)
        //兑换历史列表
        dialogBinding?.rvHistory?.adapter = GoodsHistoryListAdapterForManageDialog()
        viewModel.fetchUserExchangeHistory(profile.uid ?: 0) {
            (dialogBinding?.rvHistory?.adapter as GoodsHistoryListAdapterForManageDialog).replaceData(
                it
            )
        }
        showViewDialog(dialogBinding?.root, onConfirmAction)
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
            }
        }
    }
}



