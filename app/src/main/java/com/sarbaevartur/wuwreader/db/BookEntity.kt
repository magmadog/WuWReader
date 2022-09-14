package com.sarbaevartur.wuwreader.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value = ["book_name", "book_author"], unique = true)])
data class Book(
    @PrimaryKey(autoGenerate = true)        val id: Int = 0,
    @ColumnInfo(name = "book_name")         val title: String = "",
    @ColumnInfo(name = "book_author")       val author: String = "",
    @ColumnInfo(name = "book_path")         val path: String = "",
    @ColumnInfo(name = "last_page")         var lastPage: Int = 0,
    @ColumnInfo(name = "cover")             val cover: String = "",
    @ColumnInfo(name = "last_opened_date")  val lastOpenDate: Date
)