package com.sarbaevartur.wuwreader

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.sarbaevartur.wuwreader.db.Book
import com.sarbaevartur.wuwreader.db.BookRepository


class MainViewModel(application: Application) : AndroidViewModel(application) {

    private var mRepository: BookRepository = BookRepository(application)

    var mAllBooks: LiveData<List<Book>> = mRepository.getAllBooks()
    val mLastOpenedBook = mRepository.getLastOpenedBook()

    fun getAllBooks(): LiveData<List<Book>> {
        return mAllBooks
    }

    fun getLastOpenedBook(): LiveData<Book>{
        return mLastOpenedBook
    }

    fun insert(book: Book) {
        mRepository.insert(book)
    }

    fun pushToTop(book: Book){
        mRepository.pushToTop(book)
    }

    fun deleteAllData(){
        mRepository.deleteAllData()
    }
}