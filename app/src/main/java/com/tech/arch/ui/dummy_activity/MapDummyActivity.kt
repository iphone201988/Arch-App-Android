package com.tech.arch.ui.dummy_activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.maps.model.LatLng
import com.tech.arch.R
import com.tech.arch.databinding.ActivityMapBinding
import com.tech.arch.databinding.ActivityMapDummyBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.home_screens.HomeDashBoardActivityVm
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapDummyActivity : BaseActivity<ActivityMapDummyBinding>() {

    private val viewModel : HomeDashBoardActivityVm by viewModels()

    override fun getLayoutResource(): Int {
      return R.layout.activity_map_dummy
    }

    override fun getViewModel(): BaseViewModel {
         return viewModel
    }

    override fun onCreateView() {

        val locations = listOf(
            LatLng(37.7749, -122.4194), // San Francisco
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(40.7128, -74.0060),   // New York
            LatLng(40.7128, -74.0060),   // New York
            LatLng(34.0522, -118.2437), // Los Angeles

            LatLng(34.0522, -118.2437), // Los Angeles

            LatLng(34.0522, -118.2437), // Los Angeles

            LatLng(34.0522, -118.2437), // Los Angeles

            LatLng(34.0522, -118.2437), // Los Angeles

            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles

            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(40.7128, -74.0060),   // New York
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(40.7128, -74.0060),   // New York
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(40.7128, -74.0060),   // New York
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(40.7128, -74.0060),   // New York
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(40.7128, -74.0060),   // New York
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles
            LatLng(34.0522, -118.2437), // Los Angeles

        )

    }
}