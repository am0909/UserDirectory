package com.ameya.livefront.userdirectory.data.mapper

import com.ameya.livefront.userdirectory.data.local.UserEntity
import com.ameya.livefront.userdirectory.data.remote.UserDto
import com.ameya.livefront.userdirectory.domain.model.Location
import com.ameya.livefront.userdirectory.domain.model.User

fun UserDto.toUserEntity(): UserEntity {
    return UserEntity(
        cell = cell,
        dob = dob,
        email = email,
        gender = gender,
        location = location,
        name = name,
        nat = nat,
        phone = phone,
        picture = picture
    )
}

fun UserEntity.toUser(): User {
    return User(
        cell = cell,
        age = dob.age,
        email = email,
        gender = gender,
        location = Location(
            city = location.city,
            street = location.street,
            state = location.state,
            country = location.country,
            postcode = location.postcode
        ),
        first = name.first,
        last = name.last,
        nat = nat,
        phone = phone,
        large = picture.large,
        thumbnail = picture.thumbnail,
        medium = picture.medium
    )
}