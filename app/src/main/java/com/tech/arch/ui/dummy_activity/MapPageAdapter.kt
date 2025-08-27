package com.tech.arch.ui.dummy_activity

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.gms.maps.model.LatLng
import com.tech.arch.data.model.GetMapsApiResponse

class MapPageAdapter(
    activity: FragmentActivity,
    private val contributes: List<GetMapsApiResponse.Contribute>
) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = contributes.size

    override fun createFragment(position: Int): Fragment {
        return DummyMapFragment.newInstance(contributes[position])
    }
}

