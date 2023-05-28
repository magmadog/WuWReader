package com.sarbaevartur.wuwreader

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import com.sarbaevartur.wuwreader.data.db.BookRepositoryImpl
import com.sarbaevartur.wuwreader.domain.model.Book
import com.sarbaevartur.wuwreader.domain.usecase.GetLocalBooksUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import java.util.*

class MainViewModel (application: Application) : AndroidViewModel(application) {

    private val _stateTTS = mutableStateOf(BookScreenState())
    var stateTTS: State<BookScreenState> = _stateTTS
    private var textToSpeech:TextToSpeech? = null

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

    fun onTextFieldValueChange(text: List<String>){
        _stateTTS.value = stateTTS.value.copy(
            text = text
        )
    }

    fun textToSpeech(context: Context){
        _stateTTS.value = stateTTS.value.copy(
            isButtonEnabled = false
        )
        textToSpeech = TextToSpeech(
            context
        ) {
            if (it == TextToSpeech.SUCCESS) {
                for (textItemOnScreen in _stateTTS.value.text){
                    textToSpeech?.let { txtToSpeech ->
                        txtToSpeech.language = Locale("ru","RU")
                        txtToSpeech.setSpeechRate(1.0f)
                        txtToSpeech.speak(
                            textItemOnScreen,
                            TextToSpeech.QUEUE_ADD,
                            null,
                            null
                        )
                    }
                }
            }
            _stateTTS.value = stateTTS.value.copy(
                isButtonEnabled = true
            )
        }
    }

    fun stopTTS(){
        textToSpeech?.shutdown()
    }

    fun nextPage(){
        var currentBook: Book
        runBlocking(Dispatchers.Default) {
            currentBook = mLastOpenedBook.first()
        }
        currentBook.lastPage++
        update(currentBook)
    }

    fun prevPage(){
        val currentBook: Book
        runBlocking(Dispatchers.IO) {
            currentBook = mLastOpenedBook.first()
        }
        currentBook.lastPage--
        update(currentBook)
    }
}