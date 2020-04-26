package com.finnflare.dct_network.classes.actual_docs


import com.finnflare.dct_network.classes.Header
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CActualDocsResponse(
    val header: Header,
    val request: Request,
    val response: Response
)