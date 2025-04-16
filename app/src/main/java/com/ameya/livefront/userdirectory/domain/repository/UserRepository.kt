package com.ameya.livefront.userdirectory.domain.repository

import com.ameya.livefront.userdirectory.domain.model.User
import com.ameya.livefront.userdirectory.util.Result
import kotlinx.coroutines.flow.Flow

/**
 * Repository for User.
 */
interface UserRepository {
    /**
     * Function to fetch list of users from remote api and local db.
     */
    suspend fun getUserList(requestRemote: Boolean, query: String): Flow<Result<List<User>>>
}