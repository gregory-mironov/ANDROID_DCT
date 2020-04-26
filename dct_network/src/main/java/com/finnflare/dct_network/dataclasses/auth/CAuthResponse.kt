package com.finnflare.dct_network.dataclasses.auth


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CAuthResponse(
    val header: Header,
    val request: Request,
    val response: Response
)