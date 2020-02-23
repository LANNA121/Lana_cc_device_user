package com.lana.cc.device.user.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ActivityMainBinding
import com.lana.cc.device.user.ui.base.setupWithNavController

class MainActivity : AppCompatActivity() {

    private var currentNavController: LiveData<NavController>? = null
    private var quiteTime: Long = System.currentTimeMillis()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //通过dataBinding绑定activity_main布局，并得到Binding文件
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        //当界面是首次被打开时，设置BottomNavigation
        //因为若不是首次打开 Activity的 savedInstanceState 参数会有值，所以判断其是否为空即可判断是否是首次启动
        if (savedInstanceState == null) {
            //setUpOnBackPressedDispatcher()
            setupBottomNavigationBar()
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState!!)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        //通过id得到BottomNavigationView控件
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomnavigation)
        // 设置底部BottomNavigationView与4个页面构成的列表绑定在一起
        //注：绑定四个页面其实就是绑定4个Navigation文件（R.navigation.top_news...）
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.top_news,
                R.navigation.top_test,
                R.navigation.top_shop,
                R.navigation.top_mine
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment_container,
            intent = intent
        )
        //将MainActivity中的当前NavController赋值为以上
        currentNavController = controller
        //默认选中咨询页面
        bottomNavigationView.selectedItemId = R.id.navigation_news
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

}

fun showMainActivity(activityTemp: AppCompatActivity) {
    activityTemp.startActivity(Intent(activityTemp, MainActivity::class.java))
    activityTemp.finish()
}
