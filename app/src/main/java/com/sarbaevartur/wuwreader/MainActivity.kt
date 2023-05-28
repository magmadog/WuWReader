package com.sarbaevartur.wuwreader

import android.Manifest
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sarbaevartur.wuwreader.app.ExecuteVoiceCommand
import com.sarbaevartur.wuwreader.domain.model.Book
import com.sarbaevartur.wuwreader.screens.*
import com.sarbaevartur.wuwreader.ui.theme.WuWReaderTheme
import com.sarbaevartur.wuwreader.voice.VoiceToTextParser
import java.util.*

const val REQUEST_CODE_PERMISSION_READ_EXTERNAL_STORAGE = 12345
const val REQUEST_CODE_PERMISSION_WRITE_EXTERNAL_STORAGE = 12346

class MainActivity : ComponentActivity() {

    private val voiceToText by lazy { VoiceToTextParser(application) }
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        checkPermissions()
        setContent {
            WuWReaderTheme {
                val navController = rememberNavController()
                MyApp(viewModel = viewModel, navController, voiceToText)
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

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun MyApp(viewModel: MainViewModel, navController: NavController, voiceToText: VoiceToTextParser) {

    val scaffoldState = rememberScaffoldState()

    var isBookViewScreen by remember { mutableStateOf(false) }

    Scaffold(
        scaffoldState = scaffoldState,
        floatingActionButton = {
            if (!isBookViewScreen){ AddBookButton(viewModel) }},
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        bottomBar = {
            if (!isBookViewScreen){
                BottomAppBar{
                    IconButton(onClick = { navController.navigate(Routes.SettingsView.route) }) { Icon(Icons.Filled.Settings, contentDescription = "Настройки") }
                    Spacer(Modifier.weight(1f, true))
                    IconButton(onClick = {  }) { Icon(Icons.Filled.Info, contentDescription = "Информация о приложении") }
                }
            }
        }
    ) { padding ->

        val state by voiceToText.state.collectAsState( )

        var canRecord by remember {
            mutableStateOf(false)
        }

        val recordAudioLauncher = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
            onResult = { isGranted ->
                canRecord = isGranted
            }
        )

        LaunchedEffect(key1 = recordAudioLauncher) {
            recordAudioLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            if (canRecord) {
                                if (!state.isSpeaking) {
                                    voiceToText.startListening()
                                } else {
                                    voiceToText.stopListening()
                                }
                            }
                        }
                    )
                }
        )
        if (!state.isSpeaking and state.spokenText.isNotEmpty()) {
            ExecuteVoiceCommand().processCommand(state.spokenText.lowercase(), viewModel, navController, LocalContext.current)
            state.spokenText = ""
        }
        NavHost(
                navController = navController as NavHostController,
                startDestination = Routes.Library.route
            ) {

                composable(Routes.Library.route) {
                    isBookViewScreen = false
                    LibraryView(
                        viewModel = viewModel,
                        navController = navController,
                        modifier = Modifier.padding(padding)
                    )
                }

                composable(Routes.BookView.route) {
                    isBookViewScreen = true
                    BookView(viewModel = viewModel, modifier = Modifier.padding(padding))
                }

                composable(Routes.SettingsView.route) {
                    isBookViewScreen = false
                    SettingsView()
                }
            }
    }
}


@Composable
fun AddBookButton(
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