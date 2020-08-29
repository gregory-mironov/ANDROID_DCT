package com.finnflare.dct_database

import android.app.Application
import android.content.Context
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.finnflare.dct_database.entity.*
import com.finnflare.dct_database.files_format.actual_docs_state.DocsList
import com.finnflare.dct_database.files_format.actual_docs_state.Document
import com.finnflare.dct_database.insertable_classes.*
import com.finnflare.dct_database.request_result.CScanResult
import com.squareup.moshi.Moshi
import kotlinx.coroutines.*
import org.koin.core.KoinComponent
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@ObsoleteCoroutinesApi
class CDatabaseViewModel(application: Application): AndroidViewModel(application), KoinComponent {
    val dbDispatcher: CoroutineDispatcher = newSingleThreadContext("DBCoroutine")

    private val database = CAppDatabase.getInstance(application)

    private val dateFormat = SimpleDateFormat("HH-mm-ss_dd-MM-yyyy", Locale("RU"))

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

    fun clearMarkingCodes() {
        database.markingCodesDao().truncateTable()
    }

    fun insertMarkingCodes(mc: List<MarkingCode>) {
        val list = mutableListOf<CEntityMarkingCodes>()
        mc.forEach {
            list.add(CEntityMarkingCodes(
                mGuid = it.guid,
                mGtin = it.gtin,
                mRfid = it.rfid,
                mState = it.state,
                mSn = it.sn
            ))
        }

        database.markingCodesDao().insert(list)
    }

    fun clearGoods() {
        database.goodsDao().truncateTable()
    }

    fun insertGoods(goods: List<Good>) {
        val list = mutableListOf<CEntityGoods>()
        goods.forEach {
            list.add(CEntityGoods(
                mGuid = it.mGuid,
                mDescription = it.mName,
                mModel = it.mModel,
                mColor = it.mColor,
                mSize = it.mSize
            ))
        }

        database.goodsDao().insert(list)
    }

    fun clearStates() {
        database.goodsDao().truncateTable()
    }

    fun insertStates(goods: List<State>) {
        val list = mutableListOf<CEntityStates>()
        goods.forEach {
            list.add(CEntityStates(
                mState = it.stateGuid,
                mStateName = it.stateName
            ))
        }

        database.statesDao().insert(list)
    }

    fun clearPlanLeftovers(docId: String) {
        database.leftoversDao().clearPlanLeftovers(docId)
    }

    fun clearOldLeftovers() {
        database.leftoversDao().clearOldLeftovers()
    }

    fun insertLeftovers(leftovers: List<Leftover>) {
        val list = mutableListOf<CEntityLeftovers>()
        leftovers.forEach {
            list.add(CEntityLeftovers(
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

        database.leftoversDao().refillTable(list)
    }

    fun getLeftovers(documentId: String) = database.mainDao().getScanResultsForLists(documentId)

    fun getMCByGtin(gtin: String) = database.markingCodesDao().findByGtin(gtin)

    fun scanResultProcessing(docId: String, gtin: String, sn: String, rfid: String): Int {

        if (sn.isEmpty() && rfid.isEmpty()) {
            // EAN-13
            database.leftoversDao().findMyLine(gtin, sn, rfid)?.let {
                database.leftoversDao().incMyQtyoutEan_13(gtin)
                return 1
            }

            database.leftoversDao().findServerLine(gtin, sn, rfid)?.let{
                database.leftoversDao().insert(it.apply {
                    this.mQtyin = 0
                    this.mQtyout = 1
                })
                return 2
            }

            val mc = database.markingCodesDao().findByGtin(gtin)

            if (mc != null) {
                database.leftoversDao().insert(CEntityLeftovers(
                    mGuid = mc.mGuid,
                    mGtin = gtin,
                    mRfid = "",
                    mSn = "",
                    mState = mc.mState,
                    mQtyin = 0,
                    mQtyout = 1,
                    mDocGuid = docId,
                    mDocNumber = "",
                    mStoreId = ""
                ))

                return 3
            }

            database.leftoversDao().insert(CEntityLeftovers(
                mGuid = "",
                mGtin = gtin,
                mRfid = "",
                mSn = "",
                mState = "",
                mQtyin = 0,
                mQtyout = 1,
                mDocGuid = docId,
                mDocNumber = "",
                mStoreId = ""
            ))

            return 4
        }

        // DM, old and new RFID
        database.leftoversDao().findMyLine(gtin, sn, rfid)?.let { return -1 }

        database.leftoversDao().findServerLine(gtin, sn, rfid)?.let {
            database.leftoversDao().insert(it.apply {
                this.mQtyin = 0
                this.mQtyout = 1
            })
            return -2
        }

        val mc = database.markingCodesDao().findByGtin(gtin)
        if (mc != null) {
            database.leftoversDao().insert(CEntityLeftovers(
                mGuid = mc.mGuid,
                mGtin = gtin,
                mRfid = rfid,
                mSn = sn,
                mState = mc.mState,
                mQtyin = 0,
                mQtyout = 1,
                mDocGuid = docId,
                mDocNumber = "",
                mStoreId = ""
            ))

            return -3
        }

        database.leftoversDao().insert(CEntityLeftovers(
            mGuid = "",
            mGtin = gtin,
            mRfid = rfid,
            mSn = sn,
            mState = "",
            mQtyin = 0,
            mQtyout = 1,
            mDocGuid = docId,
            mDocNumber = "",
            mStoreId = ""
        ))

        return -4
    }

    fun getScanResults(storeId: String, documentId: String): List<CScanResult> {
        return database.mainDao().formScanResults(storeId, documentId)
    }

    fun getLocationsList() = database.shopsDao().getAll()

    fun getDocsList() = database.docsDao().getAll()

    fun getDatedDocsList(date: String) = database.docsDao().getDocsByDate(date)

    fun getDocInfo(docId: String) = database.docsDao().findById(docId)

    fun getRFIDScanResults(documentId: String): List<CEntityLeftovers> {
        return database.leftoversDao().getRFIDByDocId(documentId)
    }

    fun getBarcodeScanResults(documentId: String): List<CEntityLeftovers> {
        return database.leftoversDao().getBarcodeByDocId(documentId)
    }

    fun saveToFile(context: Context, docId: String = "") {
        val docs = mutableListOf<Document>()

        if (docId.isEmpty())
            getDocsList().forEach {
                docs.add(
                    Document(
                        doc = it,
                        rfidItemsList = getRFIDScanResults(it.mId),
                        barcodeItemsList = getBarcodeScanResults(it.mId)
                    )
                )
            }
        else
            docs.add(database.docsDao().findById(docId).let {
                Document(
                    doc = it,
                    rfidItemsList = getRFIDScanResults(it.mId),
                    barcodeItemsList = getBarcodeScanResults(it.mId)
                )
            })


        val result = Moshi.Builder().build().adapter(DocsList::class.java).toJson(
            DocsList(
                docs = docs
            )
        )

        val fileName = dateFormat.format(Date()) + ".json"
        val path = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath
            ?: return

        val file = File(path + File.separator + fileName)

        if (!file.createNewFile())
            return

        file.writeText(result)
    }

    fun uploadFromFile(file: File) {
        if (!file.name.endsWith(".json"))
            return

        try {
            val result = Moshi.Builder().build()
                .adapter(DocsList::class.java)
                .fromJson(file.readText())

            result!!.docs.forEach {
                database.mainDao().insertScanRes(it.doc, it.barcodeItemsList, it.rfidItemsList)
            }
        } catch (e: Exception) {
        }
    }

    fun deleteAllResults() {
        database.leftoversDao().deleteAllResults()
    }

    fun deleteBarcodeResults(docId: String) {
        database.leftoversDao().deleteBarcodeResults(docId)
    }

    fun deleteRfidResults(docId: String) {
        database.leftoversDao().deleteRfidResults(docId)
    }
}