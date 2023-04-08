package com.sarbaevartur.wuwreader.domain.repository

import androidx.lifecycle.LiveData
import com.sarbaevartur.wuwreader.domain.model.Book

interface BookRepository {

    fun getAllBooks(): LiveData<List<Book>>

    fun getLastOpenedBook(): LiveData<Book>

    fun insert(book: Book)

    fun pushToTop(book: Book)

    fun deleteAllData()

    fun delete(book: Book)

    fun update(book: Book)
}