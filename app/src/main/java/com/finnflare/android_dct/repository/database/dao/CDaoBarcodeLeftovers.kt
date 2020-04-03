package com.finnflare.android_dct.repository.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.android_dct.repository.database.entity.CEntityBarcodeLeftovers

@Dao
abstract class CDaoBarcodeLeftovers:
    IBaseDao<CEntityBarcodeLeftovers> {
    @Query("SELECT * FROM barcode_leftovers")
    abstract fun getAll(): List<CEntityBarcodeLeftovers>

    @Query("DELETE FROM barcode_leftovers")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityBarcodeLeftovers>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM barcode_leftovers WHERE _sn = :aSn")
    abstract fun findBySn(aSn: String): CEntityBarcodeLeftovers

    @Query("SELECT * FROM barcode_leftovers WHERE _guid = :aGuid")
    abstract fun findByGuid(aGuid: String): List<CEntityBarcodeLeftovers>
}