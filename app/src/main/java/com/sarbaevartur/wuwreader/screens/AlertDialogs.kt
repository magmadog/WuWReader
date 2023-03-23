package com.sarbaevartur.wuwreader.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.sarbaevartur.wuwreader.R

class AlertDialogs {

    @Composable
    fun infoDialog(){
        //TODO
    }

    @Composable
    fun sureDeleteBookDialog(): Boolean{
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text(text = "Dialog Title")
            },
            text = {
                Text("Here is a text ")
            },
            confirmButton = {
                Button(
                    onClick = {

                    }) {
                    Text(stringResource(id = R.string.open_book))
                }
            },
            dismissButton = {
                Button(
                    onClick = {

                    }) {
                    Text(stringResource(id = R.string.cancel))
                }
            }
        )
        return false
    }

    @Composable
    fun OpenBookDialog(onDismiss: () -> Unit, onOpen: () -> Unit) {

        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = true)
        ) {
            Card(
                //shape = MaterialTheme.shapes.medium,
                shape = RoundedCornerShape(10.dp),
                // modifier = modifier.size(280.dp, 240.dp)
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 8.dp
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Red.copy(alpha = 0.8F)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,

                        ) {

                        Image(
                            painter = painterResource(id = R.drawable.book_preview),
                            contentDescription = "Exit app",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    Text(
                        text = "Lorem Ipsum is simply dummy text",
                        modifier = Modifier.padding(8.dp), fontSize = 20.sp
                    )

                    Text(
                        text = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard",
                        modifier = Modifier.padding(8.dp)
                    )

                    Row(Modifier.padding(top = 10.dp)) {
                        OutlinedButton(
                            onClick = { onDismiss() },
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1F)
                        ) {
                            Text(text = stringResource(id = R.string.cancel))
                        }


                        Button(
                            onClick = { onOpen() },
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1F)
                        ) {
                            Text(text = stringResource(id = R.string.open_book))
                        }
                    }


                }
            }
        }
    }

    @Composable
    fun SureDeleteBookDialog(onDismiss: () -> Unit, onDelete: () -> Unit) {

        Dialog(
            onDismissRequest = { onDismiss() },
            properties = DialogProperties(dismissOnBackPress = true,dismissOnClickOutside = true)
        ) {
            Card(
                //shape = MaterialTheme.shapes.medium,
                shape = RoundedCornerShape(10.dp),
                // modifier = modifier.size(280.dp, 240.dp)
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                elevation = 8.dp
            ) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {


                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                            .background(Color.Red.copy(alpha = 0.8F)),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,

                        ) {

                        Image(
                            painter = painterResource(id = R.drawable.book_preview),
                            contentDescription = "Exit app",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.FillWidth
                        )
                    }

                    Text(
                        text = "Lorem Ipsum is simply dummy text",
                        modifier = Modifier.padding(8.dp), fontSize = 20.sp
                    )

                    Text(
                        text = stringResource(id = R.string.sure_delete_book),
                        modifier = Modifier.padding(8.dp)
                    )

                    Row(Modifier.padding(top = 10.dp)) {
                        OutlinedButton(
                            onClick = { onDismiss() },
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1F)
                        ) {
                            Text(text = stringResource(id = R.string.cancel))
                        }

                        Button(
                            onClick = { onDelete() },
                            Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .weight(1F),
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)

                        ) {
                            Text(text = stringResource(id = R.string.delete_book), color = Color.White)
                        }
                    }
                }
            }
        }
    }
}