package com.finnflare.dct_network.dataclasses.docs


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    val auditor: String,
    val basis: String,
    val comment: String,
    @Json(name = "DocDate")
    val docDate: String,
    @Json(name = "DocNumber")
    val docNumber: String,
    @Json(name = "DocSum")
    val docSum: Double,
    val id: String,
    @Json(name = "PriceType")
    val priceType: String,
    val qty: Int,
    val qtyFact: Int,
    @Json(name = "StoreId")
    val storeId: String
)