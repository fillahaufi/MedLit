package com.fillahaufi.medlit.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fillahaufi.medlit.data.local.Medicine

@Database(entities = [
    Medicine::class
], version = 1)

abstract class MedRoomDatabase: RoomDatabase() {
    abstract fun medDao(): MedDao

    companion object {
        @Volatile
        private var INSTANCE: MedRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): MedRoomDatabase {
            if (INSTANCE == null) {
                synchronized(MedRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        MedRoomDatabase::class.java,
                        "user_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE as MedRoomDatabase
        }
    }
}