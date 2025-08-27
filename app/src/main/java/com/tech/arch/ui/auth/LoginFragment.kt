package com.tech.arch.ui.auth

import android.view.View

import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tech.arch.R
import com.tech.arch.databinding.FragmentLoginBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.utils.ImageUtils


class LoginFragment : BaseFragment<FragmentLoginBinding>() {
    private val viewModel: AuthCommonVM by viewModels()

    override fun onCreateView(view: View) {
        initView()
        initOnClick()
        initObserver()
    }

    private fun initObserver() {

    }

    private fun initOnClick() {

    }

    private fun initView() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.buttonLogin -> {
                    ImageUtils.navigateWithSlideAnimations(findNavController(), R.id.navigateToSignupFragment)
                }
            }
        })
    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_login
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


}