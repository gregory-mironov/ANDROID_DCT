package com.finnflare.dct_database.dao

import androidx.room.*
import com.finnflare.dct_database.entity.CEntityGoods
import com.finnflare.dct_database.entity.CEntityLeftovers
import com.finnflare.dct_database.entity.CEntityMarkingCodes

@Dao
abstract class CDaoMain {

    @Transaction
    open fun appendItems(goods: List<CEntityGoods>,
                         leftovers: List<CEntityLeftovers>,
                         marking_codes: List<CEntityMarkingCodes>){
        insertGoods(goods)
        insertLeftovers(leftovers)
        insertMarkingCodes(marking_codes)
    }

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertGoods(aGood: List<CEntityGoods>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertLeftovers(aLeftover: List<CEntityLeftovers>)

    @Insert(onConflict = OnConflictStrategy.ABORT)
    abstract fun insertMarkingCodes(aMarkingCode: List<CEntityMarkingCodes>)
}