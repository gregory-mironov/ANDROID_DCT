package com.finnflare.dct_network.classes.docs

import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CDocsRequest(
    val header: Header,
    val request: Request
)