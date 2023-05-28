package com.sarbaevartur.wuwreader.app

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import com.sarbaevartur.wuwreader.MainViewModel
import com.sarbaevartur.wuwreader.screens.Routes

class ExecuteVoiceCommand {

    fun processCommand(command: String, viewModel: MainViewModel, navController: NavController, context: Context): Boolean{
        when (command) {
            "открой последнюю книгу" -> {
                val lastBook = viewModel.getLastOpenedBook()
                if (lastBook != null) {
                    navController.navigate(Routes.BookView.route)
                } else {
                    return false
                }
                return true
            }
            "открой экран настроек" -> {
                navController.navigate(Routes.SettingsView.route)
                return true
            }
            "следующая страница" -> {
                viewModel.nextPage()
                return true
            }
            "дальше" -> {
                viewModel.nextPage()
                return true
            }
            "предыдущая страница" -> {
                viewModel.prevPage()
                return true
            }
            "назад" -> {
                viewModel.prevPage()
                return true
            }
            "начни чтение книги" -> {
                viewModel.textToSpeech(context)
                return true
            }
            "начни чтение" -> {
                viewModel.textToSpeech(context)
                return true
            }
            "прочитай страницу" -> {
                viewModel.textToSpeech(context)
                return true
            }
            "стоп" -> {
                viewModel.stopTTS()
                return true
            }
            "стой" -> {
                viewModel.stopTTS()
                return true
            }
            else -> {
                Toast.makeText(
                    context,
                    "Неизвестная голосовая команда ['$command']",
                    Toast.LENGTH_SHORT).show()
                return false
            }
        }
    }
}