package com.tech.arch.ui.home_screens.location

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.maps.CameraUpdateFactory
import com.tech.arch.BR
import com.tech.arch.R
import com.tech.arch.data.model.Account
import com.tech.arch.data.model.GetAllListApiResponse
import com.tech.arch.data.model.GetListApiResponse
import com.tech.arch.databinding.FragmentLocationBinding
import com.tech.arch.databinding.ItemLayoutAccountBinding
import com.tech.arch.databinding.ItemLayoutAccountPopupBinding
import com.tech.arch.databinding.ItemLayoutLocationInfoBinding
import com.tech.arch.databinding.LayoutFilterListBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import com.tech.arch.ui.home_screens.bookmark_module.BookmarkActivity
import com.tech.arch.utils.BaseCustomDialog
import com.tech.arch.utils.DelayedTextWatcher
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.Polygon
import com.google.android.gms.maps.model.Polyline
import com.tech.arch.data.api.Constants
import com.tech.arch.data.api.SimpleApiResponse
import com.tech.arch.data.model.GetMapsApiResponse
import com.tech.arch.data.model.GetProfileApiResponse
import com.tech.arch.databinding.ItemLayoutLogutPopupBinding
import com.tech.arch.ui.auth.login_module.LoginActivity
import com.tech.arch.ui.dummy_activity.MapPageAdapter
import com.tech.arch.ui.home_screens.setting_module.SettingsActivity
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import com.tech.arch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationFragment : BaseFragment<FragmentLocationBinding>() , OnMapReadyCallback ,BaseCustomDialog.Listener {

    private val viewModel : LocationFragmentVm by viewModels()
    private val markerDataMap = mutableMapOf<Marker, GetAllListApiResponse.Lists>()
    private lateinit var locationListAdapter : SimpleRecyclerViewAdapter<GetAllListApiResponse.Lists, ItemLayoutLocationInfoBinding>
    private lateinit var delayedTextWatcher: DelayedTextWatcher
    private lateinit var listingAdapter: SimpleRecyclerViewAdapter<GetListApiResponse.Category, LayoutFilterListBinding>
    private lateinit var accountPopup : BaseCustomDialog<ItemLayoutAccountPopupBinding>
    private lateinit var logoutPopup : BaseCustomDialog<ItemLayoutLogutPopupBinding>
    private lateinit var  accountAdapter : SimpleRecyclerViewAdapter<Account, ItemLayoutAccountBinding>
    private lateinit var mGoogleSignInClient: GoogleSignInClient
    private var accountList = arrayListOf<Account>()
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: View
    val contributes = listOf<GetMapsApiResponse.Contribute>()

    private var contributionId : String ? = null
    private var undoType : String  ? = null

    private var currentWidth = 28f // Default width




    private var currentMode: String = ""
    private var isScrollEnabled = true // Flag to control scrollability
    private var edgeMarker: Marker? = null
    private var circle: Circle? = null
    private var dataList = ArrayList<GetAllListApiResponse.Lists>()
    private val mapElements = mutableListOf<Any>() // List to track map elements


    private var id : String ? = null
    private var listId : String ? = null

    private val polygonPoints = mutableListOf<LatLng>()
    private val linePoints = mutableListOf<LatLng>()
    private var polyline: Polyline? = null

    private var linePoly: Polyline? = null


    var rectangle: Polygon? = null
    var startPoint: LatLng? = null
    var resizeMarker: Marker? = null
    private var isDrawingEnabled = false


    private var currentPolyline: Polyline? = null
    private var isDrawing = false
    companion object{
        var lat = 0.0
        var long = 0.0
    }
    override fun onCreateView(view: View) {

//        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
//        mapFragment.getMapAsync(this)
//        mapView = mapFragment.requireView()
//       // updateCardColorBasedOnTheme()
//        if (::mMap.isInitialized) {
//            setMapStyleBasedOnTheme(mMap)
//        }





      //  viewModel.getList(Constants.GET_LIST)
        initGoogleSignIn()




//        val  data = HashMap<String,Any>()
//        data["lat"] = lat
//        data["lng"] = long
//        data["all"] = true
//
//        if (data != null){
//           // viewModel.getAllList(data, Constants.GET_ALL_LIST)
//        }


        initPopup()
        getAccountList()
        viewModel.getProfile(Constants.GET_PROFILE)


        val data = HashMap<String,Any>()
        data["limit"] = 100
        viewModel.getMaps(Constants.GET_MAPS,data)


       // viewModel.getPoints(Constants.GET_GROSPATIAL_DATA)
  //      setUpBrush()
        initOnClick()

        initAdapter()
//
//        initAdapter()
       setObserver()


//        setupSeekBar()
//
//        if (!::delayedTextWatcher.isInitialized) {
//            delayedTextWatcher =
//                DelayedTextWatcher(binding.etSearch) { searchText ->
//                    if (binding.etSearch.text.toString().trim().isNotEmpty()){
//                        val  data = HashMap<String,Any>()
//                        data["lat"] = lat
//                        data["lng"] = long
//                        data["name"] = binding.etSearch.text.toString().trim()
//
//                  //      viewModel.getAllList(data, Constants.GET_ALL_LIST)
//                    }
//                    else{
//                        val  data = HashMap<String,Any>()
//                        data["lat"] = lat
//                        data["lng"] = long
//                 //       viewModel.getAllList(data, Constants.GET_ALL_LIST)
//                    }
//
//
//                }
//        }


        if (Constants.notVisible){
            binding.tvOpen.visibility = View.GONE
            binding.tvBookmark.visibility = View.GONE
            binding.tvDislike.visibility = View.GONE
            binding.tvHazard.visibility = View.GONE
            binding.tvShare.visibility = View.GONE
            binding.tvInaccuracy.visibility = View.GONE
            binding.tvLike.visibility = View.GONE
            binding.viewPagerClickOverlay.visibility = View.GONE
        }

        binding.viewPagerClickOverlay.setOnClickListener {
            binding.tvOpen.visibility = View.GONE
            binding.tvBookmark.visibility = View.GONE
            binding.tvDislike.visibility = View.GONE
            binding.tvHazard.visibility = View.GONE
            binding.tvShare.visibility = View.GONE
            binding.tvInaccuracy.visibility = View.GONE
            binding.tvLike.visibility = View.GONE

            Constants.notVisible = true

            binding.viewPagerClickOverlay.visibility = View.GONE
        }
    }

    private fun getAccountList() {
        accountList.clear() // Clear existing data to avoid duplication
        accountList.add(Account(R.drawable.iv_payment_card, "Payment Info"))
        accountList.add(Account(R.drawable.iv_billing, "Billing History"))
        accountList.add(Account(R.drawable.iv_setting, "Settings"))
        accountList.add(Account(R.drawable.iv_terms_condition, "Terms of Service"))
        accountList.add(Account(R.drawable.iv_sign_out, "Sign Out"))
    }

    private fun initAdapter() {
        accountAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_account, BR.bean){v,m,pos ->
            when(m.title){
                "Sign Out" ->{
                    logoutPopup.show()
                }
                "Settings" ->{
                    val intent = Intent(requireContext(), SettingsActivity::class.java)
                    startActivity(intent)
                    accountPopup.dismiss()
                }
            }
        }
        accountPopup.binding.rvAccount.adapter = accountAdapter
        accountAdapter.list = accountList
        accountAdapter.notifyDataSetChanged()
    }

    private fun initGoogleSignIn() {
        val gso =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build()
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }

    private fun initPopup() {
        accountPopup = BaseCustomDialog(requireContext(), R.layout.item_layout_account_popup,this)
        logoutPopup  = BaseCustomDialog(requireContext(), R.layout.item_layout_logut_popup,this)
    }

    //    private fun setUpBrush() {
//        binding.drawingView.apply {
//            setBrushColor(R.color.app_blue)
//            setBrushAlpha(120)
////            isFocusable = false
////            isClickable = false
////            isFocusableInTouchMode = false
//        }
//        val alpha = binding.drawingView.getBrushAlpha()
//    }
//
//
////    private fun setupMapTouchListener() {
////        mapView.setOnTouchListener { _, event ->
////            val projection = mMap.projection
////            val latLng = projection.fromScreenLocation(android.graphics.Point(event.x.toInt(), event.y.toInt()))
////
////            when (event.action) {
////                MotionEvent.ACTION_DOWN -> {
////                    isDrawing = true
////                    startPolyline(latLng)
////                }
////                MotionEvent.ACTION_MOVE -> {
////                    if (isDrawing) {
////                        addPointToPolyline(latLng)
////                    }
////                }
////                MotionEvent.ACTION_UP -> {
////                    isDrawing = false
////                }
////            }
////            true // Indicate that the touch event was handled
////        }
////    }
////
////    private fun startPolyline(latLng: LatLng) {
////        currentPolyline = mMap.addPolyline(
////            PolylineOptions().add(latLng).color(0xFF0000FF.toInt()).width(8f)
////        )
////    }
////
////    private fun addPointToPolyline(latLng: LatLng) {
////        currentPolyline?.let { polyline ->
////            val points = polyline.points
////            points.add(latLng)
////            polyline.points = points
////        }
////    }
//
    private fun setObserver() {
        viewModel.obrCommon.observe(viewLifecycleOwner, Observer {
            when(it?.status){
                Status.LOADING ->{
                    progressDialogAvl.isLoading(true)
                }
                Status.SUCCESS ->{
                    hideLoading()
                    when(it.message){
//                        "getList" -> {
//                            val myDataModel: GetListApiResponse? = ImageUtils.parseJson(it.data.toString())
//                            if (myDataModel != null) {
//                                if (myDataModel.categories != null) {
//                                    val newCategory = GetListApiResponse.Category(
//                                        __v = 1, // Example data
//                                        _id = "",
//                                        createdAt = "2025-01-06T12:00:00Z",
//                                        image = "",
//                                        isDelete = false,
//                                        title = "All",
//                                        updatedAt = "2025-01-06T12:00:00Z"
//                                    )
//                                    val updatedCategories = mutableListOf(newCategory)
//                                    updatedCategories.addAll((myDataModel.categories ?: emptyList()) as Collection<GetListApiResponse.Category>)
//                                    myDataModel.categories = updatedCategories
//                                }
//                                listingAdapter.list = myDataModel.categories
//                            }
//                        }

//                        "getAllList" ->{
//                            val myDataModel : GetAllListApiResponse ? =ImageUtils.parseJson(it.data.toString())
//                            if (myDataModel != null){
//                                if (myDataModel.lists != null){
//                                    dataList = myDataModel.lists as ArrayList<GetAllListApiResponse.Lists>
//                                    locationListAdapter.list = myDataModel.lists
//                                    locationListAdapter.notifyDataSetChanged()
//
//                                    if (dataList.isNotEmpty() ){
//                                        addMarkers(dataList)
//                                    }
//
//                                }
//                            }
//                        }
//                        "getPoints" -> {
//                            val myDataModel: GetGeoPointsResponse? = ImageUtils.parseJson(it.data.toString())
//                            if (myDataModel != null) {
//                                myDataModel.geoData?.let { geoData ->
//                                    // Check if pinPoints is not empty
//                                    if (!geoData.pinPoints.isNullOrEmpty()) {
//                                        // Add markers for each pin point
//                                        geoData.pinPoints!!.forEach { pinPoint ->
//                                            pinPoint?.let {
//                                                val lat = it.lat
//                                                val lng = it.lng
//                                                if (lat != null && lng != null) {
//                                                    val markerOptions = MarkerOptions()
//                                                        .position(LatLng(lat, lng))
//                                                    // Add marker to the map
//                                                    mMap.addMarker(markerOptions)
//                                                }
//                                            }
//                                        }
//                                    }
//
//                                    // Handle nested polygon points
//                                    if (!geoData.polygonPoints.isNullOrEmpty()) {
//                                        geoData.polygonPoints!!.forEach { polygonPointList ->
//                                            if (!polygonPointList.isNullOrEmpty()) {
//                                                val polygonOptions = PolygonOptions()
//
//                                                polygonPointList.forEach { polygonPoint ->
//                                                    polygonPoint?.let {
//                                                        val lat = it.lat
//                                                        val lng = it.lng
//                                                        if (lat != null && lng != null) {
//                                                            polygonOptions.add(LatLng(lat, lng))
//                                                        }
//                                                    }
//                                                }
//
//                                                // Styling the polygon
//                                                polygonOptions.strokeColor(Color.BLUE) // Border color
//                                        //        polygonOptions.strokeWidth(5f)        // Border width
//                                                    .fillColor(Color.argb(50, 0, 0, 255))
//
//                                                // Add the polygon to the map
//                                                mMap.addPolygon(polygonOptions)
//                                            }
//                                        }
//                                    }
//                                }
//                            }
//                        }

                        "logout" ->{
                            val myDataModel : SimpleApiResponse? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                sharedPrefManager.clear()
                                logoutPopup.dismiss()
                                accountPopup.dismiss()
                                mGoogleSignInClient.signOut()
                                val intent = Intent(requireContext(), LoginActivity::class.java)
                                startActivity(intent)
                            }
                        }

                        "getProfile" ->{
                            val myDataModel : GetProfileApiResponse? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if(myDataModel.userExists != null){
                                    Log.i("data", "setObserver: ${myDataModel.userExists}")
                                     accountPopup.binding.bean = myDataModel.userExists

                                }
                            }
                        }

                        "getMaps" -> {
                            val myDataModel: GetMapsApiResponse? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null) {
                                val contributes = myDataModel.contributes?.filterNotNull()
                                if (!contributes.isNullOrEmpty()) {
                                    // Bind data to ViewPager
                                    binding.viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
                                    binding.viewPager.adapter = MapPageAdapter(requireActivity(), contributes)

                                    binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                                        override fun onPageSelected(position: Int) {
                                            super.onPageSelected(position)
                                            val currentContribute = contributes[position]
                                            contributionId = contributes[position]._id
                                            // Use currentContribute here
                                            Log.d("ViewPager", "Current page data: $currentContribute")
                                            Log.d("contributionId", "Current page data: $contributionId")
                                        }
                                    })

                                } else {
                                    Toast.makeText(requireContext(), "No dive sites found", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(requireContext(), "Failed to parse map data", Toast.LENGTH_SHORT).show()
                            }
                        }
                        "liked" ->{
                            val myDataModel : SimpleApiResponse ? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                showToast("Liked successfully")
                            }
                        }
                        "disLike" ->{
                            val myDataModel : SimpleApiResponse ? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                showToast("Dislike successfully")
                            }
                        }
                        "saveBookmark" ->{
                            val myDataModel : SimpleApiResponse ? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                showToast(myDataModel.message)
                            }
                        }

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
//
//    private fun initAdapter() {
//        locationListAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_location_info, BR.bean){v,m,pos ->
//            when(v.id){
//                R.id.consMain->{
//                  for( i in locationListAdapter.list){
//                      i.isSelect = i._id == m._id
//                  }
//                    locationListAdapter.notifyDataSetChanged()
//                    addMarkers(dataList)
//
//                }
//            }
//        }
//        binding.rvLocations.adapter = locationListAdapter
//        locationListAdapter.notifyDataSetChanged()
//
//
//        listingAdapter = SimpleRecyclerViewAdapter(R.layout.layout_filter_list, BR.bean) { v, m, pos ->
//                    when(v.id){
//                        R.id.consMain ->{
//                            listId = m._id
//                            Log.i("Dfsf", "initAdapter:$listId ")
//
//                                val  data = HashMap<String,Any>()
//                                data["lat"] = lat
//                                data["lng"] = long
//                                data["filter"] = listId.toString()
//
//                        //        viewModel.getAllList(data, Constants.GET_ALL_LIST)
//
//
//
//
//                            binding.rvListing.visibility = View.GONE
//                        }
//                    }
//
//                }
//            binding.rvListing.adapter = listingAdapter
//            listingAdapter.notifyDataSetChanged()
//
//
//    }
//
//
    private fun  initOnClick() {
        viewModel.onClick.observe(viewLifecycleOwner , Observer {
            when(it?.id){
                R.id.homeProfile ->{
                    accountPopup.show()
                }
                R.id.iv_folder ->{
                    val intent = Intent(requireContext(), BookmarkActivity::class.java)
                    startActivity(intent)
                }
                R.id.iv_bookmark ->{
                    if (contributionId != null){
                        viewModel.saveBookmark(Constants.SAVE_BOOKMARK+contributionId)
                    }
                }
                R.id.iv_like ->{
                    if (contributionId !=null){
                        val data = HashMap<String,Any>()
                        data["type"] = 1
                        viewModel.liked(Constants.LIKE_DISLIKE+contributionId,data)
                    }
                }
                R.id.iv_disLike ->{
                    if (contributionId !=null){
                        val data = HashMap<String,Any>()
                        data["type"] = 2
                        viewModel.disLike(Constants.LIKE_DISLIKE+contributionId,data)
                    }
                }
                R.id.iv_share -> {
                    val shareText = "Check out this event!" // Or whatever dynamic content you want to share
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, shareText)
                    }

                    // Start share sheet
                    startActivity(Intent.createChooser(shareIntent, "Share via"))
                }
                R.id.consFragMain->{
                    binding.tvOpen.visibility = View.GONE
                    binding.tvBookmark.visibility = View.GONE
                    binding.tvDislike.visibility = View.GONE
                    binding.tvHazard.visibility = View.GONE
                    binding.tvShare.visibility = View.GONE
                    binding.tvInaccuracy.visibility = View.GONE
                    binding.tvShare.visibility = View.GONE
                }

//                R.id.btn_rectangle ->{
//                    currentMode = "Rectangle"
//                    isScrollEnabled = false
//                    updateMapScrolling(isScrollEnabled)
//                    showToast("Rectangle")
//
//                }
//                R.id.btn_circle ->{
//                    currentMode = "Circle"
//                    isScrollEnabled = false
//                    updateMapScrolling(isScrollEnabled)
//                    showToast("Circle")
//                }
//                R.id.btn_line ->{
//                    currentMode = "Line"
//                    isScrollEnabled = false
//                    updateMapScrolling(isScrollEnabled)
//                    showToast("Line")
//                }
//                R.id.btn_polygon ->{
//                    undoType = "1"
//                    currentMode = "Polygon"
//                    isScrollEnabled = false
//                    updateMapScrolling(isScrollEnabled)
//                    showToast("Polygon")
//                    binding.singleMode.setImageResource(R.drawable.iv_polygon)
//                    binding.modes.visibility = View.GONE
//                    binding.singleMode.visibility = View.VISIBLE
//                }
//                R.id.btn_marker ->{
//                    undoType = "1"
//                    currentMode = "Marker"
//                    isScrollEnabled = false
//                    updateMapScrolling(isScrollEnabled)
//                    showToast("marker")
//                    binding.singleMode.setImageResource(R.drawable.iv_marker)
//                    binding.modes.visibility = View.GONE
//                    binding.singleMode.visibility = View.VISIBLE
//                }
//                R.id.btn_hand ->{
//                    currentMode = "Hand"
//                    if (::mMap.isInitialized) {
//                        // Always enable scrollability
//                        isScrollEnabled = true
//                        updateMapScrolling(isScrollEnabled)
//                    }
//                    showToast("hand")
//                    binding.singleMode.setImageResource(R.drawable.iv_hand)
//                    binding.modes.visibility = View.GONE
//                    binding.singleMode.visibility = View.VISIBLE
//                }
//                R.id.undoBtn ->{
//                    if (undoType == "1"){
//                        undoLastAction()
//                    }else{
//                        undoType = "2"
//                        mMap.clear()
//                        val  data = HashMap<String,Any>()
//                        data["lat"] = lat
//                        data["lng"] = long
//                        data["all"] = true
//
//                        if (data != null){
//                        //    viewModel.getAllList(data, Constants.GET_ALL_LIST)
//                        }
//
//                    }
//
//                }
//                R.id.consFragMain->{
//                    binding.rvListing.visibility = View.GONE
//                }
//                R.id.saveBtn ->{
//                    callingSaveApi()
//                }
//                R.id.singleMode ->{
//                    binding.modes.visibility = View.VISIBLE
//                    binding.singleMode.visibility = View.GONE
//                }
//                R.id.ivFilter ->{
//                    binding.rvListing.visibility = View.VISIBLE
//                }
////                R.id.highlighter -> {
////                  //  setupMapTouchListener()
////                    if (binding.drawingView.canDraw) {
////                        binding.drawingView.canDraw = false
////                        binding.highlighter.setImageResource(R.drawable.highlighter)
////                        showToast("Highlighter Disabled")
////                    } else {
////                        binding.drawingView.canDraw = true
////                        binding.highlighter.setImageResource(R.drawable.iv_select_pen)
////                        showToast("Highlighter Enabled")
////                    }
////                }
//
//
//                R.id.highlighter ->{
//                    currentMode = "Brush"
//                    isDrawingEnabled = !isDrawingEnabled
//                    if (isDrawingEnabled) {
//                        isScrollEnabled = true
//                        updateMapScrolling(isScrollEnabled)
//                        //setupCustomTouchListener()
//                        binding.seekbar1.visibility = View.VISIBLE
//                        binding.highlighter.setImageResource(R.drawable.iv_select_pen)
//                   //     setupMapGestureListeners()
//                        showToast("Brush endabled")
//                    } else {
//                        stopDrawing()
//                        //   removeCustomTouchListener()
//                        isScrollEnabled = true
//                        updateMapScrolling(isScrollEnabled)
//                        binding.seekbar1.visibility = View.GONE
//                        binding.highlighter.setImageResource(R.drawable.highlighter)
//                        showToast("Brush disabled")
//                    }
//
//                }
//
//
            }
        })
    }
//
//    private fun callingSaveApi() {
//     //   val circlePoints = mutableListOf<Map<String, Any>>()
//        val pinPoints = mutableListOf<Map<String, Double>>()
//        val polygonPointsList = mutableListOf<List<Map<String, Double>>>()
//   //     val linePointsList = mutableListOf<List<Map<String, Double>>>()
//    //    val rectanglePoints = mutableListOf<Map<String, Any>>()
//
//
//
//        // Add data for markers
//        pinPoints.addAll(mapElements.filterIsInstance<Marker>().map {
//            mapOf(
//                "lat" to it.position.latitude,
//                "lng" to it.position.longitude
//            )
//        })
//
//        // Add data for circles
////        circlePoints.addAll(mapElements.filterIsInstance<Circle>().map {
////            mapOf(
////                "center" to mapOf(
////                    "lat" to it.center.latitude,
////                    "lng" to it.center.longitude
////                ),
////                "radius" to it.radius
////            )
////        })
//
//        // Add data for polygons
//        polygonPointsList.addAll(mapElements.filterIsInstance<Polygon>().map { polygon ->
//            polygon.points.map {
//                mapOf(
//                    "lat" to it.latitude,
//                    "lng" to it.longitude
//                )
//            }
//        })
//
//        // Add data for lines (polylines)
////        linePointsList.addAll(mapElements.filterIsInstance<Polyline>().map { polyline ->
////            polyline.points.map {
////                mapOf(
////                    "lat" to it.latitude,
////                    "lng" to it.longitude
////                )
////            }
////        })
//
//        // Add data for rectangles (polygons)
////        rectanglePoints.addAll(mapElements.filterIsInstance<Polygon>().map { polygon ->
////            mapOf(
////                "ne" to mapOf( // Assuming NE corner is at index 2
////                    "lat" to polygon.points[2].latitude,
////                    "lng" to polygon.points[2].longitude
////                ),
////                "sw" to mapOf( // Assuming SW corner is at index 0
////                    "lat" to polygon.points[0].latitude,
////                    "lng" to polygon.points[0].longitude
////                )
////            )
////        })
//
//
//        // Consolidate the data into a HashMap
//        val data = HashMap<String, Any>()
//        if (pinPoints.isNotEmpty()) data["pinPoints"] = pinPoints
//       // if (circlePoints.isNotEmpty()) data["circlePoints"] = circlePoints
//        if (polygonPointsList.isNotEmpty()) data["polygonPoints"] = polygonPointsList
//     //   if (linePointsList.isNotEmpty()) data["linePoints"] = linePointsList
//       // if (rectanglePoints.isNotEmpty()) data["rectanglePoints"] = rectanglePoints
//
//        // Send the data to the API
//     //   viewModel.sendPoints(data, Constants.GROSPATIAL_DATA)
//
//        // Log the data for debugging (optional)
//        Log.i("Save", "Data sent to API: $data")
//    }
//
//
    override fun getLayoutResource(): Int {
       return  R.layout.fragment_location
    }

    override fun getViewModel(): BaseViewModel {
        return  viewModel
    }
//
    override fun onMapReady(googleMap: GoogleMap) {
    mMap = googleMap
     setMapStyleBasedOnTheme(mMap)
    mMap.uiSettings.isMyLocationButtonEnabled = false


    if (ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {

        return
    }
    mMap.isMyLocationEnabled = true
        moveCameraToCurrentLocation()
    //    updateMapScrolling(isScrollEnabled)
}

    override fun onViewClick(view: View?) {
        when(view?.id){
            R.id.ivCross ->{
                accountPopup.dismiss()
            }
            R.id.tvConfirm ->{
                viewModel.logout(Constants.LOGOUT)
//                val intent = Intent(requireContext(), LoginActivity::class.java)
//                startActivity(intent)
//                logoutPopup.dismiss()
//                requireActivity().finishAffinity()
            }
            R.id.tvCancel ->{
                accountPopup.dismiss()
                logoutPopup.dismiss()
            }
        }
    }
//
//
//
//        googleMap.setOnMapClickListener { latLng ->
//            when (currentMode) {
//                "Marker" -> {
//                    val marker= googleMap.addMarker(MarkerOptions().position(latLng))
//                    Log.i("latlong", "onMapReady: $latLng")
//
//                        if (marker != null) {
//                            mapElements.add(marker)
//
////                            val data = HashMap<String,Any>()
////                            // Add a list of pin points to the HashMap
////                            val pinPoints = mutableListOf<Map<String, Double>>()
////
////                            // Add the current marker's coordinates to the list
////                            pinPoints.add(
////                                mapOf(
////                                    "lat" to marker.position.latitude,
////                                    "lng" to marker.position.longitude
////                                )
////                            )
////
////                            // Add the pinPoints list to the data HashMap
////                            data["pinPoints"] = pinPoints
////
////                            // Send the data to the API
////                            viewModel.sendPoints(data, Constants.GROSPATIAL_DATA)
//
//                        }
//
//
//
//                }
////                "Circle" -> {
////                    Log.i("latlong", "onMapReady: $latLng")
////
////
////
////                    // Create the circle at the clicked location
////                    circle = googleMap.addCircle(
////                        CircleOptions()
////                            .center(latLng)
////                            .radius(500.0)  // Initial radius (500 meters)
////                            .strokeColor(Color.RED)
////                            .fillColor(Color.argb(50, 255, 0, 0))
////                    )
////
////                    // Create a draggable edge marker (this marker is used to adjust the circle size)
////                    edgeMarker = googleMap.addMarker(
////                        MarkerOptions()
////                            .position(LatLng(latLng.latitude + 0.005, latLng.longitude))  // Offset to place the marker at the edge
////                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
////                            .draggable(true) // Set the marker as draggable
////                            .title("Drag to adjust size")
////                    )
////
////                    // Set up a drag listener for the edge marker
////                        mMap?.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
////                        override fun onMarkerDragStart(marker: Marker) {
////                            // Optionally handle the start of dragging
////                            Log.i("Marker", "Dragging started")
////                        }
////
////                        override fun onMarkerDrag(marker: Marker) {
////                            // Update the radius of the circle dynamically based on the distance between the center and edge
////                            circle?.radius = calculateDistance(latLng, marker.position)
////                            Log.i("fdsfdsf", "onMarkerDrag: $latLng ,   ${circle?.radius}")
////                        }
////
////                        override fun onMarkerDragEnd(marker: Marker) {
////                            // Optionally handle when drag ends
////                            Log.i("Marker", "Dragging ended")
////                        }
////                    })
////
////                    if (circle != null){
////                        mapElements.add(Pair(circle, edgeMarker))
////
//////                        val data = HashMap<String,Any>()
//////                        // Add the circle data
//////                        val circlePoints = mutableListOf<Map<String, Any>>()
//////                        circlePoints.add(
//////                            mapOf(
//////                                "center" to mapOf(
//////                                    "lat" to circle!!.center.latitude,
//////                                    "lng" to circle!!.center.longitude
//////                                ),
//////                                "radius" to circle!!.radius
//////                            )
//////                        )
//////
//////                        // Add the circlePoints to the data HashMap
//////                        data["circlePoints"] = circlePoints
//////
//////                        // Send the data to the API
//////                        viewModel.sendPoints(data, Constants.GROSPATIAL_DATA)
////                    }
////
////                }
//
//
//                  "Polygon" -> {
//                    polygonPoints.add(latLng)
//
//                    // Update the polyline dynamically
//                    if (polyline == null) {
//                        // Create a new polyline if not already created
//                        polyline = googleMap.addPolyline(
//                            PolylineOptions()
//                                .addAll(polygonPoints)
//                                .color(Color.BLUE)
//                                .width(5f)
//                        )
//                    } else {
//                        // Update the existing polyline
//                        polyline?.points = polygonPoints
//                    }
//                    Log.i("polygonPoints", "onMapReady: $polygonPoints")
//
//                    // Check if the polygon can be completed (close to starting point)
//                    if (polygonPoints.size > 2 && isPointClose(latLng, polygonPoints.first())) {
//                       // connectPolygon(mMap,polygonPoints)
//                        val polygon = googleMap.addPolygon(
//                            PolygonOptions()
//                                .addAll(polygonPoints)
//                                .strokeColor(Color.BLUE)
//                                .fillColor(Color.argb(50, 0, 0, 255))
//                        )
//                        mapElements.add(polygon)  // Add the polygon to mapElements
////                        val data = HashMap<String, Any>()
////
////                        // Add the polygon points to a list
////                        val polygonPointsList = mutableListOf<List<Map<String, Double>>>()
////                        polygonPointsList.add(
////                            polygonPoints.map { point ->
////                                mapOf(
////                                    "lat" to point.latitude,
////                                    "lng" to point.longitude
////                                )
////                            }
////                        )
////
////                        // Add the polygonPointsList to the data HashMap
////                        data["polygonPoints"] = polygonPointsList
////
////                        // Send the data to the API
////                        viewModel.sendPoints(data, Constants.GROSPATIAL_DATA)
//                        polygonPoints.clear()     // Reset for a new polygon
//                        polyline?.remove()        // Remove the polyline used for drawing
//                        polyline = null
//                    }
//
//
//                    // Add the polyline to mapElements if it's not null
////                    polyline?.let {
////                        mapElements.add(it)
////                    }
//                }
//
//
//                "Brush" -> {
//                    if (isDrawingEnabled) {
//                        if (!isDrawing)
//                            startPolyline(latLng)
//                    }
//
//                    // Simulate dragging by listening to camera move
//                    mMap.setOnCameraMoveListener {
//                            if (isDrawing && currentPolyline != null) {
//                                val cameraPosition = mMap.cameraPosition.target
//                                addPointToPolyline(cameraPosition)
//                            }
//                        Log.i("latlong", "onMapReady: $latLng")
//                    }
//
//
//                }
////                "Line" -> {
////                    linePoints.add(latLng)
////
////                    // Update the polyline dynamically
////                    if (linePoly == null) {
////                        // Create a new polyline if not already created
////                        linePoly = googleMap.addPolyline(
////                            PolylineOptions()
////                                .addAll(linePoints)
////                                .color(Color.GREEN)
////                                .width(5f)
////                        )
////                    } else {
////                        // Update the existing polyline
////                        linePoly?.points = linePoints
////                    }
////
////                    // Check if the line can be completed (close to starting point)
////                    if (linePoints.size > 2 && isPointClose(latLng, linePoints.first())) {
////                        val polylane =  googleMap.addPolygon(
////                            PolygonOptions()
////                                .addAll(linePoints)
////                                .strokeColor(Color.GREEN)
////                        )
////                        mapElements.add(polylane)
//////                        val data = HashMap<String, Any>()
//////
//////                        // Add the line points to a list
//////                        val linePointsList = mutableListOf<List<Map<String, Double>>>()
//////                        linePointsList.add(
//////                            linePoints.map { point ->
//////                                mapOf(
//////                                    "lat" to point.latitude,
//////                                    "lng" to point.longitude
//////                                )
//////                            }
//////                        )
//////
//////                        // Add the linePointsList to the data HashMap
//////                        data["linePoints"] = linePointsList
//////
//////                        // Send the data to the API
//////                        viewModel.sendPoints(data, Constants.GROSPATIAL_DATA)
////
////                        linePoints.clear()     // Reset for a new line
////                        linePoly?.remove()     // Remove the polyline used for drawing
////                        linePoly = null
////                    }
////
//////                    // Add the line/polyline to mapElements if it's not null
//////                    linePoly?.let {
//////                        mapElements.add(it)
//////                    }
////                }
////                "Rectangle" -> {
////                    if (startPoint == null) {
////                        // Set the start point to the first click location
////                        startPoint = latLng
////                    } else {
////                        // Create a new bounds with the start point and the current click location
////                        val bounds = LatLngBounds.Builder()
////                            .include(startPoint!!)
////                            .include(latLng)
////                            .build()
////
////                        // Create a new rectangle with the updated bounds
////                        rectangle = googleMap.addPolygon(
////                            PolygonOptions()
////                                .add(
////                                    LatLng(bounds.southwest.latitude, bounds.southwest.longitude),
////                                    LatLng(bounds.southwest.latitude, bounds.northeast.longitude),
////                                    LatLng(bounds.northeast.latitude, bounds.northeast.longitude),
////                                    LatLng(bounds.northeast.latitude, bounds.southwest.longitude),
////                                    LatLng(bounds.southwest.latitude, bounds.southwest.longitude)
////                                )
////                                .strokeColor(Color.RED)
////                                .fillColor(Color.argb(50, 255, 0, 0))
////                        )
////
////                        // Add a draggable marker for resizing the rectangle
////                        resizeMarker = googleMap.addMarker(
////                            MarkerOptions()
////                                .position(LatLng(bounds.northeast.latitude, bounds.northeast.longitude))
////                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN))
////                                .draggable(true)
////                                .title("Drag to resize")
////                        )
////
////                        // Set up the drag listener to resize the rectangle
////                        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
////                            override fun onMarkerDragStart(marker: Marker) {
////                                Log.i("Marker", "Dragging started")
////                            }
////
////                            override fun onMarkerDrag(marker: Marker) {
////                                // Update the rectangle's corner dynamically based on the marker's position
////                                val newLatLng = marker.position
////                                val newBounds = LatLngBounds.Builder()
////                                    .include(startPoint!!)
////                                    .include(newLatLng)
////                                    .build()
////
////                                rectangle?.points = listOf(
////                                    LatLng(newBounds.southwest.latitude, newBounds.southwest.longitude),
////                                    LatLng(newBounds.southwest.latitude, newBounds.northeast.longitude),
////                                    LatLng(newBounds.northeast.latitude, newBounds.northeast.longitude),
////                                    LatLng(newBounds.northeast.latitude, newBounds.southwest.longitude),
////                                    LatLng(newBounds.southwest.latitude, newBounds.southwest.longitude)
////                                )
////
////                                Log.i("Dragging", "New rectangle size: $newBounds")
////                            }
////
////                            override fun onMarkerDragEnd(marker: Marker) {
////                                Log.i("Marker", "Dragging ended")
////                            }
////                        })
////                    }
////
////                    // Update the start point for the next click
////                    startPoint = latLng
////
////                    if (rectangle != null){
////
////                        mapElements.add(Pair(rectangle, resizeMarker))
////
////                        val data = HashMap<String, Any>()
////
////                        // Add the rectangle data
//////                        val rectanglePoints = mutableListOf<Map<String, Any>>()
//////                        rectanglePoints.add(
//////                            mapOf(
//////                                "ne" to mapOf(
//////                                    "lat" to rectangle!!.points[2].latitude,  // Northeast corner
//////                                    "lng" to rectangle!!.points[2].longitude
//////                                ),
//////                                "sw" to mapOf(
//////                                    "lat" to rectangle!!.points[0].latitude,  // Southwest corner
//////                                    "lng" to rectangle!!.points[0].longitude
//////                                )
//////                            )
//////                        )
//////
//////                        // Add the rectanglePoints to the data HashMap
//////                        data["rectanglePoints"] = rectanglePoints
//////
//////                        // Send the data to the API
//////                        viewModel.sendPoints(data, Constants.GROSPATIAL_DATA)
////
////                    }
////
////
////
////
////                    }
//
//                "Hand" -> {
//
//                }
//                else -> {
//                    binding.rvListing.visibility = View.GONE
//                   showToast("Please select mode")
//                }
//            }
//        }
//    }
//
//    private fun undoLastAction() {
//        if (mapElements.isNotEmpty()) {
//            val lastElement = mapElements.removeLast() // Remove and retrieve the last element
//            Log.i("Dfdfds", "undoLastAction: $lastElement")
//            when (lastElement) {
//                is Marker -> lastElement.remove() // Remove marker from the map
//               // is Circle -> lastElement.remove()  // Remove circle from the map
//                is Polygon -> lastElement.remove() // Remove polygon from the map
//                is Polyline -> lastElement.remove() // Remove polyline from the map
//                is Pair<*, *> -> {
//                    // Handle Pair<Circle, Marker> and remove both
//                    if (lastElement.first is Circle && lastElement.second is Marker) {
//                        val (circle, marker) = lastElement as Pair<Circle, Marker>
//                        circle.remove()  // Remove the circle
//                        marker.remove()  // Remove the marker
//                    }
//                    else if (lastElement.first is Polygon && lastElement.second is Marker){
//                        val (rectangle, marker) = lastElement as Pair<Polygon, Marker>
//                        rectangle.remove()  // Remove the circle
//                        marker.remove()  // Remove the marker
//                    }
//                }
//
//                else -> Log.w("Undo", "Unknown element type")
//            }
//        } else {
//            Toast.makeText(requireContext(), "No actions to undo", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun autoZoomToPoint(map: GoogleMap, startPoint: LatLng, distance: Float) {
//        // Calculate the zoom level based on distance (adjust thresholds as needed)
//        val zoomLevel = when {
//            distance > 1000 -> 10f // Far distance, zoom out
//            distance > 500 -> 12f  // Moderate distance
//            else -> 15f            // Close distance, zoom in
//        }
//
//        // Animate the map to zoom in on the start point
//        map.animateCamera(CameraUpdateFactory.newLatLngZoom(startPoint, zoomLevel))
//    }
//
//    private fun connectPolygon(map: GoogleMap, polygonPoints: MutableList<LatLng>) {
//        val startPoint = polygonPoints.first()
//        val endPoint = polygonPoints.last()
//
//        // Calculate distance between start and end points
//        val distance = FloatArray(1)
//        Location.distanceBetween(startPoint.latitude, startPoint.longitude, endPoint.latitude, endPoint.longitude, distance)
//
//        if (distance[0] > 10) { // If distance > 10 meters, adjust zoom
//            autoZoomToPoint(map, startPoint, distance[0])
//
//            // Delay reconnection slightly to allow zoom animation to complete
//            map.setOnCameraIdleListener {
//                polygonPoints[polygonPoints.size - 1] = startPoint // Reconnect to start point
//                map.setOnCameraIdleListener(null) // Remove listener
//            }
//        } else {
//            // Directly connect the end point to the start point
//            polygonPoints[polygonPoints.size - 1] = startPoint
//        }
//    }
//
////    private fun addMarkers(dataList: ArrayList<GetAllListApiResponse.Lists>) {
////        // Clear all existing markers from the map
////        mMap.clear()
////        dataList.forEach { item ->
////            val lat = item.lat?.toDouble()
////            val lng = item.lng?.toDouble()
////            val imageUrl = "https://arch-backend-5bh7.onrender.com${item.listCategoryId?.image}"
////            val selectedValue = item.isSelect
////
////            Log.i("dadasd", "addMarkers: ${item._id }, ${item.isSelect}")
////
////            Glide.with(this)
////                .asBitmap()
////                .load(imageUrl)
////                .into(object : CustomTarget<Bitmap>() {
////                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
////
////                        // Scale bitmap size based on selection
////                        val size = if (selectedValue) 90 else 60
////                        val scaledBitmap = Bitmap.createScaledBitmap(resource, size, size, false)
////
////                        val markerOptions = MarkerOptions()
////                            .position(LatLng(lat!!, lng!!))
////                            .title(item.title)
////                            .icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)) // Use the image as marker
////
////                        mMap.addMarker(markerOptions)
////                    }
////
////                    override fun onLoadCleared(placeholder: Drawable?) {}
////                })
////
////
////                }
////
////
////    }
//
//
//    @SuppressLint("PotentialBehaviorOverride")
//    private fun addMarkers(dataList: ArrayList<GetAllListApiResponse.Lists>) {
//        // Clear all existing markers from the map
//        mMap.clear()
//        markerDataMap.clear()
//        if (undoType != "2" ){
//        //    viewModel.getPoints(Constants.GET_GROSPATIAL_DATA)
//        }
//
//
//        dataList.forEach { item ->
//            val lat = item.lat?.toDouble()
//            val lng = item.lng?.toDouble()
//            val imageUrl = "https://arch-backend-5bh7.onrender.com${item.listCategoryId?.image}"
//            val selectedValue = item.isSelect
//
//            Glide.with(this)
//                .asBitmap()
//                .load(imageUrl)
//                .into(object : CustomTarget<Bitmap>() {
//                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                        // Scale bitmap size based on selection
//                        val size = if (selectedValue) 90 else 60
//                        val scaledBitmap = Bitmap.createScaledBitmap(resource, size, size, false)
//
//                        val markerOptions = MarkerOptions()
//                            .position(LatLng(lat!!, lng!!))
//                            .title(item.title)
//                            .icon(BitmapDescriptorFactory.fromBitmap(scaledBitmap)) // Use the image as marker
//
//                        val marker = mMap.addMarker(markerOptions)
//                        if (marker != null) {
//                            // Map the marker to the corresponding data item
//                            markerDataMap[marker] = item
//                        }
//                    }
//
//                    override fun onLoadCleared(placeholder: Drawable?) {}
//                })
//        }
//
//        // Set a marker click listener
//        // Set a marker click listener
//        mMap.setOnMarkerClickListener { marker ->
//            marker?.let {
//                val clickedData = markerDataMap[marker]
//                if (clickedData != null) {
//                    moveToTopInRecyclerView(clickedData)
//                }
//            }
//            true // Return true to consume the event
//        }
//    }
//
//
//    private fun moveToTopInRecyclerView(clickedData: GetAllListApiResponse.Lists) {
//        // Move the clicked data to the 0th index in the list
//        dataList.remove(clickedData)
//        dataList.add(0, clickedData)
//
//        // Update the adapter
//        locationListAdapter.list = dataList
//        locationListAdapter.notifyDataSetChanged()
//    }
//
    private fun moveCameraToCurrentLocation() {
        val location = LatLng(lat.toDouble(), long.toDouble())
        if (location != null){
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15f)
            mMap.animateCamera(cameraUpdate)
        }

    }
//
//    // Helper function to check if the current point is close to the starting point
//    private fun isPointClose(point1: LatLng, point2: LatLng): Boolean {
//        val tolerance = 0.0010 // Adjust tolerance as needed
//        return Math.abs(point1.latitude - point2.latitude) < tolerance &&
//                Math.abs(point1.longitude - point2.longitude) < tolerance
//    }
//
//    // Function to calculate the distance between two LatLng points (in meters)
//    private fun calculateDistance(start: LatLng, end: LatLng): Double {
//        val results = FloatArray(1)
//        Location.distanceBetween(start.latitude, start.longitude, end.latitude, end.longitude, results)
//        return results[0].toDouble()  // Distance in meters
//    }
//    // Function to enable or disable map scrolling
//    private fun updateMapScrolling(isEnabled: Boolean) {
//        if (::mMap.isInitialized) { // Check if mMap is initialized
//            mMap.uiSettings.isScrollGesturesEnabled = isEnabled
//            mMap.uiSettings.isZoomGesturesEnabled = isEnabled
//            mMap.uiSettings.isTiltGesturesEnabled = isEnabled
//            mMap.uiSettings.isRotateGesturesEnabled = isEnabled
//        } else {
//            Log.w("Map", "Map is not yet initialized. Cannot update scrolling settings.")
//        }
//    }
//
//
//
////    private fun setupMapGestureListeners() {
////        // Handle map taps
////
////            mMap.setOnMapClickListener { latLng ->
////                if (type != null){
////                    if (isDrawingEnabled) {
////                        if (!isDrawing)
////                            startPolyline(latLng)
////                    }
////                }
////
////            }
////
////            // Simulate dragging by listening to camera move
////            mMap.setOnCameraMoveListener {
////                if (type != null){
////                    if (isDrawing && currentPolyline != null) {
////                        val cameraPosition = mMap.cameraPosition.target
////                        addPointToPolyline(cameraPosition)
////                    }
////                }
////
////            }
////
////
////
////        // Stop drawing on long press
//////        mMap.setOnMapLongClickListener {
//////            stopDrawing()
//////        }
////    }
//
//    // Set up a custom touch listener on the map view
////    private fun setupCustomTouchListener() {
////        val mapView = (childFragmentManager.findFragmentById(R.id.mapView) as? SupportMapFragment)?.view
////
////        if (mapView == null) {
////            Log.e("MapView", "MapView is null")
////            return
////        }
////
////        mapView.isClickable = true
////        mapView.isFocusable = true
////        mapView.setOnTouchListener { _, event ->
////            Log.i("DFdsfsf", "setupCustomTouchListener: ${event.action.toString()}")
////            if (isDrawingEnabled) {
////                val latLng = mMap.projection.fromScreenLocation(Point(event.x.toInt(), event.y.toInt()))
////                Log.i("DSfsdf", "setupCustomTouchListener: $latLng")
////                when (event.action) {
////                    MotionEvent.ACTION_DOWN -> {
////                        startPolyline(latLng)
////                        Log.e("CustomTouchListener", "ACTION_DOWN at $latLng")
////                    }
////                    MotionEvent.ACTION_MOVE -> {
////                        addPointToPolyline(latLng)
////                        Log.e("CustomTouchListener", "ACTION_MOVE at $latLng")
////                    }
////                }
////                true // Consume touch events
////            }
////            else {
////                false // Allow map gestures when not drawing
////            }
////        }
////
////        Log.e("MapView", "Touch listener applied")
////    }
//
//
//
//
//    // Remove the custom touch listener
//    private fun removeCustomTouchListener() {
//        val mapView = (childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment).view
//        mapView?.setOnTouchListener(null)
//    }
//
//    private fun startPolyline(latLng: LatLng) {
//        isDrawing = true
//        currentPolyline = mMap.addPolyline(
//            PolylineOptions()
//                .add(latLng)
//                .color(0x400000FF.toInt()) // Semi-transparent blue
//                .width(currentWidth)
//        )
//
//        mapElements.add(currentPolyline!!) // Track the polyline
//    }
//
//    private fun addPointToPolyline(latLng: LatLng) {
//        currentPolyline?.let { polyline ->
//            val points = polyline.points
//            points.add(latLng)
//            polyline.points = points
//        }
//    }
//
//    private fun stopDrawing() {
//        isDrawing = false
//        currentPolyline = null
//    }
//
//    // Setup SeekBar listener
//    private fun setupSeekBar() {
//       binding.seekbar1.progress = currentWidth.toInt() // Set initial progress to current width
//       binding.seekbar1.max = 100 // Max width
//
//        binding.seekbar1.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
//                currentWidth = progress.toFloat() // Update currentWidth
//                updatePolylineWidth(currentWidth)
//            }
//
//            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                // Optional: Handle user starting to drag
//            }
//
//            override fun onStopTrackingTouch(seekBar: SeekBar?) {
//                // Optional: Handle user stopping drag
//            }
//        })
//    }
//
//    // Function to dynamically update polyline width
//    private fun updatePolylineWidth(newWidth: Float) {
//        currentPolyline?.width = newWidth // Apply the new width
//    }
////    override fun onPause() {
////        super.onPause()
////        mapView.onPause()
////    }
////
////    override fun onDestroy() {
////        super.onDestroy()
////        mapView.onDestroy()
////    }
////
////    override fun onLowMemory() {
////        super.onLowMemory()
////        mapView.onLowMemory()
////    }
//
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
  //  updateCardColorBasedOnTheme()
        // The theme has changed (light/dark mode)
        if (::mMap.isInitialized) {
            setMapStyleBasedOnTheme(mMap)
        }
    }

//    private fun updateCardColorBasedOnTheme() {
//        val isDarkTheme = (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
//        val cardColor = if (isDarkTheme) {
//            ContextCompat.getColor(requireContext(), R.color.light_black)
//        } else {
//            ContextCompat.getColor(requireContext(), R.color.app_blue)
//        }
//
//        val bgDrawable = binding.linearItems.background as? GradientDrawable
//        bgDrawable?.setColor(cardColor)
//    }

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
//
//
//
//
//    // Handle theme change and reapply the map style
//
//
//
//
//
    override fun onResume() {
        super.onResume()
        val  data = HashMap<String,Any>()
        data["lat"] = lat
        data["lng"] = long
        data["all"] = true
    //    viewModel.getAllList(data, Constants.GET_ALL_LIST)
            // viewModel.getProfile(Constants.GET_PROFILE)
    }




}