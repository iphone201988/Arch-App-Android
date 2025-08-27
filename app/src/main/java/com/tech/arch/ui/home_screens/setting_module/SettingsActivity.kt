package com.tech.arch.ui.home_screens.setting_module

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.tech.arch.BR
import com.tech.arch.R
import com.tech.arch.data.model.Account
import com.tech.arch.databinding.ActivitySettingsBinding
import com.tech.arch.databinding.ItemLayoutAccountBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import com.tech.arch.ui.home_screens.change_password.ChangePasswordActivity
import com.tech.arch.ui.home_screens.edit_your_profile.EditYourProfile
import com.tech.arch.utils.ImageUtils
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : BaseActivity<ActivitySettingsBinding>() {

    private val viewModel : SettingsActivityVm by viewModels()

    private lateinit var  settingAdapter : SimpleRecyclerViewAdapter<Account, ItemLayoutAccountBinding>
    private var settingList = arrayListOf<Account>()


    override fun getLayoutResource(): Int {
        return R.layout.activity_settings
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        ImageUtils.getStatusBarColor(this)
        initOnClick()
        getSettingList()
        initAdapter()
    }

    private fun getSettingList() {
        settingList.add(Account(R.drawable.iv_profile, "Edit your profile"))
        settingList.add(Account(R.drawable.iv_profile, "Change password"))
        settingList.add(Account(R.drawable.iv_profile, "Help"))
    }

    private fun initAdapter() {
        settingAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_account, BR.bean){ v, m, pos ->
            when(m.title){
                "Edit your profile" ->{
                    val intent = Intent(this , EditYourProfile::class.java)
                    startActivity(intent)
                }
                "Change password" ->{
                    val intent = Intent(this , ChangePasswordActivity::class.java)
                    startActivity(intent)
                }
            }
        }
        binding.rvSettings.adapter = settingAdapter
        settingAdapter.list = settingList
        settingAdapter.notifyDataSetChanged()
    }

    private fun initOnClick() {

    }
}