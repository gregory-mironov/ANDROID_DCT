package com.finnflare.dct_database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "leftovers",
    primaryKeys = ["_guid", "_gtin", "_sn", "_rfid", "_state", "_doc_id"]
)
data class CEntityLeftovers (
    @ColumnInfo(name = "_guid")   @NonNull  var mGuid: String,
    @ColumnInfo(name = "_gtin")             var mGtin: String,
    @ColumnInfo(name = "_rfid")             var mRfid: String,
    @ColumnInfo(name = "_sn")               var mSn: String,
    @ColumnInfo(name = "_state")            var mState: String,
    @ColumnInfo(name = "_qtyin")            var mQtyin: Int = 0,
    @ColumnInfo(name = "_qtyout")           var mQtyout: Int = 0,
    @ColumnInfo(name = "_doc_id")           val mDocGuid: String,
    @ColumnInfo(name = "_doc_num")          val mDocNumber: String,
    @ColumnInfo(name = "_store_id")         val mStoreId: String
)