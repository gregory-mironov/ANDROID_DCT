package com.finnflare.dct_database

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.finnflare.dct_database.entity.*
import com.finnflare.dct_database.insertable_classes.*
import com.finnflare.dct_database.request_result.CBarcodeScanResult
import com.finnflare.dct_database.request_result.CRFIDScanResult
import com.finnflare.dct_database.request_result.CScanResult
import kotlinx.coroutines.*
import org.koin.core.KoinComponent

@ObsoleteCoroutinesApi
class CDatabaseViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    val dbDispatcher: CoroutineDispatcher = newSingleThreadContext("DBCoroutine")

    private val database = CAppDatabase.getInstance(application)

    val authSuccessful = MutableLiveData<Boolean>()

    fun checkUser(login: String, password: String) {
        CoroutineScope(dbDispatcher).launch {
            authSuccessful.postValue(database.usersDao().checkUser(login, password))
        }
    }

    fun updateUserLastLogin(aId: String, aLogin: String, aPassword: String) {
        database.usersDao().updateUserLastLogin(aId, aLogin, aPassword)
    }

    fun insertUsers(users:List<User>) {
        val insertable = mutableListOf<CEntityUsers>()

        users.forEach {
            insertable.add(
                CEntityUsers(
                    mId = it.mId,
                    mLogin = it.mName,
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
        database.leftoversDao().truncateTable()
        database.markingCodesDao().truncateTable()
        database.mainDao().appendItems(insertable_g, insertable_lo, insertablr_mc)
    }

    fun getLeftovers(documentId: String) = database.mainDao().getScanResultsForLists(documentId)

    fun scanResultProcessing(gtin: String, sn: String, rfid: String) {
        if (sn.isEmpty() && rfid.isEmpty()) {
            // EAN-13
            if (database.leftoversDao().findMyLine(gtin, sn, rfid).isEmpty()) {
                val leftover = database.leftoversDao().findServerLine(gtin, sn, rfid)[0]
                leftover.mQtyin = 0
                leftover.mQtyout = 1
                database.leftoversDao().insert(leftover)
            } else {
                database.leftoversDao().incMyQtyoutEan_13(gtin, sn, rfid)
            }
        } else {
            // DM, old and new RFID
            if (database.leftoversDao().findMyLine(gtin, sn, rfid).isEmpty()) {
                val leftover = database.leftoversDao().findServerLine(gtin, sn, rfid)[0]
                leftover.mQtyin = 0
                leftover.mQtyout = 1
                database.leftoversDao().insert(leftover)
            }
        }
    }

    fun getScanResults(storeId: String, documentId: String): List<CScanResult> {
        return database.mainDao().formScanResults(storeId, documentId)
    }

    fun getLocationsList() = database.shopsDao().getAll()

    fun getDocsList() = database.docsDao().getAll()

    fun getDatedDocsList(date: String) = database.docsDao().getDocsByDate(date)

    fun getDocInfo(docId: String) = database.docsDao().findById(docId)

    fun getRFIDScanResults(storeId: String, documentId: String): List<CRFIDScanResult> {
        return database.leftoversDao().getRFIDScanResults(storeId, documentId)
    }

    fun getBarcodeScanResults(storeId: String, documentId: String): List<CBarcodeScanResult> {
        return database.leftoversDao().getBarcodeScanResults(storeId, documentId)
    }

    fun deleteAllMyBarcodeLines() {
        database.leftoversDao().deleteAllMyBarcodeLines()
    }

    fun deleteAllMyRfidLines() {
        database.leftoversDao().deleteAllMyRfidLines()
    }
}