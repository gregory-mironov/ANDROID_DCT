package com.finnflare.dct_database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shops")
data class CEntityShops (
    @ColumnInfo(name = "_id")          @PrimaryKey   var mId: String,
    @ColumnInfo(name = "_description")               var mDescription: String,
    @ColumnInfo(name = "_http_ref")                  var mHttpRed: String
)