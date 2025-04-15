package com.ameya.livefront.userdirectory.data.remote


import com.google.gson.annotations.SerializedName

data class Id(
    @SerializedName("name")
    val name: String,
    @SerializedName("value")
    val value: Any
)