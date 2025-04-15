package com.ameya.livefront.userdirectory.data.remote


import com.google.gson.annotations.SerializedName

data class UserResponseDto(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<UserDto>
)