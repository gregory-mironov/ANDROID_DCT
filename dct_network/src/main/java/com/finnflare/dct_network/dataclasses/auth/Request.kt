package com.finnflare.dct_network.dataclasses.auth


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Request(
    @Json(name = "Login")
    val login: String,
    @Json(name = "Password")
    val password: String
)