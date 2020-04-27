package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.dct_database.entity.CEntityLeftovers

@Dao
abstract class CDaoLeftovers:
    IBaseDao<CEntityLeftovers> {
    @Query("SELECT * FROM leftovers")
    abstract fun getAll(): List<CEntityLeftovers>

    @Query("DELETE FROM leftovers")
    abstract fun truncateTable()

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
}