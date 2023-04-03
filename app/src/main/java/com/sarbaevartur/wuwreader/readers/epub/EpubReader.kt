package com.sarbaevartur.wuwreader.readers.epub

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import org.apache.tika.metadata.Metadata
import org.apache.tika.parser.ParseContext
import org.apache.tika.parser.epub.EpubParser
import org.apache.tika.sax.BodyContentHandler
import java.io.InputStream


class EpubReader {

    @Composable
    fun GetEpubText(filePath: String) {

        val context = LocalContext.current

        val inputStream: InputStream? = context.contentResolver.openInputStream(Uri.parse(filePath)!!)
        val handler = BodyContentHandler(-1)
        val metadata = Metadata()
        val parser = EpubParser()
        val parseContext = ParseContext()
        parser.parse(inputStream, handler, metadata, parseContext)
        val content: String = handler.toString()
        EpubViewer().BookContent(book = content)

//        val sw = StringWriter()
//        val factory: SAXTransformerFactory =
//            SAXTransformerFactory.newInstance() as SAXTransformerFactory
//        val handler: TransformerHandler = factory.newTransformerHandler()
//        handler.getTransformer().setOutputProperty(OutputKeys.METHOD, "xml")
//        handler.getTransformer().setOutputProperty(OutputKeys.INDENT, "no")
//        handler.setResult(StreamResult(sw))
//
//        parser.parse(inputStream, handler, metadata, ParseContext())
//
//        val xhtml: String = sw.toString()
//        EpubViewer().BookContent(book = xhtml)
    }
}