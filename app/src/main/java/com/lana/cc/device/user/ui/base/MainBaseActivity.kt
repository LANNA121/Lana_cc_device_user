package com.lana.cc.device.user.ui.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.lana.cc.device.user.R
import com.lana.cc.device.user.databinding.ActivityMainBinding
import com.lana.cc.device.user.ui.activity.MainViewModel
import io.reactivex.disposables.CompositeDisposable
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein

abstract class MainBaseActivity : AppCompatActivity(), KodeinAware, BindLife {

    override val compositeDisposable = CompositeDisposable()
    override val kodein by closestKodein()

    private var currentNavController: LiveData<NavController>? = null
    private var quiteTime: Long = System.currentTimeMillis()
    protected lateinit var binding: ActivityMainBinding
    protected abstract val acViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            setUpOnBackPressedDispatcher()
            setupBottomNavigationBar()
        }
    }


    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState!!)
        setupBottomNavigationBar()
    }

    /**
     * Called on first creation and when restoring state.
     */
    private fun setupBottomNavigationBar() {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomnavigation)

//        Setup the bottom navigation view with a resultList of navigation graphs
        val controller = bottomNavigationView.setupWithNavController(
            navGraphIds = listOf(
                R.navigation.top_news
            ),
            fragmentManager = supportFragmentManager,
            containerId = R.id.nav_host_fragment_container,
            intent = intent
        )
        bottomNavigationView.setOnNavigationItemReselectedListener {
/*            if (it.itemId == R.id.home || it.itemId == R.id.clipping_folder) {
                acViewModel.onHomeTabClickEvent.value = Event(it.itemId)
            }*/
        }
        currentNavController = controller
        bottomNavigationView.selectedItemId = R.id.home
    }

    override fun onSupportNavigateUp(): Boolean {
        return currentNavController?.value?.navigateUp() ?: false
    }

    private fun setUpOnBackPressedDispatcher() {
        onBackPressedDispatcher.addCallback(this) {
            when {
                listOf(
                    R.id.NewsFragment

                ).contains(currentNavController?.value?.currentDestination?.id) -> {
                    if (System.currentTimeMillis() - quiteTime > 3000) {
                        Toast.makeText(
                            this@MainBaseActivity, R.string.exit_info, Toast.LENGTH_SHORT
                        ).show()
                        quiteTime = System.currentTimeMillis()
                    } else {
                        finish()
                    }
                }
                else -> currentNavController?.value?.popBackStack()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        supportFragmentManager.primaryNavigationFragment?.childFragmentManager?.fragments?.forEach {
            it.onActivityResult(requestCode, resultCode, data)
        }
    }

    //ext

    protected fun <T> LiveData<T>.observe(observer: (T?) -> Unit) where T : Any =
        observe(this@MainBaseActivity, Observer<T> { v -> observer(v) })

    protected fun <T> LiveData<T>.observeNonNull(observer: (T) -> Unit) {
        this.observe(this@MainBaseActivity, Observer {
            if (it != null) {
                observer(it)
            }
        })
    }


}



