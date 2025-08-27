package com.tech.arch.data.model

import com.google.android.gms.maps.model.LatLng

data class Experience(
    val companyName: String, val startDate: String, val endDate: String
)

data class Education(
    val id: String, val educationName: String

)

data class JobTitle(
    val id: String, val title: String

)
data class FilterStatus(
    val id: String, val title: String

)

data class Account(
    val image : Int,
    var title : String
)
data class  EventData(
    val eventName : String, val hostName : String, val location : String, val eventDate : String
)
data class ReviewData(
    val name : String, val image : String, val location : String , val rating : String , val description : String
)
data class NotificationData(
    val image : String, val notification : String, val date : String
)
data class FavoriteModel(
    var image : String , var name : String, var workName : String, var location : String, var isSelected: Boolean = false
    )
data class SettingModel(
    val image : String, var name : String
)

data class ListingModel(
    val image : Int, val title : String
)

data class ProfileModel(
    val icon: Int?, val title: String?
)

data class BookmarkData(
    val title: String
)

data class SubscriptionData(
    val subscriptionStatus : String,
    val price : String,
    val subscriptionType : String,
    val description: String
)


data class MarkLocation(
    val lat: String,
    val lng: String,
    val name: String,
    val markNumber: String
)


data class SimpleMarkerData(
    val position: LatLng,
    val title: String,
    val imageUrl: String,
    val markNumber: String
)

data class LikeData(
    val text : String
)

data class PlaceDetails(val name: String, val address: String, val location: LatLng)

data class AccountType(
    val account : String
)
data class Certification(
    var certificate : String,
    var isSelected: Boolean = false
)


data class BookMarkDataaaa(
    var siteName : String,
    var location : String,
    var city : String,
    var miles : String,
    var  score : String,
    var state : String
)

