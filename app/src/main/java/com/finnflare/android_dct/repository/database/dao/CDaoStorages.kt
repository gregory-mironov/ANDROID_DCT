package com.finnflare.android_dct.repository.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.android_dct.repository.database.entity.CEntityStorages

@Dao
abstract class CDaoStorages:
    IBaseDao<CEntityStorages> {
    @Query("SELECT * FROM storages")
    abstract fun getAll(): List<CEntityStorages>

    @Query("DELETE FROM storages")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityStorages>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM storages WHERE _id = :aId")
    abstract fun findById(aId: String): CEntityStorages
}