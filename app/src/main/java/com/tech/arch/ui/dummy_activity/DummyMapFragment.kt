package com.tech.arch.ui.dummy_activity

import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.media.Image
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.tech.arch.BR
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.api.SimpleApiResponse
import com.tech.arch.data.model.Account
import com.tech.arch.data.model.GetMapsApiResponse
import com.tech.arch.databinding.FragmentDummyMapBinding
import com.tech.arch.databinding.ItemLayoutAccountBinding
import com.tech.arch.databinding.ItemLayoutAccountPopupBinding
import com.tech.arch.databinding.ItemLayoutLogutPopupBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import com.tech.arch.ui.home_screens.bookmark_module.BookmarkActivity
import com.tech.arch.ui.home_screens.location.ImageAdapter
import com.tech.arch.ui.home_screens.location.LocationFragmentVm
import com.tech.arch.utils.BaseCustomDialog
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.ImageUtils.getMarkerBitmapDescriptor
import com.tech.arch.utils.Status
import com.tech.arch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DummyMapFragment : BaseFragment<FragmentDummyMapBinding>(), OnMapReadyCallback {

    private val viewModel: DummyMapFragmentVm by viewModels()
    private lateinit var googleMap: GoogleMap
    private var contribute: GetMapsApiResponse.Contribute? = null
    private var contributionId : String ? = null



    private var latitude = 0.0
    private var longitude = 0.0

    companion object {

        private const val ARG_CONTRIBUTE = "contribute"
        fun newInstance(contribute: GetMapsApiResponse.Contribute) = DummyMapFragment().apply {
            arguments = Bundle().apply {
                putSerializable(ARG_CONTRIBUTE, contribute)
            }
        }
    }

    override fun getLayoutResource(): Int = R.layout.fragment_dummy_map

    override fun getViewModel(): BaseViewModel = viewModel


    override fun onCreateView(view: View) {
        contribute = arguments?.getSerializable(ARG_CONTRIBUTE) as? GetMapsApiResponse.Contribute

        contributionId = contribute?._id.toString()

//        latitude = arguments?.getDouble("lat") ?: 0.0
//        longitude = arguments?.getDouble("lng") ?: 0.0

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment
            ?: SupportMapFragment.newInstance().also {
                childFragmentManager.beginTransaction()
                    .replace(R.id.mapView, it)
                    .commitNow()
            }

        mapFragment.getMapAsync(this)




        initOnClick()

        initObserver()
    }



    private fun initObserver() {
        viewModel.obrCommon.observe(viewLifecycleOwner, Observer {
            when(it?.status){
                Status.LOADING ->{
                    progressDialogAvl.isLoading(true)
                }
                Status.SUCCESS ->{
                    hideLoading()
                    when(it.message){
                    }
                }
                Status.ERROR ->{
                    hideLoading()
                    showToast(it.message.toString())
                }
                else ->{

                }
            }
        })
    }



    private fun initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner , Observer {
            when(it?.id){


            }
        })
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Main location marker
        val latLng = contribute?.location?.coordinates?.let { coords ->
            if (coords.size >= 2 && coords[1] != null && coords[0] != null) {
                LatLng(coords[1]!!, coords[0]!!)
            } else LatLng(0.0, 0.0)
        } ?: LatLng(0.0, 0.0)

        map.addMarker(MarkerOptions().position(latLng).title(contribute?.name ?: "Location"))
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
        setMapStyleBasedOnTheme(map)

        // Launch coroutine for loading marker icons
        viewLifecycleOwner.lifecycleScope.launch {
            contribute?.marksLocations?.forEach { mark ->
                val lat = mark?.lat?.toDoubleOrNull()
                val lng = mark?.lng?.toDoubleOrNull()
                val imageUrl = mark?.markNumber?.image
                val title = mark?.name ?: mark?.markNumber?.title

                if (lat != null && lng != null && !imageUrl.isNullOrBlank()) {
                    val position = LatLng(lat, lng)
                    val imageDataUrl = Constants.BASE_URL_IMAGE+imageUrl
                    val icon = getMarkerBitmapDescriptor(requireContext(), imageDataUrl)
                    val markerOptions = MarkerOptions()
                        .position(position)
                        .title(title)
                        .icon(icon ?: BitmapDescriptorFactory.defaultMarker())

                    map.addMarker(markerOptions)
                }
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        //  updateCardColorBasedOnTheme()
        // The theme has changed (light/dark mode)
        if (::googleMap.isInitialized) {
            googleMap?.let { setMapStyleBasedOnTheme(it) }
        }
    }
    private fun setMapStyleBasedOnTheme(map: GoogleMap) {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val mapStyleResource = if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            R.raw.map_style_dark // Default night mode style
        } else {
            null // Use default light mode style
        }

        if (mapStyleResource != null) {
            try {
                val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), mapStyleResource))
                if (!success) {
                    Log.e("Map", "Style parsing failed.")
                } else {
                    Log.d("Map", "Night mode style applied successfully.")
                }
            } catch (e: Resources.NotFoundException) {
                Log.e("Map", "Can't find style. Error: ", e)
            }
        } else {
            map.setMapStyle(null) // Reset to default light style
            Log.d("Map", "Using default Google Maps light style.")
        }
    }


}
