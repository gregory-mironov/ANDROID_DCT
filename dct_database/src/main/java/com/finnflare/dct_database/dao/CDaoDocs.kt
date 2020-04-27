package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.dct_database.entity.CEntityDocs

@Dao
abstract class CDaoDocs:
    IBaseDao<CEntityDocs> {
    @Query("SELECT * FROM docs")
    abstract fun getAll(): List<CEntityDocs>

    @Query("DELETE FROM docs")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityDocs>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM docs WHERE _id = :aId")
    abstract fun findByGuid(aId: String): CEntityDocs
}