package com.finnflare.dct_network.classes.auth

import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CAuthRequest(
    val header: Header,
    val request: Request
)