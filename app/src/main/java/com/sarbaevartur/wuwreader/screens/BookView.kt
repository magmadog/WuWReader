package com.sarbaevartur.wuwreader.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.sarbaevartur.wuwreader.db.Book
import com.sarbaevartur.wuwreader.readers.epub.EpubReader
import com.sarbaevartur.wuwreader.readers.pdf.PdfView

@Composable
fun BookView(book: Book, modifier: Modifier){

    when(book.format){
        "pdf" -> PdfView(book, modifier)
        "epub" -> EpubReader().GetEpubText(book.path)
    }

}