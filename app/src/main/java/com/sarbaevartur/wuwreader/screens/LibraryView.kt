package com.sarbaevartur.wuwreader.screens

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sarbaevartur.wuwreader.*
import com.sarbaevartur.wuwreader.R
import com.sarbaevartur.wuwreader.db.Book
import com.sarbaevartur.wuwreader.ui.theme.Green200
import java.util.*

const val TAG = "Library"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LibraryView(viewModel: MainViewModel, navController: NavController, modifier: Modifier){

    val lastBook by viewModel.getLastOpenedBook().observeAsState()

    Column() {
        SearchBar()
        lastBookPreview(onClick = {navController.navigate(Routes.BookView.route)}, lastBook)
        Library(viewModel)
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier
) {
    TextField(
        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null
            ) },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        placeholder = {Text(stringResource(R.string.placeholder_search)) },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

@Composable
fun lastBookPreview(
    onClick: () -> Unit,
    book: Book?,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()) {
        Image(painter = painterResource(id = R.drawable.book_preview),
            contentDescription = stringResource(id = R.string.book_preview_content_description),
            modifier = modifier.weight(1f))
        Column(modifier = modifier
            .padding(16.dp)
            .weight(3f)) {
            Text(text = book?.title ?: stringResource(id = R.string.unknown_title))
            Text(text = book?.author ?: stringResource(id = R.string.unknown_author))
            Text(text = stringResource(id = R.string.read) + " " + book?.lastPage)
            Button(onClick = onClick) {
                Text(text = stringResource(id = R.string.open_book))
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Library(viewModel: MainViewModel, modifier: Modifier = Modifier){

    val bookList by viewModel.mAllBooks.observeAsState()
    if (bookList != null){
        LazyColumn(modifier = modifier){
            items(bookList!!){ item ->
                bookColumn(book = item, onClick = {viewModel.pushToTop(item)})
            }
        }
    }
}

@Composable
fun bookColumn(book: Book, onClick: () -> Unit, modifier: Modifier = Modifier){

    Row(modifier = modifier
        .clickable(onClick = onClick)
        .padding(8.dp)
        .background(Green200)) {
        Image(painter = painterResource(id = R.drawable.book_preview),
            contentDescription = stringResource(id = R.string.book_preview_content_description),
            modifier = modifier.weight(1f))
        Column(modifier = modifier
            .weight(3f)
            .padding(horizontal = 8.dp)) {
            Text(text = book.title, style = MaterialTheme.typography.h5)
            Text(text = book.author)
        }
        Text(text = book.lastPage.toString(), modifier = modifier
            .weight(1f)
            .padding(horizontal = 8.dp)
            .align(Alignment.CenterVertically))
    }
}