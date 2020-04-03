package com.finnflare.android_dct.repository.database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "marking_codes",
    primaryKeys = arrayOf("_gtin", "_sn")
)
data class CEntityMarkingCodes (
    @ColumnInfo(name = "_guid")   @NonNull var mGuid: String,
    @ColumnInfo(name = "_gtin")            var mGtin: String,
    @ColumnInfo(name = "_rfid")            var mRfid: String,
    @ColumnInfo(name = "_sn")              var mSn: String,
    @ColumnInfo(name = "_state")           var mState: String
)