package com.finnflare.android_dct.repository.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.android_dct.repository.database.entity.CEntityGoods

@Dao
abstract class CDaoGoods:
    IBaseDao<CEntityGoods> {
    @Query("SELECT * FROM goods")
    abstract fun getAll(): List<CEntityGoods>

    @Query("DELETE FROM goods")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityGoods>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM goods WHERE _guid = :aGuid")
    abstract fun findByGuid(aGuid: String): CEntityGoods
}