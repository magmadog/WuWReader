package com.sarbaevartur.domain.model

import java.util.*

data class Book(
        val id: Int = 0,
        val title: String = "",
        val author: String = "",
        val path: String = "",
        var lastPage: Int = 0,
        var cover: String = "",
        val lastOpenDate: Date,
        val format: String = "PDF",
        var pages: Int = 100,
        val size: Int = 2048,
    )