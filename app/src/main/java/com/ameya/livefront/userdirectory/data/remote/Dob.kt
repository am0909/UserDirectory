package com.ameya.livefront.userdirectory.data.remote


import com.google.gson.annotations.SerializedName

data class Dob(
    @SerializedName("age")
    val age: Int,
    @SerializedName("date")
    val date: String
)