package com.finnflare.dct_database.files_format.actual_docs_state


import com.finnflare.dct_database.entity.CEntityDocs
import com.finnflare.dct_database.entity.CEntityLeftovers
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Document(
    val doc: CEntityDocs,
    val barcodeItemsList: List<CEntityLeftovers>,
    val rfidItemsList: List<CEntityLeftovers>
)