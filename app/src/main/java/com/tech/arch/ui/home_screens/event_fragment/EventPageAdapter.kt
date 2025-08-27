package com.tech.arch.ui.home_screens.event_fragment

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tech.arch.ui.home_screens.event_fragment.attending.AttendingFragment
import com.tech.arch.ui.home_screens.event_fragment.events.EventDataFragment
import com.tech.arch.ui.home_screens.event_fragment.invited.InvitedFragment

class EventsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount() = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> EventDataFragment()
            1 -> AttendingFragment()
            2 -> InvitedFragment()
            else -> Fragment()
        }
    }
}