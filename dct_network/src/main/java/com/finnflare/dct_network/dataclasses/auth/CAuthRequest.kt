package com.finnflare.dct_network.dataclasses.auth

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CAuthRequest(
    val header: Header,
    val request: Request
)