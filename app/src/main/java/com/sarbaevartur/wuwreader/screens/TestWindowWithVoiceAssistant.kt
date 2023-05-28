package com.sarbaevartur.wuwreader.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountBox
import androidx.compose.material.icons.rounded.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sarbaevartur.wuwreader.voice.VoiceToTextParser

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TestWindowWithVoiceAssistant(voiceToText: VoiceToTextParser, canRecord: Boolean) {
    val state by voiceToText.state.collectAsState( )

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (canRecord) {
                        if (!state.isSpeaking) {
                            voiceToText.startListening()
                        } else {
                            voiceToText.stopListening()
                        }
                    }
                }
            ) {
                AnimatedContent(targetState = state.isSpeaking) { isSpeaking ->
                    if (isSpeaking) {
                        Icon(
                            imageVector = Icons.Rounded.AccountBox,
                            contentDescription = null
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding( 20. dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AnimatedContent(targetState = state.isSpeaking) { isSpeaking ->
                if (isSpeaking) {
                    Text(
                        text = "Speak..." ,
                        style = MaterialTheme.typography.h3)
                } else {
                    Text(
                        text = state.spokenText.ifEmpty { "Нажмите на запись"},
                        style = MaterialTheme.typography.h3
                    )
                }
            }
        }
    }
}