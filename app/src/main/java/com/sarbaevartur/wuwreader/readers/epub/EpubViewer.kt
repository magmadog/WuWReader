package com.sarbaevartur.wuwreader.readers.epub

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.sarbaevartur.wuwreader.MainViewModel
import com.sarbaevartur.wuwreader.domain.model.Book
import java.lang.Math.ceil
import java.lang.Math.min

class EpubViewer(val book: Book, private val viewModel: MainViewModel) {

    @Composable
    fun BookContent() {
        val text = EpubReader().getEpubText(book.path, LocalContext.current)
        val lines = text.lines()
        val linesPerPage = 15
        val currentPage = book.lastPage

        val stateTTS = viewModel.stateTTS.value
        val context = LocalContext.current

        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "Page ${currentPage + 1} of ${ceil(lines.size.toDouble() / linesPerPage).toInt()}",
                modifier = Modifier.padding(16.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                val listOfLines = lines.subList(currentPage * linesPerPage, min((currentPage + 1) * linesPerPage, lines.size))
                viewModel.onTextFieldValueChange(listOfLines)
                itemsIndexed(listOfLines) { _, line ->
                    Text(text = line, textAlign = TextAlign.Justify)
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp)
            ) {
                Button(
                    onClick = { viewModel.prevPage() },
                    enabled = currentPage > 0,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Previous\nPage")
                }
                Button(onClick = {
                    viewModel.textToSpeech(context)
                }, enabled = stateTTS.isButtonEnabled
                ) {
                    Text(text = "speak")
                }
                Button(onClick = {
                    viewModel.stopTTS()
                }
                ) {
                    Text(text = "stop")
                }
                Button(
                    onClick = { viewModel.nextPage() },
                    enabled = currentPage < ceil(lines.size.toDouble() / linesPerPage).toInt() - 1,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Next\nPage")
                }
            }
        }

        book.pages = ceil(lines.size.toDouble() / linesPerPage).toInt()
    }
}