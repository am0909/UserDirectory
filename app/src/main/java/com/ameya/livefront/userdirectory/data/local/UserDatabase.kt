package com.ameya.livefront.userdirectory.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [UserEntity::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class UserDatabase : RoomDatabase() {
    abstract val dao: UserDao
}