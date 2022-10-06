package com.sarbaevartur.wuwreader.screens

import android.os.Build
import android.util.Log
import android.view.FrameMetrics.ANIMATION_DURATION
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.*
import androidx.compose.animation.splineBasedDecay
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.horizontalDrag
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.input.pointer.util.VelocityTracker
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.sarbaevartur.wuwreader.*
import com.sarbaevartur.wuwreader.R
import com.sarbaevartur.wuwreader.db.Book
import com.sarbaevartur.wuwreader.ui.theme.OrangeLight
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.time.Instant.now
import java.time.LocalDate.now
import java.time.LocalDateTime
import java.util.*
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

const val TAG = "Library"

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun LibraryView(viewModel: MainViewModel, navController: NavController, modifier: Modifier){

    val lastBook by viewModel.getLastOpenedBook().observeAsState()

    Column(modifier = modifier) {
        SearchBar()
        LastBookPreview(onClick = {navController.navigate(Routes.BookView.route)}, lastBook)
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
            modifier = modifier.weight(1f).align(CenterVertically))
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
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Library(viewModel: MainViewModel, modifier: Modifier = Modifier){

    val bookList by viewModel.mAllBooks.observeAsState()

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
                            viewModel.delete(item)
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
                        BookCard(book = item, onClick = { viewModel.pushToTop(item) })
                    },
                    directions = setOf(DismissDirection.EndToStart)
                )
                Divider()
            }
        }
    }
}

@Composable
fun BookCard(book: Book, onClick: () -> Unit, modifier: Modifier = Modifier){

    Column {
        Row(
            modifier = modifier
                .clickable(onClick = onClick)
        ) {
            Image(
                painter = painterResource(id = R.drawable.book_preview),
                contentDescription = stringResource(id = R.string.book_preview_content_description),
                modifier = modifier.weight(1f)
            )
            Column(
                modifier = modifier
                    .weight(3f)
                    .padding(horizontal = 8.dp)
            ) {
                Text(
                    text = book.title,
                    style = MaterialTheme.typography.h5,
                    maxLines = 2)
                Text(text = book.author)
            }
            Text(
                text = book.lastPage.toString(), modifier = modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .align(CenterVertically)
            )
        }
        LinearProgressIndicator(progress = 0.7f, modifier = modifier.fillMaxWidth())
    }
}


@Preview
@Composable
fun Preview(){
    val book = Book(12, "Граф Монте-Кристо", "Александр Дюма", "", 12, "", Date(System.currentTimeMillis()))

    LastBookPreview(onClick = { /*TODO*/ }, book = book)
}