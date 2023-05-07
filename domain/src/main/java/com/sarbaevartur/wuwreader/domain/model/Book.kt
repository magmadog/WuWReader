package com.sarbaevartur.wuwreader.domain.model

import java.util.*

class Book(
    val id: Int = 0,
    val title: String = "",
    val author: String = "",
    val path: String = "",
    var lastPage: Int = 0,
    val cover: String = "",
    val lastOpenDate: Date = Date(),
    val format: String = "PDF",
    var pages: Int = 100,
    val size: Int = 2048,
)