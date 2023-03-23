package com.sarbaevartur.wuwreader.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sarbaevartur.wuwreader.*
import com.sarbaevartur.wuwreader.R
import com.sarbaevartur.wuwreader.db.Book
import com.sarbaevartur.wuwreader.ui.theme.OrangeLight
import java.util.*

const val TAG = "Library"

@Composable
fun LibraryView(viewModel: MainViewModel, navController: NavController, modifier: Modifier){

    val lastBook by viewModel.getLastOpenedBook().observeAsState()

    Column(modifier = modifier) {
        SearchBar()
        LastBookPreview(onClick = {navController.navigate(Routes.BookView.route)}, lastBook)
        Library(viewModel, modifier, navController)
    }
}

@Composable
@Preview
fun SearchBar(
    modifier: Modifier = Modifier
) {
    val state = remember { mutableStateOf("") }
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = Modifier
            .fillMaxWidth(),
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = Color.White,
        ),
        textStyle = TextStyle(fontSize = 18.sp),
        leadingIcon = {
            Icon(
                Icons.Default.Search,
                contentDescription = "",
                modifier = Modifier
                    .padding(15.dp)
                    .size(24.dp)
            )
        },
        trailingIcon = {
            if (state.value != "") {
                IconButton(
                    onClick = {
                        state.value = ""
                    }
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "",
                        modifier = Modifier
                            .padding(15.dp)
                            .size(24.dp)
                    )
                }
            }
        },
        singleLine = true,
        shape = RectangleShape,
    )
}

@Composable
fun LastBookPreview(
    onClick: () -> Unit,
    book: Book?,
    modifier: Modifier = Modifier
){
    Row(
        modifier = modifier
            .fillMaxWidth()) {
        Image(painter = painterResource(id = R.drawable.book_preview),
            contentDescription = stringResource(id = R.string.book_preview_content_description),
            modifier = modifier
                .weight(1f)
                .align(CenterVertically))
        Column(modifier = modifier
            .padding(16.dp)
            .weight(3f)) {
            Text(
                text = book?.title ?: stringResource(id = R.string.unknown_title),
                style = MaterialTheme.typography.h5,
                maxLines = 2)
            Text(
                text = book?.author ?: stringResource(id = R.string.unknown_author),
                color = OrangeLight)
            Text(
                text = stringResource(id = R.string.read) + " " + book?.lastPage)
            Button(onClick = onClick) {
                Text(text = stringResource(id = R.string.open_book))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Library(viewModel: MainViewModel, modifier: Modifier = Modifier, navController: NavController){

    var deleteDialog by remember { mutableStateOf(false) }
    var book by remember { mutableStateOf(viewModel.getLastOpenedBook().value) }
    if(deleteDialog){
        AlertDialogs().SureDeleteBookDialog(
            onDismiss = { deleteDialog = !deleteDialog },
            onDelete = {
                viewModel.delete(book!!)
                deleteDialog = !deleteDialog
            })
    }

    val bookList by viewModel.getAllBooks().observeAsState()

    if (bookList?.isEmpty() == false){

        LazyColumn(modifier = modifier){
            itemsIndexed(
                items = bookList!!,
                key = { _, item ->
                    item.hashCode()
                }
            ) { index, item ->
                val state = rememberDismissState(
                    confirmStateChange = {
                        if (it == DismissValue.DismissedToStart) {
                            book = item
                            deleteDialog = !deleteDialog
                        }
                        true
                    }
                )

                SwipeToDismiss(
                    state = state,
                    background = {
                        val color = when (state.dismissDirection) {
                            DismissDirection.StartToEnd -> Color.Transparent
                            DismissDirection.EndToStart -> Color.Red
                            null -> Color.Transparent
                        }

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(color = color)
                                .padding(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.White,
                                modifier = Modifier.align(Alignment.CenterEnd)
                            )
                        }
                    },
                    dismissContent = {
                        BookCard(book = item, navController = navController, viewModel = viewModel)
                        Spacer(modifier = modifier.size(8.dp))
                    },
                    directions = setOf(DismissDirection.EndToStart)
                )
            }
        }
    }
}

@Composable
fun BookCard(book: Book, modifier: Modifier = Modifier, navController: NavController, viewModel: MainViewModel){

    var openDialog by remember { mutableStateOf(false)  }

    if (openDialog){
        AlertDialogs().OpenBookDialog(
            onDismiss = {openDialog = !openDialog},
            onOpen = {
                viewModel.pushToTop(book)
                navController.navigate(Routes.BookView.route)
            })
    }

    val pagePercent = book.lastPage.toFloat()/book.pages

    Box(
        modifier = modifier
            .clickable(onClick = { openDialog = !openDialog })
            .padding(8.dp)
            .border(border = BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(16.dp))
    ) {
        Column {
            Row{
                Image(
                    painter = painterResource(id = R.drawable.book_preview),
                    contentDescription = stringResource(id = R.string.book_preview_content_description),
                    modifier = modifier.weight(1f)
                )
                Column(
                    modifier = modifier
                        .weight(4f)
                        .padding(horizontal = 8.dp)
                ) {
                    Text(
                        text = book.title,
                        style = MaterialTheme.typography.h5,
                        maxLines = 2)
                    Text(
                        text = book.author)
                    LinearProgressIndicator(progress = pagePercent, modifier = modifier.fillMaxWidth())
                }
                Text(
                    text = "${(pagePercent*100).toInt()}%", modifier = modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .align(CenterVertically)
                )
            }
        }
    }
}