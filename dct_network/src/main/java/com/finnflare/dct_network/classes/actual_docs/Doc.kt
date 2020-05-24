package com.finnflare.dct_network.classes.actual_docs


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Doc(
    val id: String,
    val rfidItemsList: List<RFIDItems>,
    val barcodeItemsList: List<BarcodeItems>,
    @Json(name = "storage_id")
    val storeId: String
)