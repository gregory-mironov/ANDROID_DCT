package com.finnflare.dct_database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.finnflare.dct_database.entity.CEntityLeftovers

@Dao
abstract class CDaoLeftovers:
    IBaseDao<CEntityLeftovers> {
    @Query("SELECT * FROM leftovers")
    abstract fun getAll(): List<CEntityLeftovers>

    @Query("DELETE FROM leftovers")
    abstract fun truncateTable()

    @Transaction
    open fun refillTable(aObjList: List<CEntityLeftovers>) {
        truncateTable()
        insert(aObjList)
    }

    @Query("SELECT * FROM leftovers WHERE _sn = :aSn")
    abstract fun findBySn(aSn: String): CEntityLeftovers

    @Query("SELECT * FROM leftovers WHERE _guid = :aGuid")
    abstract fun findByGuid(aGuid: String): List<CEntityLeftovers>

    @Query("SELECT * FROM leftovers WHERE _rfid = :aRfid")
    abstract fun findByRfid(aRfid: String): CEntityLeftovers

    @Query("""UPDATE leftovers 
        SET _qtyout = _qtyout + 1 
        WHERE _gtin = :aGtin AND _sn = :aSn AND _rfid = :aRfid AND _qtyin = 0""")
    abstract fun incMyQtyoutEan_13(aGtin: String, aSn: String, aRfid: String)

    @Query("""SELECT * 
        FROM leftovers 
        WHERE _gtin = :aGtin AND _sn = :aSn AND _rfid = :aRfid AND _qtyin = 0""")
    abstract fun findMyLine(aGtin: String, aSn: String, aRfid: String): List<CEntityLeftovers>

    @Query("""SELECT * 
        FROM leftovers 
        WHERE _gtin = :aGtin AND _sn = :aSn AND _rfid = :aRfid AND _qtyin != 0""")
    abstract fun findServerLine(aGtin: String, aSn: String, aRfid: String): List<CEntityLeftovers>

    @Query("DELETE FROM leftovers WHERE _qtyin = 0 AND _rfid = '' ")
    abstract fun deleteAllMyBarcodeLines()

    @Query("DELETE FROM leftovers WHERE _qtyin = 0 AND _rfid != '' ")
    abstract fun deleteAllMyRfidLines()
}