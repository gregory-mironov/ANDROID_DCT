package com.finnflare.dct_network.classes.shops

import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CShopsRequest(
    val header: Header,
    val request: Request
)