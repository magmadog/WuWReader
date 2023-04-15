package com.sarbaevartur.wuwreader.domain.repository

import com.sarbaevartur.wuwreader.domain.model.Book
import kotlinx.coroutines.flow.Flow

interface BookRepository {

    fun getAllBooks(): Flow<List<Book>>

    fun getLastOpenedBook(): Flow<Book>

    fun insert(book: Book)

    fun pushToTop(book: Book)

    fun deleteAllData()

    fun delete(book: Book)

    fun update(book: Book)
}