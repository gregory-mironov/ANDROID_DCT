package com.finnflare.dct_network.classes.stocks

import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CStocksRequest (
    val header: Header,
    val request: Request
)