package com.kuriamind.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [BlockEntity::class],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class KuriamindDatabase : RoomDatabase() {
    abstract fun blockDao(): BlockDao
}
