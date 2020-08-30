package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import com.finnflare.dct_database.entity.CEntityDocs

@Dao
abstract class CDaoDocs:
    IBaseDao<CEntityDocs> {
    @Query("SELECT * FROM docs")
    abstract fun getAll(): List<CEntityDocs>

    @Query("""
        SELECT * 
        FROM docs
        WHERE 
            JULIANDAY(_date) - JULIANDAY(:aDate) >= 0
            AND
            JULIANDAY(_date) - JULIANDAY(:aDate) <= 1
        """)
    abstract fun getDocsByDate(aDate: String): List<CEntityDocs>

    @Query("DELETE FROM docs")
    abstract fun truncateTable()

    @Query("SELECT * FROM docs WHERE _id = :aId LIMIT 1")
    abstract fun findById(aId: String): CEntityDocs
}