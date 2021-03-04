package xyz.sushiland.gardenia.mo

import javafx.scene.control.Alert
import tornadofx.alert
import java.io.File
import java.io.InputStream
import java.io.OutputStream
import java.lang.RuntimeException
import java.util.zip.ZipFile

class FileOperation {
    companion object {
        fun movePlugin(dir: String, pgnDir: String) : Unit?{
            val file = File(dir)
            if (file.isDirectory) throw RuntimeException("Can not get a plugin from a directory.")
            return when(file.extension) {
                "dll" -> {
                    val newFile = File("$pgnDir/${file.name}")
                    file.copyTo(newFile)
                    file.delete()
                    Unit
                }
                "zip" -> {
                    val zipFile = ZipFile(dir)
                    zipFile.entries().asIterator().forEach {
                        if (it.isDirectory) {
                            File("$pgnDir/${it.name}").mkdirs()
                        } else {
                            val input = zipFile.getInputStream(it)
                            val outputFile = File("$pgnDir/${it.name}")
                            if (!outputFile.exists()) outputFile.createNewFile()
                            val outputStream = outputFile.outputStream()
                            input.writeTo(outputStream)
                        }
                    }
                    Unit
                }
                else -> {
                    null
                }
            }
        } // movePlugin
        fun InputStream.writeTo(outputStream: OutputStream, bufferedSize: Int = DEFAULT_BUFFER_SIZE,
                               closeIn: Boolean = true, closeOut: Boolean = true) {
            val buffer = ByteArray(bufferedSize)
            val inBuffered = this.buffered()
            val outBuffered = outputStream.buffered()
            var length = inBuffered.read(buffer)

            while (length != -1) {
                outBuffered.write(buffer)
                length = inBuffered.read(buffer)
            }

            outBuffered.flush()

            if (closeIn) {
                this.close()
            }

            if (closeOut) {
                outputStream.close()
            }

        }
    }
}