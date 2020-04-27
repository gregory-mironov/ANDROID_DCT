package com.finnflare.dct_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "states")
data class CEntityStates (
    @ColumnInfo(name = "_state")      @PrimaryKey var mState: String,
    @ColumnInfo(name = "_state_name")             var mStateName: String
)