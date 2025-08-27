package com.tech.arch.ui.auth.create_account

import android.net.Uri
import okhttp3.MultipartBody

object RegistrationDataHolder {
    var profileImage: Uri? = null
    var email : String ? = null
    var password : String ? = null
    var fullName : String ? = null
    var accountType : String ? = null
    var phone : String ? = null
    var countryCode : String ? = null
    var lat : String ? = null
    var long : String ? = null
    var location : String = ""

    // individual user
    var diveHistory : String ? = null
    var interestedArea : String ? = null
    var mapInteraction : String ? = null
    var termsAccepted : Boolean = false
//    var certifications: List<String> = emptyList()
    var certifications : String = ""

    //  group user
    var groupName : String ? = null
    var groupContactEmail  : String ? = null
    var groupType : String ? = null
    var groupHostedEvent : String ? = null
    var groupMemberRoles : String ? = null
    var groupWebsite : String ? = null
    var groupSocialMedia : String  ? = null
    var openToNewMembers : Boolean = false
    var numberOfMembers : Int ? = null



    // Business group

    var businessName : String ? = null
    var businessType : String ? = null
    var businessPhone : String ? = null
    var publicEmail : String ? = null
    var website : String ? = null
    var servicesOffered : String= ""
    var address : String ? =null
    var hoursOfOperation : String ? = null
    var hostedEvents : String = ""
    var businessRole : String ?= null
    var businessSocialMedia : String ? = null


    fun logData(): String {
        return """
            profileImage: $profileImage
            email: $email
            password: $password
            fullName: $fullName
            accountType: $accountType
            phone: $phone
            countryCode: $countryCode
            lat: $lat
            long: $long
            diveHistory: $diveHistory
            interestedArea: $interestedArea
            mapInteraction: $mapInteraction
            termsAccepted: $termsAccepted
            certifications: $certifications
        """.trimIndent()
    }

}