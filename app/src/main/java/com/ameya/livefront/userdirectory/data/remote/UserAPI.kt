package com.ameya.livefront.userdirectory.data.remote

import retrofit2.http.GET

interface UserAPI {
    @GET("?results=500&seed=abc&exc=login,registered,nat,id")
    suspend fun getUserList(): UserResponseDto

    companion object {
        const val BASE_URL = "https://randomuser.me/api/"
    }
}