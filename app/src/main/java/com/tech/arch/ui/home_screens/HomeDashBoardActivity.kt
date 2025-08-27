package com.tech.arch.ui.home_screens


import android.content.res.Configuration
import android.graphics.drawable.GradientDrawable
import androidx.activity.viewModels
import androidx.core.content.ContextCompat

import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.tech.arch.R
import com.tech.arch.databinding.ActivityHomeDashBoardBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint
import com.tech.arch.utils.event.SingleRequestEvent

@AndroidEntryPoint
class HomeDashBoardActivity : BaseActivity<ActivityHomeDashBoardBinding>() {
    private val viewModel: HomeDashBoardActivityVm by viewModels()
    private var isNavigating = false
    companion object {
        val navigateToHome = SingleRequestEvent<Int>()
        private const val ID_HOME = 0
        private const val ID_EXPLORE = 1
        private const val ID_MESSAGE = 2
    }

    override fun getLayoutResource(): Int {
        return R.layout.activity_home_dash_board
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }



    override fun onCreateView() {
           setBottomNavColor()
        ImageUtils.getStatusBarColor(this)


        // Get the NavHostFragment from the layout
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.navHost) as NavHostFragment

        val navController = navHostFragment.navController

        // Connect BottomNavigationView with NavController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // The theme has changed (light/dark mode)
           setBottomNavColor()
        }

    private fun setBottomNavColor() {
        val isNightMode = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
        val drawable = ContextCompat.getDrawable(this, R.drawable.bg_bottom_nav) as GradientDrawable
        val color = if (isNightMode) {
            ContextCompat.getColor(this, R.color.light_black)
        } else {
            ContextCompat.getColor(this, R.color.app_blue)
        }
        drawable.setColor(color)
        binding.bottomNavigation.background = drawable
    }



    override fun onResume() {
        super.onResume()
    }
    override fun onBackPressed() {
        super.onBackPressed()

        if (supportFragmentManager.backStackEntryCount > 0) {
            supportFragmentManager.popBackStack()
        } else {
            super.onBackPressedDispatcher.onBackPressed()
        }
        finishAffinity()
   }


}