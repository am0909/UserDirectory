package com.ameya.livefront.userdirectory.data.repository

import com.ameya.livefront.userdirectory.data.local.UserDatabase
import com.ameya.livefront.userdirectory.data.mapper.toUser
import com.ameya.livefront.userdirectory.data.mapper.toUserEntity
import com.ameya.livefront.userdirectory.data.remote.UserAPI
import com.ameya.livefront.userdirectory.domain.model.User
import com.ameya.livefront.userdirectory.domain.repository.UserRepository
import com.ameya.livefront.userdirectory.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val api: UserAPI,
    db: UserDatabase
): UserRepository {
    private val dao = db.dao

    override suspend fun getUserList(
        requestRemote: Boolean,
        query: String
    ): Flow<Resource<List<User>>> {
        return flow {
            emit(Resource.Loading(true))

            // return the list of users from the local db first
            val localUserList = dao.searchUserList(query)
            emit(Resource.Success(localUserList.map { it.toUser() }))

            // check if we need to fetch data from remote api
            val isDbEmpty = localUserList.isEmpty() && query.isBlank()
            val shouldFetchFromRemote = isDbEmpty || requestRemote
            if (!shouldFetchFromRemote) {
                emit(Resource.Loading(false))
            } else {
                try {
                    val remoteUserList = api.getUserList().results
                    if (remoteUserList.isNotEmpty()) {
                        // update the local db with the remote data
                        dao.deleteUserList()
                        dao.insertUserList(remoteUserList.map { it.toUserEntity() })

                        // always return the list of users from the local db so as to maintain
                        // single source of truth
                        emit(Resource.Success(dao.searchUserList("").map { it.toUser() }))

                        emit(Resource.Loading(false))
                    }
                } catch (e: IOException) {
                    emit(Resource.Error(message = "Error loading users: ${e.localizedMessage}"))
                } catch (e: HttpException) {
                    emit(Resource.Error(message = "Error loading users: ${e.localizedMessage}"))
                } catch (e: SocketTimeoutException) {
                    emit(Resource.Error(message = "Error loading users: ${e.localizedMessage}"))
                }
            }
        }
    }
}