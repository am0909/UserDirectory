package com.ameya.livefront.userdirectory.domain.model

import com.ameya.livefront.userdirectory.data.remote.Street

data class Location(
    val city: String,
    val country: String,
    val postcode: String,
    val state: String,
    val street: Street,
)
