package com.ameya.livefront.userdirectory.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val city: String,
    val country: String,
    val postcode: String,
    val state: String,
    val street: Street,
    val timezone: String
) : Parcelable
