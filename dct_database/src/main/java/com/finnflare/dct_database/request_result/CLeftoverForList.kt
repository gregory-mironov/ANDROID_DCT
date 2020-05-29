package com.finnflare.dct_database.request_result

data class CLeftoverForList (
    var guid: String,
    var description: String,
    var model: String,
    var color: String,
    var size: String,
    var stateName: String,
    var qtyin: Int,
    var qtyoutBarcode: Int,
    var qtyoutRfid: Int
)