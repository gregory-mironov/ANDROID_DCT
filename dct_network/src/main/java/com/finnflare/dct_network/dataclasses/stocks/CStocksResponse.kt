package com.finnflare.dct_network.dataclasses.stocks


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CStocksResponse(
    val header: Header,
    val request: Request,
    val response: Response
)