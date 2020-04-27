package com.finnflare.dct_network.classes.stores


import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CStoresResponse(
    val header: Header,
    val request: Request,
    val response: Response
)