package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.dct_database.entity.CEntityDoc
import com.finnflare.dct_database.entity.CEntityGoods

@Dao
abstract class CDaoDocs:
    IBaseDao<CEntityDoc> {
    @Query("SELECT * FROM docs")
    abstract fun getAll(): List<CEntityDoc>

    @Query("DELETE FROM docs")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityDoc>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM docs WHERE _id = :aId")
    abstract fun findByGuid(aId: String): CEntityDoc
}