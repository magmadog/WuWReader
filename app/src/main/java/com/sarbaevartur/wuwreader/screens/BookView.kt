package com.sarbaevartur.wuwreader.screens

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.sarbaevartur.wuwreader.MainViewModel
import com.sarbaevartur.wuwreader.db.Book
import com.sarbaevartur.wuwreader.readers.pdf.PdfViewer

@Composable
fun BookView(viewModel: MainViewModel, modifier: Modifier){
    val isLoading = remember { mutableStateOf(false) }
    val currentLoadingPage = remember { mutableStateOf<Int?>(null) }
    val pageCount = remember { mutableStateOf<Int?>(null) }

    val book by viewModel.getLastOpenedBook().observeAsState()

    Box {
        PdfViewer(
            modifier = Modifier.fillMaxSize(),
            pdfResUri = Uri.parse(book!!.path),
            loadingListener = { loading, currentPage, maxPage ->
                isLoading.value = loading
                if (currentPage != null) currentLoadingPage.value = currentPage
                if (maxPage != null) pageCount.value = maxPage
            }
        )
        if (isLoading.value) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center
            ) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    progress = if (currentLoadingPage.value == null || pageCount.value == null) 0f
                    else currentLoadingPage.value!!.toFloat() / pageCount.value!!.toFloat()
                )
                Text(
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 5.dp)
                        .padding(horizontal = 30.dp),
                    text = "${currentLoadingPage.value ?: "-"} pages loaded/${pageCount.value ?: "-"} total pages"
                )
                Spacer(modifier = Modifier.padding(48.dp))
            }
        }
    }
}