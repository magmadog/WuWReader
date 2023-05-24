package com.sarbaevartur.wuwreader.app

import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.sarbaevartur.wuwreader.MainViewModel
import com.sarbaevartur.wuwreader.screens.BookView
import com.sarbaevartur.wuwreader.screens.Routes

class ExecuteVoiceCommand {

    fun processCommand(command: String, viewModel: MainViewModel, navController: NavController): Boolean{
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
                return true
            }
            "предыдущая страница" -> {
                return true
            }
            else -> {
                return false
            }
        }
    }
}