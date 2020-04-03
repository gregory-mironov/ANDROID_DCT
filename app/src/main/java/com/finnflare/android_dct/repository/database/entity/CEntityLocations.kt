package com.finnflare.android_dct.repository.database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class CEntityLocations (
    @ColumnInfo(name = "_id")          @NonNull @PrimaryKey var mId: String,
    @ColumnInfo(name = "_description")                      var mDescription: String
)