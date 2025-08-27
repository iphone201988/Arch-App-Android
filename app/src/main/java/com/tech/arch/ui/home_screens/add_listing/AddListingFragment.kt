package com.tech.arch.ui.home_screens.add_listing

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.content.res.Resources
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.tech.arch.R
import com.tech.arch.data.api.Constants
import com.tech.arch.data.model.CreateListingApiResponse
import com.tech.arch.data.model.GetListApiResponse
import com.tech.arch.databinding.FragmentAddListingBinding
import com.tech.arch.databinding.ItemLayoutDeleteMapPopupBinding
import com.tech.arch.databinding.ItemLayoutDialogSaveBinding
import com.tech.arch.databinding.ItemLayoutListingBinding
import com.tech.arch.ui.base.BaseFragment
import com.tech.arch.ui.base.BaseViewModel
import com.tech.arch.ui.base.SimpleRecyclerViewAdapter
import com.tech.arch.ui.home_screens.HomeDashBoardActivity
import com.tech.arch.utils.BaseCustomDialog
import com.tech.arch.utils.ImageUtils
import com.tech.arch.utils.Status
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompletePrediction
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.tech.arch.BR
import com.tech.arch.data.model.AddContributionApiResponse
import com.tech.arch.data.model.LikeData
import com.tech.arch.data.model.MarkLocation
import com.tech.arch.data.model.PlaceDetails
import com.tech.arch.data.model.SimpleMarkerData
import com.tech.arch.databinding.ItemCategoryBinding
import com.tech.arch.databinding.ItemCustomMarkerPopupBinding
import com.tech.arch.databinding.ItemLayoutLikeDislikeBinding
import com.tech.arch.databinding.ItemLayoutMarkerBinding
import com.tech.arch.ui.home_screens.location.LocationFragment
import com.tech.arch.utils.showToast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Stack

@AndroidEntryPoint
class AddListingFragment : BaseFragment<FragmentAddListingBinding>(), OnMapReadyCallback, BaseCustomDialog.Listener {

    private lateinit var listingAdapter: SimpleRecyclerViewAdapter<GetListApiResponse.Category, ItemLayoutListingBinding>
    private lateinit var filterCategoryAdapter : SimpleRecyclerViewAdapter<GetListApiResponse.Category,ItemCategoryBinding>
 //   private var listingList = ArrayList<ListingModel>()


    private lateinit var likeAdapter : SimpleRecyclerViewAdapter<LikeData,ItemLayoutLikeDislikeBinding>
    private lateinit var saveDialogBox  : BaseCustomDialog<ItemLayoutDialogSaveBinding>
    private lateinit var deleteMapPopup : BaseCustomDialog<ItemLayoutDeleteMapPopupBinding>
    private lateinit var markerPopup : BaseCustomDialog<ItemCustomMarkerPopupBinding>
    private lateinit var markerAdapter : SimpleRecyclerViewAdapter<GetListApiResponse.Category,ItemLayoutMarkerBinding>
    private var likeList = arrayListOf<LikeData>()
    val undoStack = Stack<SimpleMarkerData>()
    val redoStack = Stack<SimpleMarkerData>()
    val markerList = mutableListOf<Marker>()
    private var location: LatLng? = null
    private var address : String ? = null
    private val places = ArrayList<PlaceDetails>()
    private lateinit var apiKey: String
    private lateinit var placesClient: PlacesClient
    private lateinit var autocompleteSessionToken: AutocompleteSessionToken
    private lateinit var placeAdapter: PlaceAdapter

    private var likeStatus : Boolean ? =null


    private var selectedCategory: GetListApiResponse.Category? = null

    private val markLocations = mutableListOf<MarkLocation>()

    private var listId : String ? = null
    private val viewModel: AddListingFragmentVm by viewModels()
    private lateinit var mMap: GoogleMap
    private lateinit var mapView: View



    companion object{
        var lattitude = 0.0
        var longitude = 0.0
        var lat = 0.0
        var long = 0.0

        var address : String ? = null
        var screen : String ? = null
    }
    override fun onCreateView(view: View) {
        ImageUtils.getStatusBarColor(requireActivity())

        val mapFragment = childFragmentManager.findFragmentById(R.id.mapView) as SupportMapFragment
        mapFragment.getMapAsync(this)
        mapView = mapFragment.requireView()
        apiKey = getString(R.string.api_key)
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), apiKey)
        }
        placesClient = Places.createClient(requireContext())
        autocompleteSessionToken = AutocompleteSessionToken.newInstance()
        if (::mMap.isInitialized) {
            setMapStyleBasedOnTheme(mMap)
        }
        initPopup()
        locationAdapter()
        searchLocation()
        initOnCLick()
        viewModel.getMarkerList(Constants.GET_MARKERS)
   //     binding.etLocation.setText("")

        getLikeDislike()
        initAdapter()
        setObserver()


        saveDialogBox.binding.tvNotes.setOnTouchListener { v, event ->
            val parent = v.parent ?: return@setOnTouchListener false  // Safely accessing the parent
            parent.requestDisallowInterceptTouchEvent(true)
            when (event.action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_UP -> parent.requestDisallowInterceptTouchEvent(false)
            }
            false
        }


        if (Constants.mapContentNotVisible){
            binding.tvOpen.visibility = View.GONE
            binding.tvSave.visibility = View.GONE
            binding.tvDelete.visibility = View.GONE
            binding.tvRedo.visibility = View.GONE
            binding.tvUndo.visibility = View.GONE
            binding.tvEraser.visibility = View.GONE
            binding.tvBoulder.visibility = View.GONE
            binding.tvEntryPoint.visibility = View.GONE
            binding.tvHazard.visibility = View.GONE
            binding.tvParking.visibility = View.GONE
            binding.tvRestRoom.visibility = View.GONE
            binding.tvSeaGrass.visibility = View.GONE
            binding.tvMoreMarkers.visibility  = View.GONE
        }



    }


    private fun locationAdapter() {
        placeAdapter = PlaceAdapter(emptyList()) { places ->

            Log.i("dadadda", "initAdapter: $places ")

            setPlaceToEditText(places)

        }
        saveDialogBox.binding.rvEnterLocation.adapter = placeAdapter
        placeAdapter.notifyDataSetChanged()
    }

    private fun searchLocation() {

        saveDialogBox.binding.tvLocationName.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {

                if (places.isNotEmpty()) {
                    saveDialogBox.binding.locationCard.visibility = View.VISIBLE
                } else {
                    saveDialogBox.binding.locationCard.visibility = View.GONE
                }

            } else {
                saveDialogBox.binding.locationCard.visibility = View.GONE
            }

        }

       saveDialogBox.binding.tvLocationName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.isNullOrEmpty()) {
                    getPlacePredictions(s.toString())
                   saveDialogBox.binding.locationCard.visibility = View.VISIBLE
                } else {
                    placeAdapter.updatePlaces(emptyList())
                    saveDialogBox.binding.locationCard.visibility = View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    private fun getPlacePredictions(query: String) {
        val request = FindAutocompletePredictionsRequest.builder().setQuery(query).build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            val predictions = response.autocompletePredictions
            //  binding.cards.visibility = View.VISIBLE // Ensure card is visible when there's text
            fetchPlaceDetails(predictions)

        }.addOnFailureListener { exception ->
            exception.printStackTrace()
            Log.i("dsaas", "getPlacePredictions: $exception")
        }
    }
    // function to show full address details of location search

    private fun fetchPlaceDetails(predictions: List<AutocompletePrediction>) {
        val placeFields =
            listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

        places.clear()
        predictions.forEach { prediction ->
            val request = FetchPlaceRequest.builder(prediction.placeId, placeFields).build()
            placesClient.fetchPlace(request).addOnSuccessListener { response ->
                val place = response.place

                val placeDetails = PlaceDetails(
                    name = place.name ?: "", address = place.address ?: "", location = place.latLng
                )
                places.add(placeDetails)


                if (places.size == predictions.size) {
                    placeAdapter.updatePlaces(places)
                }

                Log.i("places", "fetchPlaceDetails: ${placeDetails.location}")
            }.addOnFailureListener { exception ->
                exception.printStackTrace()
            }
        }
    }
    private fun setPlaceToEditText(places: PlaceDetails) {
        val combinedAddress = "${places.name}, ${places.address}".trim(',').trim()
        saveDialogBox.binding.tvLocationName.setText(combinedAddress)
        location = places.location
        address = combinedAddress
        Log.i("dsadasda", "setPlaceToEditText: ${location!!.latitude} , ${location!!.longitude}")

        Log.i("dasd", "setPlaceToEditText: ${places.address} ")

        val latLng = LatLng(places.location.latitude, places.location.longitude)



        saveDialogBox.binding.locationCard.visibility = View.GONE
    }
    private fun getLikeDislike() {
        likeList.add(LikeData("yes"))
        likeList.add(LikeData("no"))
    }

    private fun initPopup() {
        saveDialogBox = BaseCustomDialog(requireContext(),R.layout.item_layout_dialog_save,this)
        deleteMapPopup = BaseCustomDialog(requireContext(),R.layout.item_layout_delete_map_popup,this)
        markerPopup = BaseCustomDialog(requireContext(), R.layout.item_custom_marker_popup,this)
    }


    private fun setObserver() {
        viewModel.obrCommon.observe(viewLifecycleOwner , Observer {
            when(it?.status){
                Status.LOADING ->{
                    progressDialogAvl.isLoading(true)
                }
                Status.SUCCESS ->{
                    hideLoading()
                    when(it.message){
                        "getMarkerList" ->{
                           val myDataModel :  GetListApiResponse ? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.categories != null){
                                    markerAdapter.list =  myDataModel.categories
                                    markerAdapter.notifyDataSetChanged()


                                    val selectedTitles = listOf( "Rest room", "Parking","Boulder","Hazard", "Entry Point","Sea grass")

                                    val filteredCategories: List<GetListApiResponse.Category> = myDataModel.categories!!
                                        .filterNotNull()
                                        .filter { it.isDelete == false && it.title in selectedTitles }

                                    Log.i("filterData", "setObserver: $filteredCategories")

                                    filterCategoryAdapter.list = filteredCategories
                                    filterCategoryAdapter.notifyDataSetChanged()
                                }
                               //   listingAdapter.list = myDataModel.categories


                            }
                        }
                        "createList" ->{
                            val myDataModel : CreateListingApiResponse ? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.list != null){
                                    showToast("List add successfully")
                                    val intent = Intent(requireContext(), HomeDashBoardActivity::class.java)
                                    startActivity(intent)
                                }
                            }
                        }

                        "addContribution" ->{
                            val myDataModel : AddContributionApiResponse ? = ImageUtils.parseJson(it.data.toString())
                            if (myDataModel != null){
                                if (myDataModel.contribute != null){
                                    showToast("Added successfully")
                                    saveDialogBox.dismiss()
                                }
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


    private fun initOnCLick() {
        viewModel.onClick.observe(viewLifecycleOwner, Observer {
            when (it?.id) {
                R.id.iv_save ->{
                    saveDialogBox.show()
                    Log.i("dsdsdsadas", "initOnCLick: $markLocations")
                }
                R.id.iv_delete ->{
                    deleteMapPopup.show()
                }
                R.id.extendedDrop -> {
                    binding.linearItems2.visibility = View.VISIBLE
                    binding.extendedDrop.visibility = View.GONE
                }
                R.id.up_arrow ->{
                    binding.linearItems2.visibility = View.GONE
                    binding.extendedDrop.visibility = View.VISIBLE
                }
                R.id.iv_show_markers ->{
                    markerPopup.show()
                }
                R.id.iv_redo -> {
                    if (redoStack.isNotEmpty()) {
                        val markerData = redoStack.pop()

                        lifecycleScope.launch {
                            val icon = ImageUtils.getMarkerBitmapDescriptor(requireContext(), markerData.imageUrl)

                            val marker = mMap.addMarker(
                                MarkerOptions()
                                    .position(markerData.position)
                                    .title(markerData.title)
                                    .icon(icon ?: BitmapDescriptorFactory.defaultMarker())
                            )

                            marker?.let {
                                markerList.add(it)
                                undoStack.push(markerData)

                                markLocations.add(
                                    MarkLocation(
                                        lat = it.position.latitude.toString(),
                                        lng = it.position.longitude.toString(),
                                        name = it.title ?: "",
                                        markNumber = markerData.markNumber
                                    )
                                )
                            }
                        }
                    } else {
                        showToast("Nothing to redo")
                    }
                }

                R.id.iv_undo -> {
                    if (undoStack.isNotEmpty()) {
                        val lastMarker = markerList.lastOrNull()
                        lastMarker?.remove()
                        markerList.remove(lastMarker)

                        val markerData = undoStack.pop()
                        redoStack.push(markerData)

                        if (markLocations.isNotEmpty()) {
                            markLocations.removeLast()
                        }
                    } else {
                        showToast("Nothing to undo")
                    }
                }

//                R.id.tvSignUpBtn -> {
//                    if (isEmptyField()){
//                        if (lattitude != null && longitude != null){
//                            val data = HashMap<String, Any>()
//                            data["title"] = binding.etTitle.text.toString().trim()
//                            data["description"] = binding.etDescription.text.toString().trim()
//                            data["address"] = address.toString()
//                            data["listCategoryId"] = listId.toString()
//                            data["lat"] = lattitude.toString()
//                            data["lng"] = longitude.toString()
//
//                            viewModel.createList(data, Constants.CREATE_LIST)
//                        }
//                        else{
//                            showToast("Location not found ")
//                        }
//
//                    }
 //               }
//                R.id.etLocation ->{
//                    val intent = Intent(requireContext(), MapActivity::class.java)
//                    startActivity(intent)
//                }
//                R.id.dropSelect , R.id.etSelect->{
//                    binding.rvListing.visibility = View.VISIBLE
//                }
//                R.id.mainCons , R.id.secondCons ->{
//                    binding.rvListing.visibility = View.GONE
//                }
            }
        })

    }

    private fun initAdapter() {
//        listingAdapter =
//            SimpleRecyclerViewAdapter(R.layout.item_layout_listing, BR.bean) { v, m, pos ->
//                when(v.id){
//                    R.id.consMain ->{
//                        listId = m._id
//                        Log.i("Dfsf", "initAdapter:$listId ")
//                        binding.etSelect.setText(m.title)
//                        binding.rvListing.visibility = View.GONE
//                    }
//                }
//
//            }
//        binding.rvListing.adapter = listingAdapter
//        listingAdapter.notifyDataSetChanged()
//
        markerPopup.binding.rvMarkers.layoutManager = GridLayoutManager(requireContext(), 4)
        markerAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_marker,BR.bean){v,m,pos ->
            when(v.id){
                R.id.consMain,R.id.ivMarker ->{
                    // Reset selection
                    selectedCategory = null
                    selectedCategory = m
                    Log.i("seleted marker data", "initAdapter: $selectedCategory")
                    markerPopup.dismiss() // close the popup
                }
            }

        }
        markerPopup.binding.rvMarkers.adapter = markerAdapter



        likeAdapter = SimpleRecyclerViewAdapter(R.layout.item_layout_like_dislike, BR.bean){v,m,pos ->
            when(v.id){
                R.id.consMain,R.id.title ->{
                    saveDialogBox.binding.tvLikeDislike.setText(m.text)
                    saveDialogBox.binding.rvLike.visibility = View.GONE


                    likeStatus = when (m.text?.trim()?.lowercase()) {
                        "yes" -> true
                        "no" -> false
                        else -> null
                    }
                    Log.i("dasdasdasd", "initAdapter: $likeStatus")
                }
            }
        }
        saveDialogBox.binding.rvLike.adapter = likeAdapter
        likeAdapter.list = likeList
        likeAdapter.notifyDataSetChanged()


        filterCategoryAdapter = SimpleRecyclerViewAdapter(R.layout.item_category,BR.bean){v,m,pos ->
            when(v.id){
                R.id.consMain,R.id.iv_Image ->{
                    selectedCategory = null
                    selectedCategory = m
                    Log.i("seleted marker data", "initAdapter: $selectedCategory")
                }

            }
        }
        binding.rvFilterCategory.adapter = filterCategoryAdapter


    }

    override fun getLayoutResource(): Int {
        return R.layout.fragment_add_listing
    }

    override fun getViewModel(): BaseViewModel {
        return viewModel
    }

    override fun onViewClick(view: View?) {
        when(view?.id){
            R.id.ivCross ->{
                saveDialogBox.dismiss()
            }
            R.id.ivCrossDelete ->{
                deleteMapPopup.dismiss()
            }
            R.id.tvSaveBtn ->{
                if (saveEmptyField()){
                    val data = HashMap<String,Any>()
                    data["name"] = saveDialogBox.binding.tvMapName.text.toString()
                    data["address"] = saveDialogBox.binding.tvLocationName.text.toString()
                    data["lat"] = (location?.latitude ?: lat).toString()
                    data["lng"] = (location?.longitude ?: long).toString()
                    data["maxDepth"] = saveDialogBox.binding.tvMaxDepth.text.toString()
                    data["averageVisibility"] =  saveDialogBox.binding.tvAverageVisibility.text.toString()
                    data["entryType"] = saveDialogBox.binding.tvEntryType.text.toString()
                    data["bottomComposition"] =  saveDialogBox.binding.tvComposition.text.toString()
                    data["aquaticLife"] = saveDialogBox.binding.tvAquaticLife.text.toString()
                    data["isDive"] =  likeStatus.toString()
                    data["notes"] = saveDialogBox.binding.tvNotes.text.toString()
                    data["status"] = 2
                    data["marksLocations"] = markLocations

                    Log.i("data", "onViewClick: $data")

                    viewModel.addContribution(data,Constants.SAVE_MAP)
                }
            }
            R.id.tvLikeDislike ->{
                saveDialogBox.binding.rvLike.visibility = View.VISIBLE
            }
        }
    }

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
        //    updateMapScrolling(isScrollEnabled)

        googleMap.setOnMapClickListener { latLng ->

            binding.tvOpen.visibility = View.GONE
            binding.tvSave.visibility = View.GONE
            binding.tvDelete.visibility = View.GONE
            binding.tvRedo.visibility = View.GONE
            binding.tvUndo.visibility = View.GONE
            binding.tvEraser.visibility = View.GONE
            binding.tvBoulder.visibility = View.GONE
            binding.tvEntryPoint.visibility = View.GONE
            binding.tvHazard.visibility = View.GONE
            binding.tvParking.visibility = View.GONE
            binding.tvRestRoom.visibility = View.GONE
            binding.tvSeaGrass.visibility = View.GONE
            binding.tvMoreMarkers.visibility  = View.GONE

            Constants.mapContentNotVisible = true



            val category = selectedCategory
            if (category == null) {
                showToast("Please select a marker first")
                return@setOnMapClickListener
            }

            val imageUrl = Constants.BASE_URL_IMAGE+category.image

            lifecycleScope.launch {
                try {
                    val icon = ImageUtils.getMarkerBitmapDescriptor(requireContext(), imageUrl)

                    val markerOptions = MarkerOptions()
                        .position(latLng)
                        .title(category.title)
                        .icon(icon ?: BitmapDescriptorFactory.defaultMarker())

                    val marker = googleMap.addMarker(markerOptions)

                    // Only add marker data if added successfully
                    if (marker != null) {
                        markerList.add(marker)
                        undoStack.push(
                            SimpleMarkerData(
                                position = latLng,
                                title = category.title ?: "",
                                imageUrl = imageUrl,
                                markNumber = category._id ?: ""
                            )
                        )

                        markLocations.add(
                            MarkLocation(
                                lat = latLng.latitude.toString(),
                                lng = latLng.longitude.toString(),
                                name = category.title.orEmpty(),
                                markNumber = category._id.orEmpty()
                            )
                        )

                        Log.i("MapClick", "Added marker: $markLocations")
                    } else {
                        showToast("Failed to add marker on map")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    showToast("Error loading marker icon")
                }
            }



        }


    }


    private fun moveCameraToCurrentLocation() {
        val location = LatLng(lat.toDouble(), long.toDouble())
        Log.i("sdfdsfsdf", "moveCameraToCurrentLocation: $location")
        if (location != null){
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, 15f)
            mMap.animateCamera(cameraUpdate)
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

    private fun saveEmptyField(): Boolean{
        if (TextUtils.isEmpty(saveDialogBox.binding.tvMapName.text.toString().trim())) {
            showToast("Please enter map name")
            return false
        }
        if (TextUtils.isEmpty(saveDialogBox.binding.tvLocationName.text.toString().trim())) {
            showToast("Please enter location name")
            return false
        }
        if (TextUtils.isEmpty(saveDialogBox.binding.tvMaxDepth.text.toString().trim())) {
            showToast("Please enter max depth")
            return false
        }
        if (TextUtils.isEmpty(saveDialogBox.binding.tvAverageVisibility.text.toString().trim())) {
            showToast("Please enter average visibility")
            return false
        }
        if (TextUtils.isEmpty(saveDialogBox.binding.tvEntryType.text.toString().trim())) {
            showToast("Please enter entry type")
            return false
        }
        if (TextUtils.isEmpty(saveDialogBox.binding.tvEntryType.text.toString().trim())) {
            showToast("Please enter entry type")
            return false
        }
        if (TextUtils.isEmpty(saveDialogBox.binding.tvEntryType.text.toString().trim())) {
            showToast("Please enter entry type")
            return false
        }
        if (TextUtils.isEmpty(saveDialogBox.binding.tvLikeDislike.text.toString().trim())) {
            showToast("Please enter like dislike")
            return false
        }

        return true
    }


}



//    private fun isEmptyField(): Boolean {
//
//        if (TextUtils.isEmpty(binding.etTitle.text.toString().trim())) {
//            showToast("Please enter title")
//            return false
//        }
//        if (TextUtils.isEmpty(binding.etDescription.text.toString().trim())) {
//            showToast("Please enter description")
//            return false
//        }
//        if (TextUtils.isEmpty(binding.etLocation.text.toString().trim())) {
//            showToast("Please select address")
//            return false
//        }
//
//        return true
//    }

//    override fun onResume() {
//        super.onResume()
//        Log.i("fdsfsdf", "onCreateView: $lattitude ,$longitude , $address")
//        if (screen == "map"){
//            binding.etLocation.setText(address)
//        }
//        else{
//            binding.etLocation.setText("")
//        }
//        screen = "Dsada"
//
//
//    }
