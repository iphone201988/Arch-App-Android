package com.tech.arch.ui.auth

import androidx.activity.viewModels
import com.tech.arch.R
import com.tech.arch.databinding.ActivityDummyBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DummyActivity : BaseActivity<ActivityDummyBinding>() {

    private val viewModel : DummyActivityVm by viewModels()

    override fun getLayoutResource(): Int {
       return R.layout.activity_dummy
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
       initOnClick()

//        binding.drawingView.apply {
//
//        }


    }

    private fun initOnClick() {

    }
}