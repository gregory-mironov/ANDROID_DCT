package com.finnflare.dct_network.dataclasses.stocks

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CStocksRequest (
    val header: Header,
    val request: Request
)