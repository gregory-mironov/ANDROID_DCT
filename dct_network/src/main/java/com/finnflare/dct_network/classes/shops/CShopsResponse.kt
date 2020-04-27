package com.finnflare.dct_network.classes.shops


import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CShopsResponse(
    val header: Header,
    val request: Request,
    val response: Response
)