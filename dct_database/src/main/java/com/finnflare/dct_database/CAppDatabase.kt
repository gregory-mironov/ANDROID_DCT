package com.finnflare.dct_database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.finnflare.dct_database.dao.*
import com.finnflare.dct_database.entity.*

@Database(
    entities = [
        CEntityUsers::class,
        CEntityShops::class,
        CEntityStores::class,
        CEntityDocs::class,
        CEntityGoods::class,
        CEntityLeftovers::class,
        CEntityMarkingCodes::class,
        CEntityStates::class
    ],
    version = 1
)
internal abstract class CAppDatabase: RoomDatabase() {
    abstract fun usersDao(): CDaoUsers
    abstract fun shopsDao(): CDaoShops
    abstract fun storesDao(): CDaoStores
    abstract fun docsDao(): CDaoDocs
    abstract fun goodsDao(): CDaoGoods
    abstract fun leftoversDao(): CDaoLeftovers
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