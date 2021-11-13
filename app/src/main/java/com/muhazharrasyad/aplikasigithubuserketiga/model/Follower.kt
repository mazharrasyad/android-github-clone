package com.muhazharrasyad.aplikasigithubuserketiga.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Follower(
    var username: String? = null,
    var avatar: String? = null,
) : Parcelable