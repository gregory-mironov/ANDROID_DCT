package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.dct_database.entity.CEntityMarkingCodes

@Dao
abstract class CDaoMarkingCodes:
    IBaseDao<CEntityMarkingCodes> {
    @Query("SELECT * FROM marking_codes")
    abstract fun getAll(): List<CEntityMarkingCodes>

    @Query("DELETE FROM marking_codes")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityMarkingCodes>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM marking_codes WHERE _guid = :aGuid")
    abstract fun findByGuid(aGuid: String): List<CEntityMarkingCodes>

    @Query("SELECT * FROM marking_codes WHERE _rfid = :aRfid")
    abstract fun findByRfid(aRfid: String): List<CEntityMarkingCodes>

    @Query("SELECT * FROM marking_codes WHERE _sn = :aSn")
    abstract fun findBySn(aSn: String): List<CEntityMarkingCodes>
}