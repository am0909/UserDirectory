package com.ameya.livefront.userdirectory.data.repository

import android.util.Log
import com.ameya.livefront.userdirectory.data.local.UserDatabase
import com.ameya.livefront.userdirectory.data.mapper.toUser
import com.ameya.livefront.userdirectory.data.mapper.toUserEntity
import com.ameya.livefront.userdirectory.data.remote.UserAPI
import com.ameya.livefront.userdirectory.domain.model.User
import com.ameya.livefront.userdirectory.domain.repository.UserRepository
import com.ameya.livefront.userdirectory.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

/**
 * Concrete implementation of [UserRepository] interface.
 *
 * @param api: UserAPI - The API to fetch the user data.
 * @param db: UserDatabase - The database to store the user data.
 */
class UserRepositoryImpl @Inject constructor(
    private val api: UserAPI,
    db: UserDatabase
) : UserRepository {
    private val dao = db.dao

    override suspend fun getUserList(
        requestRemote: Boolean,
        query: String
    ): Flow<Result<List<User>>> {
        return flow {
            emit(Result.Loading)

            // Return the list of users from the local db first
            val localUserList = dao.searchUserList(query)

            val isDbEmpty = localUserList.isEmpty() && query.isBlank()
            // If db is empty emit loading state and proceed to fetching data from the remote api
            // else return the list of users
            if (isDbEmpty) {
                emit(Result.Loading)
            } else {
                emit(Result.Success(localUserList.map { it.toUser() }))
            }

            // Check if we need to fetch data from remote api
            val shouldFetchFromRemote = isDbEmpty || requestRemote
            if (shouldFetchFromRemote) {
                try {
                    val remoteUserList = api.getUserList().results
                    if (remoteUserList.isNotEmpty()) {
                        // Update the local db with the remote data
                        dao.deleteUserList()
                        dao.insertUserList(remoteUserList.map { it.toUserEntity() })

                        // Always return the list of users from the local db so as to maintain
                        // single source of truth
                        emit(Result.Success(dao.searchUserList("").map { it.toUser() }))
                    }
                } catch (e: IOException) {
                    Log.e(TAG, "${e.javaClass} ${e.localizedMessage}")
                    emit(Result.Error(e))
                } catch (e: HttpException) {
                    Log.e(TAG, "${e.javaClass} ${e.localizedMessage}")
                    emit(Result.Error(e))
                } catch (e: SocketTimeoutException) {
                    Log.e(TAG, "${e.javaClass} ${e.localizedMessage}")
                    emit(Result.Error(e))
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    companion object {
        const val TAG = "UserRepositoryImpl"
    }
}