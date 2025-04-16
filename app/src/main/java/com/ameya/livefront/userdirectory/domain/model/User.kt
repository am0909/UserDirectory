package com.ameya.livefront.userdirectory.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Long,
    val cell: String,
    val age: Int,
    val email: String,
    val gender: String,
    val location: Location,
    val first: String,
    val last: String,
    val phone: String,
    val large: String,
    val medium: String
) : Parcelable
