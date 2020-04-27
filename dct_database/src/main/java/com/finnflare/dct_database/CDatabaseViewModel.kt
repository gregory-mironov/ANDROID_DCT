package com.finnflare.dct_database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.finnflare.dct_database.entity.*
import com.finnflare.dct_database.insertable_classes.*
import org.koin.core.KoinComponent

class CDatabaseViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    private val database = CAppDatabase.getInstance(application)

    fun insertUsers(users:List<User>) {
        val insertable = mutableListOf<CEntityUsers>()

        users.forEach {
            insertable.add(
                CEntityUsers(
                    mId = it.mId,
                    mDescription = it.mName,
                    mPassword = "",
                    mLastLogin = ""
                )
            )
        }

        database.usersDao().truncateTable()
        database.usersDao().insert(insertable)
    }

    fun insertShops(shops: List<Shop>) {
        val insertable = mutableListOf<CEntityShops>()

        shops.forEach {
            insertable.add(CEntityShops(
                mId = it.mId,
                mDescription = it.mName,
                mHttpRed = it.mHttpRef
            ))
        }

        database.shopsDao().truncateTable()
        database.shopsDao().insert(insertable)
    }

    fun insertStores(stores: List<Store>) {
        val insertable = mutableListOf<CEntityStores>()

        stores.forEach {
            insertable.add(CEntityStores(
                mId = it.mId,
                mDescription = it.mName
            ))
        }

        database.storesDao().truncateTable()
        database.storesDao().insert(insertable)
    }

    fun insertDocs(docs: List<Doc>) {
        val insertable = mutableListOf<CEntityDocs>()

        docs.forEach {
            insertable.add(CEntityDocs(
                mId = it.mId,
                mNumber = it.mDocNumber,
                mDate = it.mDocDate,
                mStoreId = it.mStoreId,
                mQty = it.mQty,
                mQtyFact = it.mQtyFact,
                mDocSum = it.mDocSum,
                mAuditor = it.mAuditor,
                mPriceType = it.mPriceType,
                mBasis = it.mBasis,
                mComment = it.mComment
            ))

            database.docsDao().truncateTable()
            database.docsDao().insert(insertable)
        }
    }

    fun insertStocks(goods: List<Good>, leftovers: List<Leftover>, mc: List<MarkingCode>) {
        val insertable_g = mutableListOf<CEntityGoods>()
        goods.forEach {
            insertable_g.add(CEntityGoods(
                mGuid = it.mGuid,
                mDescription = it.mName,
                mModel = it.mModel,
                mColor = it.mColor,
                mSize = it.mSize
            ))
        }

        val insertable_lo = mutableListOf<CEntityLeftovers>()
        leftovers.forEach {
            insertable_lo.add(CEntityLeftovers(
                mDocGuid = it.mDocGuid,
                mDocNumber = it.mDocNumber,
                mGuid = it.mGuid,
                mGtin = it.mGtin,
                mRfid = it.mRfid,
                mSn = it.mSn,
                mStoreId = it.mStoreGuid,
                mState = it.mState,
                mQtyin = it.mQtyin,
                mQtyout = 0
            ))
        }

        val insertablr_mc = mutableListOf<CEntityMarkingCodes>()
        mc.forEach {
            insertablr_mc.add(CEntityMarkingCodes(
                mGuid = it.guid,
                mGtin = it.gtin,
                mRfid = it.rfid,
                mState = it.state,
                mSn = it.sn
            ))
        }
        database.goodsDao().truncateTable()
        database.barcodeLeftoversDao().truncateTable()
        database.markingCodesDao().truncateTable()
        database.mainDao().appendItems(insertable_g, insertable_lo, insertablr_mc)
    }
}