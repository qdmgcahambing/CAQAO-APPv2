package com.example.caqao.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [RegisterEntity::class], version = 1, exportSchema = true)
abstract class RegisterDatabase : RoomDatabase() {

    abstract fun getRegisterDatabaseDao(): RegisterDatabaseDao

    companion object {

        @Volatile
        private var instance : RegisterDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also{
                instance = it
            }
        }

        private fun buildDatabase(context: Context) = Room.databaseBuilder(
            context.applicationContext,
            RegisterDatabase::class.java,
            "user-databasee"
        ).build()
    }
}