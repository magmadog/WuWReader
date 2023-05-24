package com.sarbaevartur.wuwreader.screens

import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.sarbaevartur.wuwreader.MainViewModel
import com.sarbaevartur.wuwreader.readers.epub.EpubViewer
import com.sarbaevartur.wuwreader.readers.pdf.PdfView

@Composable
fun BookView(viewModel: MainViewModel, modifier: Modifier){
    val book by viewModel.getLastOpenedBook().collectAsState(initial = null)

    if (book != null) {
        var page by remember { mutableStateOf(book!!.lastPage) }

        when (book?.format) {
            "pdf" -> PdfView(book!!, modifier)
            "epub" -> {
                EpubViewer().BookContent(book = book!!, viewModel, page, { page++ }, { page-- })
            }
        }
    }
}