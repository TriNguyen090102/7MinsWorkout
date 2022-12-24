package com.example.a7minsworkout

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [HistoryEntity :: class], version = 1)
abstract class HistoryDatabase : RoomDatabase(){
    abstract fun historyDao() : HistoryDAO

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        fun getInstance(context: Context): HistoryDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context.applicationContext).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): HistoryDatabase {
            return Room.databaseBuilder(context.applicationContext, HistoryDatabase::class.java, "history_database")
                .build()
        }
    }



}