package com.finnflare.dct_network.classes.goods

import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CGoodsRequest (
    val header: Header,
    val request: Request
)