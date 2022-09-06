package com.sarbaevartur.wuwreader.db

data class Book(
    val id: Long = 0,
    val title: String = "",
    val author: String = "",
    var last_page: Int = 0,
    val cover: String = ""
)