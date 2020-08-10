package com.finnflare.dct_network.classes.leftovers


import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CLeftoversResponse(
    val header: Header,
    val request: Request,
    val response: Response
)