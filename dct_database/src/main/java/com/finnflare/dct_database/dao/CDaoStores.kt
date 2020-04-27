package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.dct_database.entity.CEntityStores

@Dao
abstract class CDaoStores:
    IBaseDao<CEntityStores> {
    @Query("SELECT * FROM storages")
    abstract fun getAll(): List<CEntityStores>

    @Query("DELETE FROM storages")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityStores>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM storages WHERE _id = :aId")
    abstract fun findById(aId: String): CEntityStores
}