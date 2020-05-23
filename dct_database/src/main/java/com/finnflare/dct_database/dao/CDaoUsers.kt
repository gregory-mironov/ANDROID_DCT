package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.dct_database.entity.CEntityUsers

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

    @Query("""
        SELECT COUNT(_login) > 0
        FROM users 
        WHERE 
            _login = :aLogin 
            AND 
            _password = :aPassword
            AND
            (JULIANDAY('now') - JULIANDAY(_last_login)) < 3
        """)
    abstract fun checkUser(aLogin: String, aPassword: String): Boolean

    @Query("""
        INSERT OR REPLACE INTO users(_id, _login, _password, _last_login)
        VALUES (:aId, :aLogin, :aPassword, date('now'))
    """)
    abstract fun updateUserLastLogin(aId: String, aLogin: String, aPassword: String)

    @Query("SELECT * FROM users WHERE _login = :aLogin")
    abstract fun findById(aLogin: String): CEntityUsers
}