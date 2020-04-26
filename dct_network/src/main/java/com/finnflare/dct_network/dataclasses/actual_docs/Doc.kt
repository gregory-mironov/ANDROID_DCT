package com.finnflare.dct_network.dataclasses.actual_docs


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Doc(
    val id: String,
    val itemsList: List<Items>,
    @Json(name = "storage_id")
    val storageId: String
)