package com.finnflare.dct_network.dataclasses.users

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CUsersRequest(
    val header: Header,
    val request: Request
)