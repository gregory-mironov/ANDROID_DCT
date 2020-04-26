package com.finnflare.dct_network.dataclasses.shops


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CShopsResponse(
    val header: Header,
    val request: Request,
    val response: Response
)