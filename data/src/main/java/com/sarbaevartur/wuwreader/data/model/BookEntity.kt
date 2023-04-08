package com.sarbaevartur.wuwreader.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import java.util.*

@Entity(indices = [Index(value = ["book_name", "book_author"], unique = true)])
data class BookEntity(
    @PrimaryKey(autoGenerate = true)        val id: Int = 0,
    @ColumnInfo(name = "book_name")         val title: String = "",
    @ColumnInfo(name = "book_author")       val author: String = "",
    @ColumnInfo(name = "book_path")         val path: String = "",
    @ColumnInfo(name = "last_page")         var lastPage: Int = 0,
    @ColumnInfo(name = "cover")             val cover: String = "",
    @ColumnInfo(name = "last_opened_date")  val lastOpenDate: Date = Date(),
    @ColumnInfo(name = "data_format")       val format: String = "PDF",
    @ColumnInfo(name = "pages")             val pages: Int = 100,
    @ColumnInfo(name = "file_size")         val size: Int = 2048,
)