package com.sarbaevartur.wuwreader.readers.epub

import android.content.Context
import android.net.Uri
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.epub.EpubParser
import org.apache.tika.sax.BodyContentHandler
import java.io.InputStream

class EpubReader {

    fun getEpubText(filePath: String, context: Context): String {

        val inputStream: InputStream? = context.contentResolver.openInputStream(Uri.parse(filePath)!!)
        val handler = BodyContentHandler(-1)
        val metadata = Metadata()
        val parser = EpubParser()
        val parseContext = ParseContext()
        parser.parse(inputStream, handler, metadata, parseContext)
        return handler.toString()
    }
}