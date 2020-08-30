package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import com.finnflare.dct_database.entity.CEntityMarkingCodes

@Dao
abstract class CDaoMarkingCodes:
    IBaseDao<CEntityMarkingCodes> {

    @Query("SELECT * FROM marking_codes WHERE _guid = :aGuid")
    abstract fun findByGuid(aGuid: String): List<CEntityMarkingCodes>

    @Query("SELECT * FROM marking_codes WHERE _gtin = :aGtin LIMIT 1")
    abstract fun findByGtin(aGtin: String): CEntityMarkingCodes?
}