package com.tech.arch.ui.home_screens.bookmark_module

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.tech.arch.ui.home_screens.bookmark_module.saved_private_map.SavedPrivateMapFragment
import com.tech.arch.ui.home_screens.bookmark_module.saved_public_map.SavedPublicMapFragment


class BookMarkAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> SavedPublicMapFragment()
            1 -> SavedPrivateMapFragment()
            else -> Fragment()
        }
    }
}
