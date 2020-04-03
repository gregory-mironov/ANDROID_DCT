package com.finnflare.android_dct.repository.database.request_results

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.finnflare.android_dct.repository.database.dao.*
import com.finnflare.android_dct.repository.database.entity.*

@Database(
    entities = [
        CEntityUsers::class,
        CEntityLocations::class,
        CEntityStorages::class,
        CEntityGoods::class,
        CEntityBarcodeLeftovers::class,
        CEntityRfidLeftovers::class,
        CEntityMarkingCodes::class,
        CEntityStates::class
    ],
    version = 1
)
abstract class CAppDatabase: RoomDatabase() {
    abstract fun usersDao(): CDaoUsers
    abstract fun locationsDao(): CDaoLocations
    abstract fun storagesDao(): CDaoStorages
    abstract fun goodsDao(): CDaoGoods
    abstract fun barcodeLeftoversDao(): CDaoBarcodeLeftovers
    abstract fun rfidLeftoversDao(): CDaoRfidLeftovers
    abstract fun markingCodesDao(): CDaoMarkingCodes
    abstract fun statesDao(): CDaoStates
    abstract fun mainDao(): CDaoMain

    companion object {
        @Volatile private var INSTANCE: CAppDatabase? = null

        fun getInstance(context: Context): CAppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                CAppDatabase::class.java,
                "dct_database.db")
                .build()
    }
}