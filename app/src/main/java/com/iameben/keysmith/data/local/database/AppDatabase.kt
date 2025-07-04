package com.iameben.keysmith.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.iameben.keysmith.data.local.dao.PasswordDao
import com.iameben.keysmith.data.local.entity.PasswordEntity

@Database(entities = [PasswordEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun passwordDao(): PasswordDao
}