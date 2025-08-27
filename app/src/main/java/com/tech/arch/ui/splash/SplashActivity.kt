package com.tech.arch.ui.splash

import android.Manifest
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.tech.arch.R
import com.tech.arch.databinding.ActivitySplashBinding
import com.tech.arch.ui.auth.create_account.CreateAccountActivity
import com.tech.arch.ui.auth.login_module.LoginActivity
import com.tech.arch.ui.base.BaseActivity
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.location.LocationHandler
import com.tech.arch.ui.base.location.LocationResultListener
import com.tech.arch.ui.base.permission.PermissionHandler
import com.tech.arch.ui.base.permission.Permissions
import com.tech.arch.ui.home_screens.HomeDashBoardActivity
import com.tech.arch.ui.home_screens.location.LocationFragment
import com.tech.arch.ui.home_screens.map_module.MapActivity
import com.tech.arch.utils.ImageUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.tech.arch.ui.auth.create_account.AccountLastStepActivity
import com.tech.arch.ui.home_screens.add_listing.AddListingFragment
import com.tech.arch.utils.ImageUtils.hasPermissions
import com.tech.arch.utils.ImageUtils.permissions
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BaseActivity<ActivitySplashBinding>() , LocationResultListener {

    private val viewModel: SplashActivityVm by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationHandler: LocationHandler? = null
    private var mCurrentLocation: Location? = null
    private val NOTIFICATION_PERMISSION_REQUEST_CODE = 1001

    override fun getLayoutResource(): Int {
        return R.layout.activity_splash
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onCreateView() {
        ImageUtils.getStatusBarColor(this)
       // fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        checkLocation()
        checkPermissions()
       //checkAndRequestPermissions()
    }


    private fun checkPermissions() {
        if (!hasPermissions(this, permissions)) {
            permissionResultLauncher.launch(permissions)
        } else {
            // All permissions already granted
        }
    }


    private val permissionResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
            if (result.all { it.value }) {
                // All permissions granted
            } else {
                // One or more permissions denied
            }
        }


    private fun checkLocation() {
        Permissions.check(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION,
            0,
            object : PermissionHandler() {
                override fun onGranted() {
                    createLocationHandler()
                 //   initHandler()
                }

                override fun onDenied(context: Context?, deniedPermissions: ArrayList<String>?) {
                    super.onDenied(context, deniedPermissions)
                    checkLocation()
                }
            })
    }


    private fun createLocationHandler() {
        locationHandler = LocationHandler(this, this)
        locationHandler?.getUserLocation()
        locationHandler?.removeLocationUpdates()
    }


//    private fun checkAndRequestPermissions() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            // Newer Android version requires POST_NOTIFICATIONS too
//            requestLocationPermissions(
//                android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        } else {
//            // Older versions
//            requestLocationPermissions(
//                android.Manifest.permission.ACCESS_FINE_LOCATION,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            )
//        }
//    }
//
//    private fun requestLocationPermissions(vararg permissions: String) {
//        val permissionsLauncher = registerForActivityResult(
//            ActivityResultContracts.RequestMultiplePermissions()
//        ) { permissionsResult ->
//            if (permissionsResult.all { it.value }) {
//                fetchUserLocation()
//            } else {
//                proceedWithoutLocation()
//            }
//        }
//
//        // Check if permissions are already granted
//        val isGranted = permissions.all { permission ->
//            ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED
//        }
//
//        if (!isGranted) {
//            permissionsLauncher.launch(permissions as Array<String>?)
//        } else {
//            fetchUserLocation()
//        }
//    }

//    private fun fetchUserLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_FINE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//            || ActivityCompat.checkSelfPermission(
//                this,
//                android.Manifest.permission.ACCESS_COARSE_LOCATION
//            ) == PackageManager.PERMISSION_GRANTED
//        ) {
//            fusedLocationClient.lastLocation.addOnCompleteListener { task: Task<Location> ->
//                if (task.isSuccessful && task.result != null) {
//                    val location = task.result
//                    val latitude = location.latitude
//                    val longitude = location.longitude
//
//                    Log.i("location", "fetchUserLocation: $latitude  , $longitude")
//
//                    CreateAccountActivity.lat = latitude
//                    CreateAccountActivity.long = longitude
//
//                    LoginActivity.lat = latitude
//                    LoginActivity.long = longitude
//
//
//                    LocationFragment.lat = latitude
//                    LocationFragment.long = longitude
//
//
//                    MapActivity.lat = latitude
//                    MapActivity.long = longitude
//
//
//                    proceedWithLocation(latitude, longitude)
//                } else {
//                    proceedWithoutLocation()
//                }
//            }
//        }
//    }


    private fun proceedWithLocation(latitude: Double, longitude: Double) {
        // Handle actions with the fetched location
        initHandler() // Continue with your logic
    }

    private fun proceedWithoutLocation() {
        // Handle case where location is not available
     //  fetchUserLocation()

    }

    private fun initHandler() {
        Handler(Looper.getMainLooper()).postDelayed({
            val data = sharedPrefManager.getCurrentUser()
            if (data != null) {
                val intent = Intent(this, HomeDashBoardActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 2000)
    }

    override fun getLocation(location: Location) {
        this.mCurrentLocation = location
        Log.i("dsdsad", "getLocation: $location")
        val latitude = location.latitude
        val longitude = location.longitude


        CreateAccountActivity.lat = latitude
        CreateAccountActivity.long = longitude

        AccountLastStepActivity.lat = latitude
        AccountLastStepActivity.long = longitude

        LoginActivity.lat = latitude
        LoginActivity.long = longitude


        LocationFragment.lat = latitude
        LocationFragment.long = longitude


        MapActivity.lat = latitude
        MapActivity.long = longitude


        AddListingFragment.lat = latitude
        AddListingFragment.long = longitude

        Log.i("dsdsad", "getLocation: $latitude, $longitude")

        initHandler()
    }
}