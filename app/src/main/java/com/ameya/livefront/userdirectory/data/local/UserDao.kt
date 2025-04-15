package com.ameya.livefront.userdirectory.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUserList(userListEntities: List<UserEntity>)

    @Query("DELETE FROM UserEntity")
    suspend fun deleteUserList()

    @Query(
        """
        SELECT * FROM userentity WHERE LOWER(first) LIKE '%' || LOWER(:query) || '%' OR LOWER(last) LIKE '%' || LOWER(:query) || '%'
    """
    )
    suspend fun searchUserList(query: String): List<UserEntity>
}