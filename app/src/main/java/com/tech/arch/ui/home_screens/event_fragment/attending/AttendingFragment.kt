package com.tech.arch.ui.home_screens.event_fragment.attending

import android.view.View
import androidx.fragment.app.viewModels
import com.tech.arch.R
import com.tech.arch.databinding.FragmentAttendingBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AttendingFragment : BaseFragment<FragmentAttendingBinding>() {

    private val viewModel :  AttendingFragmentVm by viewModels()


    override fun onCreateView(view: View) {

    }

    override fun getLayoutResource(): Int {
       return R.layout.fragment_attending
     }

    override fun getViewModel(): BaseViewModel {
      return viewModel
    }


}