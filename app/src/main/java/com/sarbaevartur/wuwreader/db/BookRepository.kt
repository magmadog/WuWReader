package com.sarbaevartur.wuwreader.db

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


class BookRepository(application: Application) {

    val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "database-name"
    ).build()

    var executor: ExecutorService = Executors.newSingleThreadExecutor()
    var handler: Handler = Handler(Looper.getMainLooper())

    private var mBookDao: BookDAO = db.bookDao()
    private var mAllBooks: LiveData<List<Book>> = mBookDao.getAll()
    private var mLastOpenedBook: LiveData<Book> = mBookDao.getLastBook()

    fun getAllBooks(): LiveData<List<Book>> {
        return mAllBooks
    }

    fun getLastOpenedBook(): LiveData<Book>{
        return mLastOpenedBook
    }

    fun insert(book: Book) {
        executor.execute {mBookDao.insertAll(book)}
    }

    fun pushToTop(book: Book){
        executor.execute {mBookDao.pushToTop(Date(System.currentTimeMillis()), book.id)}
    }

    fun deleteAllData(){
        mBookDao.nukeTable()
    }

    fun delete(book: Book){
        executor.execute {mBookDao.delete(book)}
    }
}