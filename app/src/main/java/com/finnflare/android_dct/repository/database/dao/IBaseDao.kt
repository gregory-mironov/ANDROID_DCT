package com.finnflare.android_dct.repository.database.dao

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update

interface IBaseDao<T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(aObjList: List<T>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg aObj: T)

    @Delete
    fun delete(aObjList: List<T>)

    @Delete
    fun delete(vararg aObj: T)

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(aObjList: List<T>)

    @Update(onConflict = OnConflictStrategy.ABORT)
    fun update(vararg aObj: T)
}