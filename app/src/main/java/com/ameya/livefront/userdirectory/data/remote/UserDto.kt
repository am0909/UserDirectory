package com.ameya.livefront.userdirectory.data.remote


import com.google.gson.annotations.SerializedName

data class UserDto(
    @SerializedName("cell")
    val cell: String,
    @SerializedName("dob")
    val dob: Dob,
    @SerializedName("email")
    val email: String,
    @SerializedName("gender")
    val gender: String,
    @SerializedName("id")
    val id: Id,
    @SerializedName("location")
    val location: Location,
    @SerializedName("name")
    val name: Name,
    @SerializedName("nat")
    val nat: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("picture")
    val picture: Picture,
    @SerializedName("registered")
    val registered: Registered
)