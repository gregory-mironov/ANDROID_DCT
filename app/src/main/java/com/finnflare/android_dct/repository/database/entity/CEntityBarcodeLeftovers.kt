package com.finnflare.android_dct.repository.database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "barcode_leftovers",
    primaryKeys = arrayOf("_guid", "_gtin", "_sn", "_state")
)
data class CEntityBarcodeLeftovers (
    @ColumnInfo(name = "_guid")   @NonNull var mGuid: String,
    @ColumnInfo(name = "_gtin")            var mGtin: String,
    @ColumnInfo(name = "_sn")              var mSn: String,
    @ColumnInfo(name = "_state")           var mState: String,
    @ColumnInfo(name = "_qtyin")           var mQtyin: Int = 0,
    @ColumnInfo(name = "_qtyout")          var mQtyout: Int = 0
)