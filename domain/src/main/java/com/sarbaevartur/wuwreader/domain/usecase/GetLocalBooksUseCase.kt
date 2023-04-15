package com.sarbaevartur.wuwreader.domain.usecase

import com.sarbaevartur.wuwreader.domain.model.Book
import com.sarbaevartur.wuwreader.domain.repository.BookRepository
import kotlinx.coroutines.flow.Flow


class GetLocalBooksUseCase(val repository: BookRepository) {

    fun execute(): Flow<List<Book>> {
        return repository.getAllBooks()
    }
}