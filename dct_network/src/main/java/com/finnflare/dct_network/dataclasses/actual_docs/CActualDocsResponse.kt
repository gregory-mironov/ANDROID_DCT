package com.finnflare.dct_network.dataclasses.actual_docs


import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CActualDocsResponse(
    val header: Header,
    val request: Request,
    val response: Response
)