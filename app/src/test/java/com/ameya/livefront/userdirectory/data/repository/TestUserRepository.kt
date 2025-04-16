package com.ameya.livefront.userdirectory.data.repository

import com.ameya.livefront.userdirectory.domain.model.User
import com.ameya.livefront.userdirectory.domain.repository.UserRepository
import com.ameya.livefront.userdirectory.util.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TestUserRepository: UserRepository {
    private val users = mutableListOf<User>()
    private var isLoading = false
    private var isError = false

    override suspend fun getUserList(
        requestRemote: Boolean,
        query: String
    ): Flow<Result<List<User>>> {
        return flow {
            if (isLoading) {
                emit(Result.Loading)
            } else if (isError) {
                emit(Result.Error(Exception("Error fetching users")))
            } else {
                if (query.isNotBlank()) {
                    val filteredUsers = users.filter { user ->
                        listOf(user.first, user.last).any { it.contains(query, ignoreCase = true) }
                    }
                    emit(Result.Success(filteredUsers))
                } else {
                    emit(Result.Success(users))
                }
            }
        }
    }

    fun addUsers(usersList: List<User>) {
        users.addAll(usersList)
    }

    fun setIsLoading(loading: Boolean) {
        isLoading = loading
    }

    fun setIsError(error: Boolean) {
        isError = error
    }
}