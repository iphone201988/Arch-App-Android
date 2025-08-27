package com.tech.arch.data.local

import android.content.SharedPreferences
import com.tech.arch.data.model.SignUpApiResponse


import com.google.gson.Gson
import javax.inject.Inject

class SharedPrefManager @Inject constructor(private val sharedPreferences: SharedPreferences) {

    object KEY {
        const val USER = "user"
        const val USER_ID = "user_id"
        const val BEARER_TOKEN = "bearer_token"
        const val PROFILE_COMPLETED = "profile_completed"
        const val APPEARANCE_KEY = "appearance_key"
        const val LOCALE = "locale_key"
        const val TODAY_RECORD = "today_record"
        const val TODAY = "today"
        const val ANS = "ans"
        const val IS_FIRST = "is_first"
        const val IS_FIRST_HOME = "is_first_home"
        const val IS_FIRST_ESTIMATE = "is_first_estimate"
        const val USER_TOKEN = "user_token"
    }


    fun saveUser(bean: SignUpApiResponse) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.USER, Gson().toJson(bean))
        editor.apply()
    }

    fun getCurrentUser(): SignUpApiResponse? {
        val s: String? = sharedPreferences.getString(KEY.USER, null)
        return Gson().fromJson(s, SignUpApiResponse::class.java)
    }

    fun saveToken(token: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY.USER_TOKEN, token)
        editor.apply()
    }

    fun getToken(): String? {
        val token: String? = sharedPreferences.getString(KEY.USER_TOKEN, null)
        return token
    }


    /* fun getToken(): String {
         return getCurrentUser()?.token?.let { token ->
             "Bearer $token"
         }.toString()
     }*/

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}