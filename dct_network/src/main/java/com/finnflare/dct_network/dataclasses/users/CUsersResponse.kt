package com.finnflare.dct_network.dataclasses.users


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CUsersResponse(
    val header: Header,
    val request: Request,
    val response: Response
)