package com.ameya.livefront.userdirectory.domain.repository

import com.ameya.livefront.userdirectory.domain.model.User
import com.ameya.livefront.userdirectory.util.Resource
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getUserList(requestRemote: Boolean, query: String): Flow<Resource<List<User>>>
}