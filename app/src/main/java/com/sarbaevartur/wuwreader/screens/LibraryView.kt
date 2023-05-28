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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.sarbaevartur.wuwreader.*
import com.sarbaevartur.wuwreader.R
import com.sarbaevartur.wuwreader.domain.model.Book
import com.sarbaevartur.wuwreader.ui.theme.OrangeLight
import java.util.*

@Composable
fun LibraryView(viewModel: MainViewModel, navController: NavController, modifier: Modifier) {

    val lastBook by viewModel.getLastOpenedBook().collectAsState(initial = null)
    val bookList by viewModel.getAllBooks().collectAsState(initial = emptyList())
    val searchRequestText = remember { mutableStateOf("") }

    if (bookList.isNotEmpty()) {
        Column(modifier = modifier) {
            SearchBar(state = searchRequestText)
            if (lastBook != null && searchRequestText.value == "")
                LastBookPreview(
                    onClick = { navController.navigate(Routes.BookView.route) },
                    lastBook!!
                )
            Library(viewModel, modifier, navController, bookList)
            if (searchRequestText.value != ""){
                val books = searchBooksInOPDS("Book")
                SearchResults(books = books)
            }
        }
    }
}

@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    state: MutableState<String>
) {
    TextField(
        value = state.value,
        onValueChange = { value ->
            state.value = value
        },
        modifier = modifier
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
    book: Book,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier.fillMaxWidth()) {
        Image(
            painter = painterResource(id = R.drawable.book_preview),
            contentDescription = stringResource(id = R.string.book_preview_content_description),
            modifier = modifier
                .weight(1f)
                .align(CenterVertically)
        )
        Column(
            modifier = modifier
                .padding(16.dp)
                .weight(3f)
        ) {
            Text(
                text = book.title,
                style = MaterialTheme.typography.h5,
                maxLines = 2
            )
            Text(
                text = book.author,
                color = OrangeLight
            )
            Text(
                text = stringResource(id = R.string.read) + " " + book.lastPage+1
            )
            Button(onClick = onClick) {
                Text(text = stringResource(id = R.string.open_book))
            }
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Library(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier,
    navController: NavController,
    bookList: List<Book>
) {

    var deleteDialog by remember { mutableStateOf(false) }
    var book by remember { mutableStateOf(Book()) }

    if (deleteDialog) {
        AlertDialogs().SureDeleteBookDialog(
            onDismiss = { deleteDialog = !deleteDialog },
            onDelete = {
                viewModel.delete(book)
                deleteDialog = !deleteDialog
            })
    }

    if (bookList.isNotEmpty()) {
        LazyColumn(modifier = modifier) {
            itemsIndexed(
                items = bookList
            ) { _, item ->
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
                            else -> Color.Transparent
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
fun BookCard(
    book: Book,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: MainViewModel
) {

    var openDialog by remember { mutableStateOf(false) }

    if (openDialog) {
        AlertDialogs().OpenBookDialog(
            onDismiss = { openDialog = !openDialog },
            onOpen = {
                viewModel.pushToTop(book)
                navController.navigate(Routes.BookView.route)
            })
    }

    val pagePercent = book.lastPage.toFloat() / book.pages

    Box(
        modifier = modifier
            .clickable(onClick = {
                openDialog = !openDialog
            })
            .padding(8.dp)
            .border(border = BorderStroke(1.dp, Color.DarkGray), shape = RoundedCornerShape(16.dp))
    ) {
        Column {
            Row {
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
                        maxLines = 2
                    )
                    Text(
                        text = book.author
                    )
                    LinearProgressIndicator(
                        progress = pagePercent,
                        modifier = modifier.fillMaxWidth()
                    )
                }
                Text(
                    text = "${(pagePercent * 100).toInt()}%", modifier = modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .align(CenterVertically)
                )
            }
        }
    }
}

@Composable
fun SearchResults(books: List<Book>){
    if (books.isNotEmpty()) {
        LazyColumn() {
            itemsIndexed(
                items = books
            ) { _, item ->
                Box(modifier = Modifier.height(64.dp)){
                    Column {
                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.book_preview),
                                contentDescription = stringResource(id = R.string.book_preview_content_description)
                            )
                            Column(
                            ) {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.h5,
                                    maxLines = 2
                                )
                                Text(
                                    text = item.author
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

fun searchBooksInOPDS(query: String): List<Book> {
    // Здесь вы можете выполнить запрос к OPDS API и получить список книг,
    // соответствующих запросу пользователя
    // В этом примере я просто возвращаю фиктивные данные

    val allBooks = listOf(
        Book(title = "Book 1", author = "Author 1"),
        Book(title = "Book 2", author = "Author 2"),
        Book(title = "Book 3", author = "Author 3"),
        Book(title = "Book 4", author = "Author 4"),
        Book(title = "Book 5", author = "Author 5")
    )

    // Фильтруем список книг по запросу пользователя
    val filteredBooks = allBooks.filter { book ->
        book.title.contains(query, ignoreCase = true) || book.author.contains(query, ignoreCase = true)
    }

    return filteredBooks
}