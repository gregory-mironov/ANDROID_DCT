package com.finnflare.dct_database.entity

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class CEntityUsers (
    @ColumnInfo(name = "_id")          @NonNull @PrimaryKey var mId: String,
    @ColumnInfo(name = "_description")                      var mDescription: String,
    @ColumnInfo(name = "_password")                         var mPassword: String,
    @ColumnInfo(name = "_last_login")                       var mLastLogin: String
)