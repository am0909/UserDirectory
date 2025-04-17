package com.ameya.livefront.userdirectory.data.mapper

import com.ameya.livefront.userdirectory.data.local.UserEntity
import com.ameya.livefront.userdirectory.data.remote.UserDto
import com.ameya.livefront.userdirectory.domain.model.Location
import com.ameya.livefront.userdirectory.domain.model.Street
import com.ameya.livefront.userdirectory.domain.model.User

/**
 * Extension function to convert [UserDto] to [UserEntity]. This is used to map the data from the remote
 * api and store it in the local db.
 */
fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        cell = cell,
        dob = dob,
        email = email,
        gender = gender,
        location = location,
        name = name,
        phone = phone,
        picture = picture
    )
}

/**
 * Extension function to convert [UserEntity] to [User]. This is used to map the data from the local db
 * to the domain model which is then used by the UI layer.
 */
fun UserEntity.toUser(): User {
    return User(
        id = id!!,
        cell = cell,
        age = dob.age,
        email = email,
        gender = gender,
        location = Location(
            city = location.city,
            street = Street(
                name = location.street.name,
                number = location.street.number
            ),
            state = location.state,
            country = location.country,
            postcode = location.postcode,
            timezone = location.timezone.offset
        ),
        first = name.first,
        last = name.last,
        phone = phone,
        large = picture.large,
        medium = picture.medium
    )
}