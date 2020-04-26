package com.finnflare.dct_network.dataclasses.docs


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CDocsResponse(
    val header: Header,
    val request: Request,
    val response: Response
)