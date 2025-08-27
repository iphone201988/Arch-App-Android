package com.tech.arch.data.api

import java.util.TimeZone

object Constants {

     const val BASE_URL = "http://192.168.0.190:8080"
 //   const val BASE_URL = "https://arch-backend-5bh7.onrender.com/"
  //  const val BASE_URL_IMAGE = "https://arch-backend-5bh7.onrender.com"
     const val BASE_URL_IMAGE = "http://192.168.0.190:8080"

    var accountType :String ? = null

    var  notVisible : Boolean = false
    var mapContentNotVisible =  false
    var timeZone = TimeZone.getDefault().id.replace("Asia/Calcutta", "Asia/Kolkata")
    var deviceToken : String =""




    /**************** API LIST *****************/

    const val HEADER_API = "X-API-Key:lkcMuYllSgc3jsFi1gg896mtbPxIBzYkEL"
    const val LOGIN = "/api/v1/user/login"
    const val SIGNUP = "/api/v1/user/signup"
    const val SOCIAL_LOGIN = "/api/v1/user/socialLogin"
   const val GET_PROFILE = "/api/v1/user/userdata"
    const val CHANGE_PASSWORD = "/api/v1/user/updateUserData"
    const val LOGOUT = "/api/v1/user/logout"
    const val FORGOT_PASSWORD = "/api/v1/user/sendOtp"
    const val VERIFY_OTP = "/api/v1/user/verifyOtp"
    const val RESET_PASSWORD = "/api/v1/user/resetPassword"
    const val UPDATE_PROFILE = "/api/v1/user/updateUserData"
    const val DELETE_ACCOUNT = "/api/v1/user/deleteAccount"
    const val GET_LIST = "/api/v1/listCat/get"
    const val GET_MAPS = "/api/v1/contribute/findAll"
    const val LIKE_DISLIKE = "/api/v1/contribute/likeDislike/"
    const val SAVE_BOOKMARK = "/api/v1/bookmark/saveUnsave/"
    const val GET_BOOKMARK = "/api/v1/bookmark"
    const val GET_MARKERS = "/api/v1/listCat/get"
    const val DELETE_MAP = "/api/v1/contribute/me/"
    const val SAVE_MAP = "/api/v1/contribute/add"
    const val CREATE_LIST = "/api/v1/list/create"
    const val GET_ALL_LIST = "/api/v1/list/get"
    const val GROSPATIAL_DATA = "/api/v1/geospatalData/update"
    const val GET_GROSPATIAL_DATA = "/api/v1/geospatalData/get"






}