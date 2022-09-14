package com.sarbaevartur.wuwreader.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters

@Database(entities = [Book::class], version = 1)
@TypeConverters(BookConverters::class)
abstract class AppDatabase: RoomDatabase() {
    abstract fun bookDao(): BookDAO
}