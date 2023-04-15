package com.sarbaevartur.wuwreader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.sarbaevartur.wuwreader.data.db.BookRepositoryImpl
import com.sarbaevartur.wuwreader.domain.model.Book
import com.sarbaevartur.wuwreader.domain.usecase.GetLocalBooksUseCase
import kotlinx.coroutines.flow.Flow

class MainViewModel (application: Application) : AndroidViewModel(application) {

    private var mRepository = BookRepositoryImpl(application)

    private val mLastOpenedBook: Flow<Book> = mRepository.getLastOpenedBook()

    fun getAllBooks(): Flow<List<Book>> {
        return GetLocalBooksUseCase(repository = mRepository).execute()
    }

    fun getLastOpenedBook(): Flow<Book> {
        return mRepository.getLastOpenedBook()
    }

    fun insert(book: Book) {
        mRepository.insert(book)
    }

    fun delete(book: Book){
        mRepository.delete(book)
    }

    fun pushToTop(book: Book){
        mRepository.pushToTop(book)
    }

    fun deleteAllData(){
        mRepository.deleteAllData()
    }

    fun update(book: Book){
        mRepository.update(book)
    }
}