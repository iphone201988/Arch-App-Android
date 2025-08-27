package com.tech.arch

import android.app.Application
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ProcessLifecycleOwner
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {
    private var isRestarting: Boolean = false

    fun onLogout() {
        restartApp()
    }
    companion object{
        var type : Int = 0
    }

    override fun onCreate() {
        super.onCreate()

        // Set the theme based on the device's current mode
        applyThemeBasedOnDeviceSettings()

        ProcessLifecycleOwner.get().lifecycle.addObserver(
           com.tech.arch.ui.base.AppLifecycleListener(
                this@App
            )
        )
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // Update the theme dynamically when the system theme changes
        applyThemeBasedOnDeviceSettings()
    }

    private fun applyThemeBasedOnDeviceSettings() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> {
                type = 1
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            Configuration.UI_MODE_NIGHT_NO -> {
                type = 2
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            else -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
        }
    }


//    private fun applyThemeBasedOnDeviceSettings() {
//        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
//            Configuration.UI_MODE_NIGHT_YES -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
//            }
//            Configuration.UI_MODE_NIGHT_NO -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
//            }
//            Configuration.UI_MODE_NIGHT_UNDEFINED -> {
//                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
//            }
//        }
//    }

    private fun restartApp() {
        if (!isRestarting) {
            isRestarting = true
            val intent =
                baseContext.packageManager.getLaunchIntentForPackage(baseContext.packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
    /* companion object {
         private var instance: App? = null
         fun getInstance(): App? {
             return instance
         }
         fun getToken(): String? {
             return SharedPrefManager.getString(SharedPrefManager.KEY.Token, null)

         }*/


}
