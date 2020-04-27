package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.dct_database.entity.CEntityStates

@Dao
abstract class CDaoStates:
    IBaseDao<CEntityStates> {
    @Query("SELECT * FROM states")
    abstract fun getAll(): List<CEntityStates>

    @Query("DELETE FROM states")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityStates>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM states WHERE _state = :aState")
    abstract fun findByState(aState: String): CEntityStates
}