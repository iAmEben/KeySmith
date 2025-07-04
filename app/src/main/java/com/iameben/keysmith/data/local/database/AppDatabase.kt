package com.iameben.keysmith.data.local.database

import androidx.room.Database
import com.iameben.keysmith.data.local.dao.PasswordDao
import com.iameben.keysmith.data.local.entity.PasswordEntity

@Database(entities = [PasswordEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase {
    abstract fun passwordDao(): PasswordDao
}