package com.finnflare.dct_database.dao

import androidx.room.*
import com.finnflare.dct_database.entity.CEntityDocs
import com.finnflare.dct_database.entity.CEntityGoods
import com.finnflare.dct_database.entity.CEntityLeftovers
import com.finnflare.dct_database.entity.CEntityMarkingCodes
import com.finnflare.dct_database.request_result.CLeftoverForList
import com.finnflare.dct_database.request_result.CScanResult

@Dao
abstract class CDaoMain {

    @Transaction
    open fun appendItems(goods: List<CEntityGoods>,
                         leftovers: List<CEntityLeftovers>,
                         marking_codes: List<CEntityMarkingCodes>){
        insertGoods(goods)
        insertLeftovers(leftovers)
        insertMarkingCodes(marking_codes)
    }

    @Transaction
    open fun insertScanRes(
        aDoc: CEntityDocs,
        aBarcodeRes: List<CEntityLeftovers>,
        aRfidRes: List<CEntityLeftovers>) {
        insertDoc(aDoc)
        insertLeftovers(aBarcodeRes)
        insertLeftovers(aRfidRes)
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertDoc(aDoc: CEntityDocs)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertGoods(aGood: List<CEntityGoods>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertLeftovers(aLeftover: List<CEntityLeftovers>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertMarkingCodes(aMarkingCode: List<CEntityMarkingCodes>)

    @Query("""
        SELECT
            lo._guid as guid,
            IFNULL(g._description, '') as description,
            g._model as model,
            g._color as color,
            g._size as size,
            IFNULL(st._state_name, '') as stateName,
            SUM(lo._qtyin) as qtyin,
            SUM(CASE WHEN lo._rfid = "" THEN lo._qtyout ELSE 0 END) as qtyoutBarcode,
            SUM(CASE WHEN lo._rfid != "" THEN lo._qtyout ELSE 0 END) as qtyoutRfid
        FROM
            leftovers lo
        LEFT JOIN
            goods g
        ON
            lo._guid = g._guid
        LEFT JOIN
            states st
        ON
            lo._state = st._state
        WHERE
            _doc_id = :aDocumentId
        GROUP BY
            lo._guid""")
    internal abstract fun getScanResultsForLists(aDocumentId: String): List<CLeftoverForList>

    @Query("""
        SELECT
            A._guid as guid,
            B._description as description,
            C._state_name as stateName,
            SUM(A._qtyin) as qtyin,
            SUM(CASE WHEN A._qtyin != 0 THEN A._qtyout ELSE 0 END) as qtyoutBarcode,
            SUM(CASE WHEN A._qtyin = 0 THEN A._qtyout ELSE 0 END) as qtyoutRfid
        FROM
            leftovers A
        LEFT JOIN
            goods B
        ON
            A._guid = B._guid
        LEFT JOIN
            states C
        ON
            A._state = C._state
        WHERE
            _store_id = :aStoreId AND _doc_id = :aDocumentId
        GROUP BY
            A._guid
        ORDER BY
            B._description""")
    internal abstract fun formScanResults(aStoreId: String, aDocumentId: String): List<CScanResult>
}