package com.ameya.livefront.userdirectory.data.local

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ameya.livefront.userdirectory.data.remote.Dob
import com.ameya.livefront.userdirectory.data.remote.Location
import com.ameya.livefront.userdirectory.data.remote.Name
import com.ameya.livefront.userdirectory.data.remote.Picture

@Entity
data class UserEntity(
    val cell: String,
    @Embedded val dob: Dob,
    val email: String,
    val gender: String,
    @PrimaryKey val id: Long? = null,
    val location: Location,
    @Embedded val name: Name,
    val phone: String,
    @Embedded val picture: Picture
)
