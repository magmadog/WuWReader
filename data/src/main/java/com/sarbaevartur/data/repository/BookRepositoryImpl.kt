package com.sarbaevartur.data.repository

import com.sarbaevartur.data.storage.BookStorage
import com.sarbaevartur.domain.repository.BookRepository

class BookRepositoryImpl(private val bookStorage: BookStorage): BookRepository {


}