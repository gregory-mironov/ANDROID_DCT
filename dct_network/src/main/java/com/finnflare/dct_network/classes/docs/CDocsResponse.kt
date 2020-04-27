package com.finnflare.dct_network.classes.docs


import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CDocsResponse(
    val header: Header,
    val request: Request,
    val response: Response
)