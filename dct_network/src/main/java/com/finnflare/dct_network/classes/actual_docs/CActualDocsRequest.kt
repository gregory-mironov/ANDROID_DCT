package com.finnflare.dct_network.classes.actual_docs

import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CActualDocsRequest(
    val header: Header,
    val request: Request
)