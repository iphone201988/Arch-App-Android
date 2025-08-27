package com.tech.arch.ui.home_screens.privacy_policy

import androidx.activity.viewModels
import com.tech.arch.R
import com.tech.arch.databinding.ActivityPrivacyPolicyBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PrivacyPolicyActivity : BaseActivity<ActivityPrivacyPolicyBinding>() {

    private val viewModel : PrivacyPolicyActivityVm by viewModels()

    override fun getLayoutResource(): Int {
       return  R.layout.activity_privacy_policy
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
     }

    override fun onCreateView() {
    }
}