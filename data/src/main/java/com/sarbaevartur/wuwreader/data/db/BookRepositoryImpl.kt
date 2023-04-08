package com.sarbaevartur.wuwreader.data.db

import android.app.Application
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.map
import androidx.room.Room
import com.sarbaevartur.wuwreader.data.model.BookEntity
import com.sarbaevartur.wuwreader.domain.model.Book
import com.sarbaevartur.wuwreader.domain.repository.BookRepository
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class BookRepositoryImpl(application: Application): BookRepository {

    private val db = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "database-name"
    )
        .allowMainThreadQueries()
        .build()

    var executor: ExecutorService = Executors.newSingleThreadExecutor()

    private var mBookDao: BookDAO = db.bookDao()
    private var mAllBooks: LiveData<List<BookEntity>> = mBookDao.getAll()
    private var mLastOpenedBookEntity: LiveData<BookEntity> = mBookDao.getLastBook()

    override fun getAllBooks(): LiveData<List<Book>> {
        return mAllBooks.map { it.map { book -> bookEntityToBookTransformer(book) } }.distinctUntilChanged()
    }

    override fun getLastOpenedBook(): LiveData<Book> {
        return mLastOpenedBookEntity.map {  book -> bookEntityToBookTransformer(book) }.distinctUntilChanged()
    }

    override fun insert(book: Book) {
        executor.execute {
            val k = mBookDao.insertAll(bookToBookEntityTransformer(book))
            Log.d("DB_add", "$k")
        }
    }

    override fun pushToTop(book: Book){
        executor.execute {mBookDao.pushToTop(Date(System.currentTimeMillis()), book.id)}
    }

    override fun deleteAllData(){
        mBookDao.nukeTable()
    }

    override fun delete(book: Book){
        executor.execute {mBookDao.delete(bookToBookEntityTransformer(book))}
    }

    override fun update(book: Book){
        executor.execute {mBookDao.update(bookToBookEntityTransformer(book))}
    }

    private fun bookToBookEntityTransformer(book: Book): BookEntity{
        return BookEntity(
            id = book.id,
            title = book.title,
            author = book.author,
            path = book.path,
            lastPage = book.lastPage,
            cover = book.cover,
            lastOpenDate = book.lastOpenDate,
            format = book.format,
            pages = book.pages,
            size = book.size
        )
    }

    private fun bookEntityToBookTransformer(bookEntity: BookEntity): Book {
        return Book(
            id = bookEntity.id,
            title = bookEntity.title,
            author = bookEntity.author,
            path = bookEntity.path,
            lastPage = bookEntity.lastPage,
            cover = bookEntity.cover,
            lastOpenDate = bookEntity.lastOpenDate,
            format = bookEntity.format,
            pages = bookEntity.pages,
            size = bookEntity.size
        )
    }
}