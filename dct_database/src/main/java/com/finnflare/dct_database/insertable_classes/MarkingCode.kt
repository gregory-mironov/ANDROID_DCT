package com.finnflare.dct_database.insertable_classes

data class MarkingCode (
    val gtin: String,
    val guid: String,
    val rfid: String,
    val sn: String,
    val state: String
)