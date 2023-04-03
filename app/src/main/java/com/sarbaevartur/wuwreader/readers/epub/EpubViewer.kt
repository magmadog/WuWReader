package com.sarbaevartur.wuwreader.readers.epub

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.Math.ceil
import java.lang.Math.min

class EpubViewer {

    @Composable
    fun BookContent(book: String, fontSize: TextUnit = 14.sp) {
        val context = LocalContext.current
        val configuration = LocalConfiguration.current

        val lines = book.lines()
        val currentPage = remember { mutableStateOf(0) }
        val linesPerPage = 15 // количество строк на странице

        Column(modifier = Modifier.fillMaxSize()) {
            // Отображаем текущую страницу и общее количество страниц
            Text(
                text = "Page ${currentPage.value + 1} of ${ceil(lines.size.toDouble() / linesPerPage).toInt()}",
                modifier = Modifier.padding(16.dp)
            )

            // Отображаем строки текущей страницы
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                itemsIndexed(lines.subList(currentPage.value * linesPerPage, min((currentPage.value + 1) * linesPerPage, lines.size))) { index, line ->
                    Text(text = line, textAlign = TextAlign.Justify)
                }
            }


            // Кнопки для перехода на следующую/предыдущую страницу
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 40.dp)
            ) {
                Button(
                    onClick = { currentPage.value-- },
                    enabled = currentPage.value > 0,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Previous Page")
                }
                Button(
                    onClick = { currentPage.value++ },
                    enabled = currentPage.value < ceil(lines.size.toDouble() / linesPerPage).toInt() - 1,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(text = "Next Page")
                }
            }
        }
    }
}