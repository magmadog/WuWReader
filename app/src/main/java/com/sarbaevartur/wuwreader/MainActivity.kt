package com.sarbaevartur.wuwreader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sarbaevartur.wuwreader.db.Book
import com.sarbaevartur.wuwreader.readers.pdf.PdfViewer
import com.sarbaevartur.wuwreader.ui.theme.Green200
import com.sarbaevartur.wuwreader.ui.theme.WuWReaderTheme
import java.io.*


const val TAG = "MainActivity"
const val REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 12345
const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 12346
var uriOpenDocument: Uri = Uri.parse("")

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()
        setContent {
            WuWReaderTheme {
                MyApp()
            }
        }
    }


    fun checkPermissions() {

        var permissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE
            )
        }

        permissionStatus =
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE
            )
        }
    }
}

@Composable
fun MyApp() {
    Scaffold(
        floatingActionButton = {addBookButton()},
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar{
                IconButton(onClick = {  }) { Icon(Icons.Filled.Settings, contentDescription = "Избранное") }
                Spacer(Modifier.weight(1f, true))
                IconButton(onClick = {  }) { Icon(Icons.Filled.Info, contentDescription = "Информация о приложении") }
            }
        }
    ) {
        var library = remember { mutableStateOf<Boolean>(true)}
        Log.d(TAG, "current state: ${library.value}")
        Column{
            if (library.value){
                SearchBar()
                Button(onClick = { library.value = !library.value }) {
                    Text("Open pdf doc")
                }
                lastBookPreview()
                Library()
            }
            else{
                DemoLayout(uriOpenDocument)
            }
        }
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
            )
        },
        colors = TextFieldDefaults.textFieldColors(
            backgroundColor = MaterialTheme.colors.surface
        ),
        placeholder = {
            Text(stringResource(R.string.placeholder_search))
        },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 56.dp)
    )
}

@Composable
fun lastBookPreview(
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
            Text(text = "Title of the book")
            Text(text = "Author of the book")
            Text(text = "Readed 1/256 (1%)")
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Open book")
            }
        }
    }
}

@Composable
fun Library(
    modifier: Modifier = Modifier){
    val books = getBooks()

    LazyColumn(modifier = modifier){
        items(books){ item ->
            bookColumn(book = item)
        }
    }
}

@Composable
fun bookColumn(book: Book, modifier: Modifier = Modifier){

    Row(modifier = modifier
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
        Text(text = book.last_page.toString(), modifier = modifier
            .weight(1f)
            .padding(horizontal = 8.dp)
            .align(Alignment.CenterVertically))
    }
}

@Composable
fun addBookButton(modifier: Modifier = Modifier){

    val result = remember { mutableStateOf<ActivityResult?>(null)}

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result.value = it
    }

    val context = LocalContext.current
    if (result.value != null){

        val uri: Uri = result.value!!.data!!.data!!
        context.contentResolver.takePersistableUriPermission(uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        uriOpenDocument = uri
    }

    val onClick = {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)

        // the type of file we want to pick; for now it's all
        intent.type = "*/*"

        // allows us to pick multiple files
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        launcher.launch(intent)
    }

    FloatingActionButton(onClick = onClick) {
        Icon(Icons.Filled.Add,"")
    }
}

@Composable
fun DemoLayout(uri: Uri) {
    Log.d(TAG, "uri: $uri")
    val isLoading = remember {mutableStateOf(false)}
    val currentLoadingPage = remember {mutableStateOf<Int?>(null)}
    val pageCount = remember {mutableStateOf<Int?>(null)}
    Box {
        PdfViewer(
            modifier = Modifier.fillMaxSize(),
            pdfResUri = uri,
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
            }
        }
    }
}


fun getBooks(): List<Book>{
    var books: MutableList<Book> = mutableListOf()
    for (i in 0 until 30){
        val book = Book(i.toLong(), "Book №$i", "Author of Book №$i")
        books.add(book)
    }
    return books
}


@Preview(showBackground = true, widthDp = 320)
@Composable
fun DefaultPreview() {
//    MyApp()
}