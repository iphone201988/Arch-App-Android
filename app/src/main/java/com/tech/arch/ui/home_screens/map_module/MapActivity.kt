package com.tech.arch.ui.home_screens.map_module

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import com.tech.arch.R
import com.tech.arch.databinding.ActivityMapBinding
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.home_screens.add_listing.AddListingFragment
import com.tech.arch.ui.home_screens.location.LocationFragment
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MapActivity : BaseActivity<ActivityMapBinding>() , OnMapReadyCallback {

    private val viewModel : MapActivityVm by viewModels()
    private lateinit var mMap: GoogleMap
    private var userLatLng: LatLng? = null
    private var address: String? = null


    companion object{
        var lat = 0.0
        var long = 0.0
    }
    override fun getLayoutResource(): Int {
        return R.layout.activity_map
      }

    override fun getViewModel(): BaseViewModel {
      return  viewModel
    }

    override fun onCreateView() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)

        initOnClick()
    }

    private fun initOnClick() {
        viewModel.onClick.observe(this , Observer {
            when(it?.id){
                R.id.confirmBtn ->{
                    Log.i("dadasd", "initOnClick: $lat ,$long ,$address")
                  AddListingFragment.lattitude = lat
                    AddListingFragment.longitude = long
                    AddListingFragment.address = address
                    AddListingFragment.screen = "map"
                    onBackPressedDispatcher.onBackPressed()
                }
            }
        })
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        setMapStyleBasedOnTheme(mMap)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true
        moveCameraToCurrentLocation()


        // Set up listener to get the location when the map stops moving
        mMap!!.setOnCameraIdleListener {
            userLatLng = mMap!!.cameraPosition.target // Get the new center of the map
            lat = userLatLng!!.latitude
            long = userLatLng!!.longitude

            // Update the UI or perform any action with the new coordinates
            address = getAddress(lat, long)
            if (address != null) {
                Log.i("dasdasd", "onMapReady: $address")
            }
        }
    }


    private fun moveCameraToCurrentLocation() {
        val location = LatLng(LocationFragment.lat.toDouble(), LocationFragment.long.toDouble())
        if (location != null){
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15f)
            mMap.animateCamera(cameraUpdate)
        }

    }

    private fun getAddress(lat: Double, lng: Double): String? {
        val geocoder = Geocoder(this, Locale.getDefault())
        return try {
            // Safely handle the nullable return type
            val addresses: List<Address>? = geocoder.getFromLocation(lat.toDouble(), lng.toDouble(), 1)

            if (!addresses.isNullOrEmpty()) {
                val address: Address = addresses[0]
                address.getAddressLine(0) // Get the full address
            } else {
                null
            }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    // Function to apply map style based on the current system theme
    private fun setMapStyleBasedOnTheme(map: GoogleMap) {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK

        val mapStyleResource = if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            R.raw.map_style_dark // Default night mode style
        } else {
            null // Use default light mode style
        }

        if (mapStyleResource != null) {
            try {
                val success = map.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, mapStyleResource))
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

    // Handle theme change and reapply the map style
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.uiMode and Configuration.UI_MODE_NIGHT_MASK != resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            // The theme has changed (light/dark mode)
            setMapStyleBasedOnTheme(mMap)
        }
    }

}