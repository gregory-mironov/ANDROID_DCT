package com.finnflare.dct_database.files_format.actual_docs_state

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class DocsList(
    @Json(name = "Docs")
    val docs: List<Document>
)