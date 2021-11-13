package com.muhazharrasyad.consumerappaplikasigithubuserketiga.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Github(
    var username: String? = null,
    var name: String? = null,
    var avatar: String? = null,
    var favorite: Boolean = false,
) : Parcelable