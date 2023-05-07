package com.sarbaevartur.wuwreader.readers

interface Reader {

    fun getLastOpenedPage(): Int

    fun setLastOpenedPage(pageNumber: Int)

    fun getCover()
}