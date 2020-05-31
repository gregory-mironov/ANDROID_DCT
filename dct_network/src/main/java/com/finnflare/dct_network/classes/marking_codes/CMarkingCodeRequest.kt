package com.finnflare.dct_network.classes.marking_codes

import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CMarkingCodeRequest (
    val header: Header,
    val request: Request
)