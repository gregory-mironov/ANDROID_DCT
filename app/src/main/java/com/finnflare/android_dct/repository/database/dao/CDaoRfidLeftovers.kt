package com.finnflare.android_dct.repository.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.android_dct.repository.database.entity.CEntityRfidLeftovers

@Dao
abstract class CDaoRfidLeftovers:
    IBaseDao<CEntityRfidLeftovers> {
    @Query("SELECT * FROM rfid_leftovers")
    abstract fun getAll(): List<CEntityRfidLeftovers>

    @Query("DELETE FROM rfid_leftovers")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityRfidLeftovers>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM rfid_leftovers WHERE _rfid = :aRfid")
    abstract fun findByRfid(aRfid: String): CEntityRfidLeftovers

    @Query("SELECT * FROM rfid_leftovers WHERE _sn = :aSn")
    abstract fun findBySn(aSn: String): CEntityRfidLeftovers
}