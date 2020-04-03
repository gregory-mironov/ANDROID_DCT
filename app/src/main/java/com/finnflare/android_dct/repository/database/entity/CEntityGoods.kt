package com.finnflare.android_dct.repository.database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "goods")
data class CEntityGoods (
    @ColumnInfo(name = "_guid")        @NonNull @PrimaryKey var mGuid: String,
    @ColumnInfo(name = "_description")                      var mDescription: String,
    @ColumnInfo(name = "_model")                            var mModel: String,
    @ColumnInfo(name = "_color")                            var mColor: String,
    @ColumnInfo(name = "_size")                             var mSize: String,
    @ColumnInfo(name = "_qtyout")                           var mQtyout: Int = 0
)