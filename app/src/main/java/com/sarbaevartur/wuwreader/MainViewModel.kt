package com.sarbaevartur.wuwreader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sarbaevartur.wuwreader.data.db.BookRepositoryImpl
import com.sarbaevartur.wuwreader.domain.model.Book
import com.sarbaevartur.wuwreader.domain.usecase.GetLocalBooksUseCase

class MainViewModel (application: Application) : AndroidViewModel(application) {

    private var mRepository = BookRepositoryImpl(application)

    private val mLastOpenedBook: LiveData<Book> = mRepository.getLastOpenedBook()

    fun getAllBooks(): LiveData<List<Book>> {
        return GetLocalBooksUseCase(mRepository).execute()
    }

    fun getLastOpenedBook(): LiveData<Book> {
        return mLastOpenedBook
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