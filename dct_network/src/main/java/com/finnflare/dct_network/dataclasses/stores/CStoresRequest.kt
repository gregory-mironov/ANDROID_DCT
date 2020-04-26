package com.finnflare.dct_network.dataclasses.stores

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CStoresRequest(
    val header: Header,
    val request: Request
)