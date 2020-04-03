package com.finnflare.android_dct.repository.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.android_dct.repository.database.entity.CEntityUsers

@Dao
abstract class CDaoUsers:
    IBaseDao<CEntityUsers> {
    @Query("SELECT * FROM users")
    abstract fun getAll(): List<CEntityUsers>

    @Query("DELETE FROM users")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityUsers>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM users WHERE _id = :aId")
    abstract fun findById(aId: String): CEntityUsers
}