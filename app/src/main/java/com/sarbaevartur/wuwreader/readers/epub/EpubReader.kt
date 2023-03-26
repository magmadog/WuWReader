package com.sarbaevartur.wuwreader.readers.epub

import android.net.Uri
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.apache.tika.parser.epub.EpubParser
import org.apache.tika.sax.BodyContentHandler
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import java.io.InputStream


class EpubReader {

    @Composable
    fun GetEpubText(filePath: String) {

        val context = LocalContext.current

        val inputStream: InputStream? = context.contentResolver.openInputStream(Uri.parse(filePath)!!)
        val handler = BodyContentHandler()
        val metadata = Metadata()
        val parser = EpubParser()
        val parseContext = ParseContext()
        parser.parse(inputStream, handler, metadata, parseContext)
        val content: String = handler.toString()
        Text(text = content)
    }
}