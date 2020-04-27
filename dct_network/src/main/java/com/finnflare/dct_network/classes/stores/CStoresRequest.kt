package com.finnflare.dct_network.classes.stores

import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CStoresRequest(
    val header: Header,
    val request: Request
)