package com.finnflare.android_dct.repository.networkservice.dataclasses


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CLeftoverData(
    @Json(name = "_GTIN")
    val mGtin: String,
    @Json(name = "_GUID")
    val mGuid: String,
    @Json(name = "_QTYIN")
    val mQtyin: Int,
    @Json(name = "_RFID")
    val mRfid: Any,
    @Json(name = "_SN")
    val mSN: Any,
    @Json(name = "_STATE")
    val mState: String
)

@JsonClass(generateAdapter = true)
data class CLeftoversList(
    @Json(name = "LEFTOVERS")
    val mLeftovers: List<CLeftoverData>
)