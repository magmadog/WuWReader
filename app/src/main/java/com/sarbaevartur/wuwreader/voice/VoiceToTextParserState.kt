package com.sarbaevartur.wuwreader.voice

data class VoiceToTextParserState(
    val isSpeaking: Boolean = false,
    var spokenText: String = "",
    val error: String? = null
)