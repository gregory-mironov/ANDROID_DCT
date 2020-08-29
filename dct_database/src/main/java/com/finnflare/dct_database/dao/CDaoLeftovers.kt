package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.dct_database.entity.CEntityLeftovers
import com.finnflare.dct_database.request_result.CBarcodeScanResult
import com.finnflare.dct_database.request_result.CRFIDScanResult

@Dao
abstract class CDaoLeftovers:
    IBaseDao<CEntityLeftovers> {
    @Query("SELECT * FROM leftovers")
    abstract fun getAll(): List<CEntityLeftovers>

    @Query("SELECT * FROM leftovers WHERE _doc_id = :docId")
    abstract fun getAllByDocId(docId: String): List<CEntityLeftovers>

    @Query("SELECT * FROM leftovers WHERE _doc_id = :docId")
    abstract fun getBarcodeByDocId(docId: String): List<CEntityLeftovers>

    @Query("SELECT * FROM leftovers WHERE _doc_id = :docId")
    abstract fun getRFIDByDocId(docId: String): List<CEntityLeftovers>

    @Query("DELETE FROM leftovers")
    abstract fun truncateTable()

    @Query("""
        DELETE FROM leftovers
        WHERE _doc_id = :docId AND _qtyout = 0
    """)
    abstract fun clearPlanLeftovers(docId: String)

    @Query("""
        DELETE FROM leftovers
        WHERE _doc_id NOT IN (SELECT _id FROM docs)
    """)
    abstract fun clearOldLeftovers()

    @Transaction
    open fun refillTable(aObjList: List<CEntityLeftovers>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM leftovers WHERE _sn = :aSn")
    abstract fun findBySn(aSn: String): CEntityLeftovers

    @Query("SELECT * FROM leftovers WHERE _guid = :aGuid")
    abstract fun findByGuid(aGuid: String): List<CEntityLeftovers>

    @Query("SELECT * FROM leftovers WHERE _rfid = :aRfid")
    abstract fun findByRfid(aRfid: String): CEntityLeftovers

    @Query("""
        UPDATE leftovers 
        SET _qtyout = _qtyout + 1 
        WHERE _gtin = :aGtin AND _sn = '' AND _rfid = '' AND _qtyin = 0""")
    abstract fun incMyQtyoutEan_13(aGtin: String)

    @Query("""
        SELECT * 
        FROM leftovers 
        WHERE _gtin = :aGtin AND _sn = :aSn AND _rfid = :aRfid AND _qtyin = 0
        LIMIT 1
        """)
    abstract fun findMyLine(aGtin: String, aSn: String, aRfid: String): CEntityLeftovers?

    @Query("""
        SELECT * 
        FROM leftovers 
        WHERE _gtin = :aGtin AND _sn = :aSn AND _rfid = :aRfid AND _qtyin != 0 
        LIMIT 1""")
    abstract fun findServerLine(aGtin: String, aSn: String, aRfid: String): CEntityLeftovers?

    @Query("""
        SELECT 
            _guid as guid,
            _gtin as gtin,
            _sn as sn,
            _rfid as rfid,
            _state as state,
            _qtyout as qtyout
        FROM leftovers
        WHERE _store_id = :aStoreId AND _doc_id = :aDocumentId AND _qtyin = 0 AND _rfid != ''
    """)
    abstract fun getRFIDScanResults(aStoreId: String, aDocumentId: String): List<CRFIDScanResult>

    @Query("""
        SELECT
            _guid as guid,
            _gtin as gtin,
            _sn as sn,
            _state as state,
            _qtyout as qtyout
        FROM leftovers
        WHERE _store_id = :aStoreId AND _doc_id = :aDocumentId AND _qtyin = 0 AND _rfid == ''
    """)
    abstract fun getBarcodeScanResults(aStoreId: String, aDocumentId: String): List<CBarcodeScanResult>

    @Query("DELETE FROM leftovers WHERE _qtyin = 0")
    abstract fun deleteAllResults()

    @Query("DELETE FROM leftovers WHERE _qtyin = 0 AND _rfid = '' AND _doc_id = :docId")
    abstract fun deleteBarcodeResults(docId: String)

    @Query("DELETE FROM leftovers WHERE _qtyin = 0 AND _rfid != '' AND _doc_id = :docId")
    abstract fun deleteRfidResults(docId: String)
}