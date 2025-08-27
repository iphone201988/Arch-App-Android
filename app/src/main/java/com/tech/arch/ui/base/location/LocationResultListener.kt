package com.tech.arch.ui.base.location
import android.location.Location

interface LocationResultListener {
    fun getLocation(location: Location)
}