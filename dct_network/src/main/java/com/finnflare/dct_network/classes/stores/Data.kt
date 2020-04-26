package com.finnflare.dct_network.classes.stores


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Data(
    @Json(name = "Id")
    val id: String,
    @Json(name = "Name")
    val name: String,
    @Json(name = "ShopId")
    val shopId: String,
    @Json(name = "StoreType")
    val storeType: String?
)