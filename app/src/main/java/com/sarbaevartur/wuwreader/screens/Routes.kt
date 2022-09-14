package com.sarbaevartur.wuwreader.screens

sealed class Routes(val route: String) {
    object Library : Routes("library")
    object BookView : Routes("book_view")
}