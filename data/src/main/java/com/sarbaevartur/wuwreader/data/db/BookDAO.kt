package com.sarbaevartur.wuwreader.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sarbaevartur.wuwreader.data.model.BookEntity
import java.util.*

@Dao
interface BookDAO {

    @Query("SELECT * FROM bookEntity ORDER BY last_opened_date DESC")
    fun getAll(): LiveData<List<BookEntity>>

    @Query("SELECT * FROM bookEntity WHERE id IN (:userIDs)")
    fun loadAllByIDs(userIDs: IntArray): LiveData<List<BookEntity>>

    @Query("SELECT * FROM bookEntity WHERE book_author LIKE :author AND book_name LIKE :title LIMIT 1")
    fun findByName(author: String, title: String): LiveData<BookEntity>

    @Query("SELECT * FROM bookEntity ORDER BY last_opened_date DESC LIMIT 1")
    fun getLastBook(): LiveData<BookEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertAll(bookEntity: BookEntity): Long

    @Query("DELETE FROM bookEntity")
    fun nukeTable()

    @Query("UPDATE bookEntity SET last_opened_date=:date WHERE id = :id")
    fun pushToTop(date: Date, id: Int)

    @Delete
    fun delete(bookEntity: BookEntity)

    @Update
    fun update(bookEntity: BookEntity)
}