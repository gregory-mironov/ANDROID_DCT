package com.finnflare.dct_database.dao

import androidx.room.*
import com.finnflare.dct_database.entity.*
import com.finnflare.dct_database.request_result.CLeftoverForList

@Dao
abstract class CDaoMain {

    @Transaction
    open fun insertScanRes(
        aDoc: CEntityDocs,
        aBarcodeRes: List<CEntityLeftovers>,
        aRfidRes: List<CEntityLeftovers>
    ) {
        insertDoc(aDoc)
        insertLeftovers(aBarcodeRes)
        insertLeftovers(aRfidRes)
    }

    @Transaction
    open fun insertNewShopInfo(
        aDocs: List<CEntityDocs>,
        aMC: List<CEntityMarkingCodes>,
        aGoods: List<CEntityGoods>,
        aStates: List<CEntityStates>
    ) {
        truncateDocsTable()
        truncateGoodsTable()
        truncateMCTable()
        truncateStatesTable()

        insertDocs(aDocs)
        insertMarkingCodes(aMC)
        insertGoods(aGoods)
        insertStates(aStates)

        clearOldLeftovers()
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertDoc(aDoc: CEntityDocs)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertDocs(aDoc: List<CEntityDocs>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertGoods(aGood: List<CEntityGoods>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertLeftovers(aLeftover: List<CEntityLeftovers>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertMarkingCodes(aMarkingCode: List<CEntityMarkingCodes>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertStates(aMarkingCode: List<CEntityStates>)

    @Query("DELETE FROM docs")
    abstract fun truncateDocsTable()

    @Query("DELETE FROM marking_codes")
    abstract fun truncateMCTable()

    @Query("DELETE FROM goods")
    abstract fun truncateGoodsTable()

    @Query("DELETE FROM states")
    abstract fun truncateStatesTable()

    @Query(
        """
        DELETE FROM leftovers
        WHERE _doc_id NOT IN (SELECT _id FROM docs)
    """
    )
    abstract fun clearOldLeftovers()

    @Query(
        """
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
            lo._guid"""
    )
    internal abstract fun getScanResultsForLists(aDocumentId: String): List<CLeftoverForList>
}