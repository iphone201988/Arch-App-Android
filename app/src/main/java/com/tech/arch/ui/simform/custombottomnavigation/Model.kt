package com.tech.arch.ui.simform.custombottomnavigation

import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import com.tech.arch.R

data class Model(
    @DrawableRes
    val icon: Int = 0,
    @IdRes
    val destinationId: Int = -1,
    val id: Int = -1,
    @StringRes
    var count: Int = R.string.empty_value
)