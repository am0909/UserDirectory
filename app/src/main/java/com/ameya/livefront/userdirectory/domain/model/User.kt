package com.ameya.livefront.userdirectory.domain.model

data class User(
    val cell: String,
    val age: Int,
    val email: String,
    val gender: String,
    val location: Location,
    val first: String,
    val last: String,
    val nat: String,
    val phone: String,
    val large: String,
    val thumbnail: String,
    val medium: String
)
