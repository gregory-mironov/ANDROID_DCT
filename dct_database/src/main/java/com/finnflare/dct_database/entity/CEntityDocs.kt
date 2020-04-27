package com.finnflare.dct_database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "docs")
data class CEntityDocs (
    @ColumnInfo(name = "_id")         @PrimaryKey   var mId: String,
    @ColumnInfo(name = "_number")                   var mNumber: String,
    @ColumnInfo(name = "_date")                     var mDate: String,
    @ColumnInfo(name = "_store_id")                 var mStoreId: String,
    @ColumnInfo(name = "_qty")                      var mQty: Int,
    @ColumnInfo(name = "_qty_fact")                 var mQtyFact: Int,
    @ColumnInfo(name = "_doc_sum")                  var mDocSum: Double,
    @ColumnInfo(name = "_auditor")                  var mAuditor: String,
    @ColumnInfo(name = "_price_type")               var mPriceType: String,
    @ColumnInfo(name = "_basis")                    var mBasis: String,
    @ColumnInfo(name = "_comment")                  var mComment: String
)