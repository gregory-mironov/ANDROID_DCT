package com.finnflare.android_dct.repository.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.android_dct.repository.database.entity.CEntityLocations

@Dao
abstract class CDaoLocations:
    IBaseDao<CEntityLocations> {
    @Query("SELECT * FROM locations")
    abstract fun getAll(): List<CEntityLocations>

    @Query("DELETE FROM locations")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityLocations>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM locations WHERE _id = :aId")
    abstract fun findById(aId: String): CEntityLocations
}