package com.tech.arch.data.model

import java.io.Serializable



/**************************  login & signup  api response*********************/

//data class SignUpApiResponse(
//    var _id: String?,
//    var email: String?,
//    var message: String?,
//    var success: Boolean?,
//    var token: String?
//)

//data class SignUpApiResponse(
//    var message: String?,
//    var success: Boolean?,
//    var userExists: UserExists?
//) {
//    data class UserExists(
//        var _id: String?,
//        var email: String?,
//        var token: String?
//    )
//}


data class SignUpApiResponse(
    var message: String?,
    var success: Boolean?,
    var userExists: UserExists?
) {
    data class UserExists(
        var _id: String?,
        var accountType: String?,
        var email: String?,
        var profileImage: String?,
        var token: String?
    )
}

/**************************  get profile*********************/
data class GetProfileApiResponse(
    var message: String?,
    var success: Boolean?,
    var userExists: UserExists?
) {
    data class UserExists(
        var __v: Int?,
        var _id: String?,
        var adsTimer: Int?,
        var countryCode: Any?,
        var createdAt: String?,
        var deviceToken: String?,
        var deviceType: Int?,
        var email: String?,
        var endDateOfPrimium: Any?,
        var fullName: String?,
        var gender: Any?,
        var isActive: Boolean?,
        var isDeleted: Boolean?,
        var isEmailVerified: Boolean?,
        var isFreeTrail: Boolean?,
        var isPhoneVerified: Boolean?,
        var isPremium: Boolean?,
        var jti: String?,
        var lastActive: Any?,
        var lat: String?,
        var lng: String?,
        var location: Location?,
        var phone: Any?,
        var profileImage: String?,
        var role: String?,
        var shareUrl: List<Any?>?,
        var socialId: Any?,
        var socialType: Any?,
        var startDateOfPrimium: Any?,
        var timeZone: Any?,
        var updatedAt: String?
    ) {
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        )
    }
}



//data class GetProfileApiResponse(
//    var message: String?,
//    var success: Boolean?,
//    var userExists: UserExists?
//) {
//    data class UserExists(
//        var __v: Int?,
//        var _id: String?,
//        var accountType: String?,
//        var adsTimer: Int?,
//        var businessDetails: BusinessDetails?,
//        var certifications: List<String?>?,
//        var countryCode: String?,
//        var createdAt: String?,
//        var currentLocation: CurrentLocation?,
//        var deviceToken: String?,
//        var deviceType: Int?,
//        var email: String?,
//        var endDateOfPrimium: Any?,
//        var fullName: String?,
//        var groupDetails: GroupDetails?,
//        var homeLocation: HomeLocation?,
//        var individualDetails: IndividualDetails?,
//        var isActive: Boolean?,
//        var isDeleted: Boolean?,
//        var isEmailVerified: Boolean?,
//        var isFreeTrail: Boolean?,
//        var isPhoneVerified: Boolean?,
//        var isPremium: Boolean?,
//        var jti: String?,
//        var lastActive: Any?,
//        var lat: String?,
//        var lng: String?,
//        var location: Location?,
//        var otp: String?,
//        var otpExpires: String?,
//        var password: String?,
//        var phone: String?,
//        var profileImage: String?,
//        var role: String?,
//        var shareUrl: List<Any?>?,
//        var socialId: Any?,
//        var socialType: Any?,
//        var startDateOfPrimium: Any?,
//        var timeZone: String?,
//        var updatedAt: String?
//    ) {
//        data class BusinessDetails(
//            var address: String?,
//            var businessName: String?,
//            var businessPhone: String?,
//            var businessRole: String?,
//            var businessType: Any?,
//            var hostedEvents: List<Any?>?,
//            var hoursOfOperation: String?,
//            var publicEmail: String?,
//            var servicesOffered: List<Any?>?,
//            var socialMedia: SocialMedia?,
//            var termsAccepted: Boolean?,
//            var website: String?
//        ) {
//            class SocialMedia
//        }
//
//        data class CurrentLocation(
//            var coordinates: List<Int?>?,
//            var type: String?
//        )
//
//        data class GroupDetails(
//            var groupContactEmail: String?,
//            var groupHostedEvents: List<Any?>?,
//            var groupMembersRoles: List<Any?>?,
//            var groupName: String?,
//            var groupSocialMedia: GroupSocialMedia?,
//            var groupType: String?,
//            var groupWebsite: String?,
//            var numberOfMembers: Int?,
//            var openToNewMembers: Boolean?,
//            var termsAccepted: Boolean?
//        ) {
//            class GroupSocialMedia
//        }
//
//        data class HomeLocation(
//            var coordinates: List<Int?>?,
//            var type: String?
//        )
//
//        data class IndividualDetails(
//            var diveHistory: String?,
//            var interestedArea: String?,
//            var mapInteraction: String?,
//            var termsAccepted: Boolean?
//        )
//
//        data class Location(
//            var coordinates: List<Double?>?,
//            var type: String?
//        )
//    }
//}
/**************************  forgot password api response *********************/
//data class ForgotPasswordApiResponse(
//    var _id: String?,
//    var email: String?,
//    var message: String?,
//    var otp: String?,
//    var success: Boolean?
//)

data class ForgotPasswordApiResponse(
    var message: String?,
    var success: Boolean?,
    var userExists: UserExists?
) {
    data class UserExists(
        var _id: String?,
        var email: String?,
        var otp: String?
    )
}


/**************************  get list api response *********************/
data class GetListApiResponse(
    var categories: List<Category?>?,
    var message: String?,
    var success: Boolean?
) {
    data class Category(
        var __v: Int?,
        var _id: String?,
        var createdAt: String?,
        var image: String?,
        var isDelete: Boolean?,
        var title: String?,
        var updatedAt: String?
    )
}

/**************************  create listing  api response *********************/
data class CreateListingApiResponse(
    var list: Category?,
    var message: String?,
    var success: Boolean?
) {
    data class Category(
        var __v: Int?,
        var _id: String?,
        var address: String?,
        var createdAt: String?,
        var description: String?,
        var isDelete: Boolean?,
        var lat: String?,
        var listCategoryId: String?,
        var lng: String?,
        var location: Location?,
        var title: String?,
        var updatedAt: String?,
        var userId: String?
    ) {
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        )
    }
}


/**************************  get all list   api response *********************/
data class GetAllListApiResponse(
    var lists: List<Lists?>?,
    var message: String?,
    var success: Boolean?
) {
    data class Lists(
        var __v: Int?,
        var _id: String?,
        var address: String?,
        var createdAt: String?,
        var description: String?,
        var isDelete: Boolean?,
        var lat: String?,
        var listCategoryId: ListCategoryId?,
        var lng: String?,
        var location: Location?,
        var title: String?,
        var updatedAt: String?,
        var userId: UserId?,
        var isSelect : Boolean = false
    ) {
        data class ListCategoryId(
            var _id: String?,
            var image: String?,
            var title: String?
        )

        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        )

        data class UserId(
            var _id: String?,
            var email: String?,
            var fullName: String?,
            var profileImage: String?
        )
    }
}



/**************************  get gio point    api response *********************/
data class GetGeoPointsResponse(
    var geoData: GeoData?,
    var message: String?,
    var success: Boolean?
) {
    data class GeoData(
        var __v: Int?,
        var _id: String?,
        var circlePoints: List<Any?>?,
        var createdAt: String?,
        var linePoints: List<Any?>?,
        var pinPoints: List<PinPoint?>?,
        var polygonPoints: List<List<PolygonPoint?>?>?,
        var rectanglePoints: List<Any?>?,
        var updatedAt: String?,
        var userId: String?
    ) {
        data class PinPoint(
            var _id: String?,
            var lat: Double?,
            var lng: Double?
        )

        data class PolygonPoint(
            var _id: String?,
            var lat: Double?,
            var lng: Double?
        )
    }
}



/**************************  get fill all map  api response *********************/


data class GetMapsApiResponse(
    var contributes: List<Contribute?>?,
    var message: String?,
    var pagination: Pagination?,
    var success: Boolean?
) {
    data class Contribute(
        var __v: Int?,
        var _id: String?,
        var address: String?,
        var aquaticLife: String?,
        var averageVisibility: String?,
        var bottomComposition: String?,
        var createdAt: String?,
        var disLikes: List<Any?>?,
        var entryType: String?,
        var image: String?,
        var isDelete: Boolean?,
        var isDeleted: Boolean?,
        var isDisliked: Boolean?,
        var isDive: Boolean?,
        var isLiked: Boolean?,
        var lat: String?,
        var likes: List<Any?>?,
        var lng: String?,
        var location: Location?,
        var marksLocations: List<MarksLocation?>?,
        var maxDepth: String?,
        var name: String?,
        var notes: String?,
        var status: Int?,
        var updatedAt: String?,
        var userId: String?
    ): Serializable {
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        ): Serializable

        data class MarksLocation(
            var _id: String?,
            var lat: String?,
            var lng: String?,
            var markNumber: MarkNumber?,
            var name: String?
        ) : Serializable{
            data class MarkNumber(
                var __v: Int?,
                var _id: String?,
                var createdAt: String?,
                var image: String?,
                var isDelete: Boolean?,
                var title: String?,
                var updatedAt: String?
            ): Serializable
        }
    }

    data class Pagination(
        var currentPage: Any?,
        var limit: String?,
        var total: Int?,
        var totalPages: Int?
    ): Serializable
}

/**************************  get book mark  api response *********************/
data class GetBookMarkApiResponse(
    var bookmark: List<Bookmark?>?,
    var message: String?,
    var success: Boolean?
) {
    data class Bookmark(
        var __v: Int?,
        var _id: String?,
        var contributeId: ContributeId?,
        var createdAt: String?,
        var isDelete: Boolean?,
        var updatedAt: String?,
        var userId: String?
    ) {
        data class ContributeId(
            var __v: Int?,
            var _id: String?,
            var address: String?,
            var aquaticLife: String?,
            var averageVisibility: String?,
            var bottomComposition: String?,
            var createdAt: String?,
            var disLikes: List<Any?>?,
            var entryType: String?,
            var image: String?,
            var isDelete: Boolean?,
            var isDeleted: Boolean?,
            var isDisliked: Boolean?,
            var isDive: Boolean?,
            var isLiked: Boolean?,
            var lat: String?,
            var likes: List<String?>?,
            var lng: String?,
            var location: Location?,
            var marksLocations: List<MarksLocation?>?,
            var maxDepth: String?,
            var name: String?,
            var notes: String?,
            var status: Int?,
            var updatedAt: String?,
            var userId: String?
        ) {
            data class Location(
                var coordinates: List<Double?>?,
                var type: String?
            )

            data class MarksLocation(
                var _id: String?,
                var lat: String?,
                var lng: String?,
                var markNumber: String?,
                var name: String?
            )
        }
    }
}

/************************** add contribution  api response *********************/
data class AddContributionApiResponse(
    var contribute: Contribute?,
    var message: String?,
    var success: Boolean?
) {
    data class Contribute(
        var __v: Int?,
        var _id: String?,
        var address: String?,
        var aquaticLife: String?,
        var averageVisibility: String?,
        var bottomComposition: String?,
        var createdAt: String?,
        var disLikes: List<Any?>?,
        var entryType: String?,
        var image: String?,
        var isDelete: Boolean?,
        var isDeleted: Boolean?,
        var isDive: Boolean?,
        var lat: String?,
        var likes: List<Any?>?,
        var lng: String?,
        var location: Location?,
        var marksLocations: List<MarksLocation?>?,
        var maxDepth: String?,
        var name: String?,
        var notes: String?,
        var status: Int?,
        var updatedAt: String?,
        var userId: String?
    ) {
        data class Location(
            var coordinates: List<Double?>?,
            var type: String?
        )

        data class MarksLocation(
            var _id: String?,
            var lat: String?,
            var lng: String?,
            var markNumber: String?,
            var name: String?
        )
    }
}