package com.finnflare.android_dct.repository.networkservice.dataclasses

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CStorageData(
    @Json(name = "_ID")
    val mID: String,
    @Json(name = "_DESCRIPTION")
    val MDescription: String
)

@JsonClass(generateAdapter = true)
data class CStoragesList(
    @Json(name = "STORAGES")
    val mLocations: List<CStorageData>
)