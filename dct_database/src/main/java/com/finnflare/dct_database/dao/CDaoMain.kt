package com.finnflare.dct_database.dao

import androidx.room.*
import com.finnflare.dct_database.entity.CEntityGoods
import com.finnflare.dct_database.entity.CEntityLeftovers
import com.finnflare.dct_database.entity.CEntityMarkingCodes
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

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertGoods(aGood: List<CEntityGoods>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertLeftovers(aLeftover: List<CEntityLeftovers>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertMarkingCodes(aMarkingCode: List<CEntityMarkingCodes>)

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