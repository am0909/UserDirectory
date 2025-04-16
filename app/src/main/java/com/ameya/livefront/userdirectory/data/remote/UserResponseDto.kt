package com.ameya.livefront.userdirectory.data.remote

import com.google.gson.annotations.SerializedName

/**
 * Data class representing the response from the user API.
 */
data class UserResponseDto(
    @SerializedName("info")
    val info: Info,
    @SerializedName("results")
    val results: List<UserDto>
)