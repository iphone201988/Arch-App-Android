package com.tech.arch.ui.home_screens.event_fragment

import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.tech.arch.R
import com.tech.arch.databinding.FragmentEventBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EventFragment : BaseFragment<FragmentEventBinding>() {

    private val viewModel : EventFragmentVm by viewModels()
    private lateinit var eventPageAdapter : EventsPagerAdapter

    override fun onCreateView(view: View) {
        initOnClick()
        initAdapter()
    }

    private fun initAdapter() {
        eventPageAdapter = EventsPagerAdapter(this)
        binding.viewPager.adapter = eventPageAdapter

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Events"
                1 -> "Attending"
                2 -> "Invited"
                else -> ""
            }
        }.attach()

    }

    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {

        })
    }

    override fun getLayoutResource(): Int {
         return R.layout.fragment_event
    }

    override fun getViewModel(): BaseViewModel {
       return viewModel
    }


}