package com.finnflare.dct_network.classes.goods


import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CGoodsResponse(
    val header: Header,
    val request: Request,
    val response: Response
)