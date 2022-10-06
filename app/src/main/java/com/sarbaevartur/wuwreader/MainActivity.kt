package com.sarbaevartur.wuwreader

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sarbaevartur.wuwreader.db.Book
import com.sarbaevartur.wuwreader.screens.BookView
import com.sarbaevartur.wuwreader.screens.LibraryView
import com.sarbaevartur.wuwreader.screens.Routes
import com.sarbaevartur.wuwreader.ui.theme.WuWReaderTheme
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.HiltAndroidApp
import java.util.*


const val TAG = "MainActivity"
const val REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 12345
const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 12346

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<MainViewModel>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()
        setContent {
            WuWReaderTheme {
                val navController = rememberNavController()
                MyApp(viewModel = viewModel, navController)
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

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MyApp(viewModel: MainViewModel, navController: NavController) {
    Scaffold(
        floatingActionButton = {addBookButton(viewModel)},
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            BottomAppBar{
                IconButton(onClick = {  }) { Icon(Icons.Filled.Settings, contentDescription = "Избранное") }
                Spacer(Modifier.weight(1f, true))
                IconButton(onClick = {  }) { Icon(Icons.Filled.Info, contentDescription = "Информация о приложении") }
            }
        }
    ) { padding ->
        
        NavHost(navController = navController as NavHostController, startDestination = Routes.Library.route) {

            composable(Routes.Library.route){
                LibraryView(viewModel = viewModel, navController = navController, modifier = Modifier.padding(padding))
            }

            composable(Routes.BookView.route){
                BookView(viewModel = viewModel, modifier = Modifier.padding(padding))
            }
        }
    }
}


@Composable
fun addBookButton(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier){

    val result = remember { mutableStateOf<ActivityResult?>(null)}

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        result.value = it
    }

    val context = LocalContext.current
    if (result.value != null){

        val uri: Uri = result.value!!.data!!.data!!
        context.contentResolver.takePersistableUriPermission(uri,
            Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION)

        val title = queryName(context.contentResolver, uri)
        val book = Book(
            title = title.substringBeforeLast('.'),
            path = uri.toString(),
            lastOpenDate = Date(System.currentTimeMillis()),
            format = title.substringAfterLast('.')
            )
        viewModel.insert(book)
    }

    val onClick = {

        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "*/*"

        // allows us to pick multiple files
//        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)

        launcher.launch(intent)
    }

    FloatingActionButton(onClick = onClick, modifier = modifier) {
        Icon(Icons.Filled.Add,"")
    }
}

private fun queryName(resolver: ContentResolver, uri: Uri): String {
    val returnCursor: Cursor = resolver.query(uri, null, null, null, null)!!
    val nameIndex: Int = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
    returnCursor.moveToFirst()
    val name: String = returnCursor.getString(nameIndex)
    returnCursor.close()
    return name
}