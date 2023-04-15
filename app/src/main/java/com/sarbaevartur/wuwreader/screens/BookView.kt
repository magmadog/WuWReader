package com.sarbaevartur.wuwreader.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.sarbaevartur.wuwreader.MainViewModel
import com.sarbaevartur.wuwreader.readers.epub.EpubReader
import com.sarbaevartur.wuwreader.readers.pdf.PdfView

@Composable
fun BookView(viewModel: MainViewModel, modifier: Modifier){

    val book by viewModel.getLastOpenedBook().observeAsState()

    when(book?.format){
        "pdf" -> PdfView(book!!, modifier)
        "epub" -> EpubReader().GetEpubText(book!!.path)
    }

}