package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.dct_database.entity.CEntityShops

@Dao
abstract class CDaoShops : IBaseDao<CEntityShops> {
    @Query("SELECT * FROM shops")
    abstract fun getAll(): List<CEntityShops>

    @Query("DELETE FROM shops")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityShops>) {
        truncateTable()
        insert(aObjList)
    }
}