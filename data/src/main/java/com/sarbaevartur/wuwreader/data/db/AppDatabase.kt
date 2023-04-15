package com.sarbaevartur.wuwreader.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.sarbaevartur.wuwreader.data.model.BookEntity

@Database(entities = [BookEntity::class], version = 1)
@TypeConverters(BookConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDAO
}