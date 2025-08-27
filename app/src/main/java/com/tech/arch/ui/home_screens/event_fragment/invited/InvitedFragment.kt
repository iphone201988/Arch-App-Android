package com.tech.arch.ui.home_screens.event_fragment.invited

import android.view.View
import androidx.fragment.app.viewModels
import com.tech.arch.R
import com.tech.arch.databinding.FragmentInvitedBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint

class InvitedFragment : BaseFragment<FragmentInvitedBinding>() {

    private val viewModel: InvitedFragmentVm by viewModels()


    override fun onCreateView(view: View) {

    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_invited
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }


}