package com.finnflare.dct_database.insertable_classes

data class Doc (
    val mAuditor: String,
    val mBasis: String,
    val mComment: String,
    val mDocDate: String,
    val mDocNumber: String,
    val mDocSum: Double,
    val mId: String,
    val mPriceType: String,
    val mQty: Int,
    val mQtyFact: Int,
    val mStoreId: String
)