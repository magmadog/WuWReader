package com.sarbaevartur.wuwreader.db

import android.app.Application
import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.*


class BookRepository(application: Application) {

    val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "database-name"
    ).build()

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
        insertAsyncTask(mBookDao).execute(book)
    }

    fun pushToTop(book: Book){
        updateAsyncTask(mBookDao).execute(book)
    }

    fun deleteAllData(){
        mBookDao.nukeTable()
    }

    private class updateAsyncTask internal constructor(dao: BookDAO) :
        AsyncTask<Book, Void?, Void?>() {
        private val mAsyncTaskDao: BookDAO
        override fun doInBackground(vararg params: Book): Void? {
            mAsyncTaskDao.pushToTop(Date(System.currentTimeMillis()), params[0].id)
            return null
        }

        init {
            mAsyncTaskDao = dao
        }
    }

    private class insertAsyncTask internal constructor(dao: BookDAO) :
        AsyncTask<Book, Void?, Void?>() {
        private val mAsyncTaskDao: BookDAO
        override fun doInBackground(vararg params: Book): Void? {
            mAsyncTaskDao.insertAll(params[0])
            return null
        }

        init {
            mAsyncTaskDao = dao
        }
    }
}