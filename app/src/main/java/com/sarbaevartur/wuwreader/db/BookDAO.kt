package com.sarbaevartur.wuwreader.db

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface BookDAO {

    @Query("SELECT * FROM book ORDER BY last_opened_date DESC")
    fun getAll(): LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE id IN (:userIDs)")
    fun loadAllByIDs(userIDs: IntArray): LiveData<List<Book>>

    @Query("SELECT * FROM book WHERE book_author LIKE :author AND book_name LIKE :title LIMIT 1")
    fun findByName(author: String, title: String): LiveData<Book>

    @Query("SELECT * FROM book ORDER BY last_opened_date DESC LIMIT 1")
    fun getLastBook(): LiveData<Book>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg books: Book)

    @Query("DELETE FROM book")
    fun nukeTable()

    @Query("UPDATE book SET last_opened_date=:date WHERE id = :id")
    fun pushToTop(date: Date, id: Int)

    @Delete
    fun delete(book: Book)

    @Update
    fun update(book: Book)
}