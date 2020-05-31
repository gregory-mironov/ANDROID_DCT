package com.finnflare.dct_network.classes.leftovers


import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CLeftoversRequest(
    val header: Header,
    val request: Request
)