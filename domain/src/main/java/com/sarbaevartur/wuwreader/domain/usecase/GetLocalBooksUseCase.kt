package com.sarbaevartur.wuwreader.domain.usecase

import androidx.lifecycle.LiveData
import com.sarbaevartur.wuwreader.domain.model.Book
import com.sarbaevartur.wuwreader.domain.repository.BookRepository


class GetLocalBooksUseCase(val repository: BookRepository) {

    fun execute(): LiveData<List<Book>> {
        return repository.getAllBooks()
    }
}