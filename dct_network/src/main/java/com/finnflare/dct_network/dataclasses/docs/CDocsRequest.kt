package com.finnflare.dct_network.dataclasses.docs

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CDocsRequest(
    val header: Header,
    val request: Request
)