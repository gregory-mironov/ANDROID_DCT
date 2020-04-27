package com.finnflare.dct_database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "storages")
class CEntityStores (
    @ColumnInfo(name = "_id")          @NonNull @PrimaryKey var mId: String,
    @ColumnInfo(name = "_description")                      var mDescription: String
)