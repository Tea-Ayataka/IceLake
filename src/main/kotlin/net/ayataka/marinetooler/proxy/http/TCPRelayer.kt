package net.ayataka.marinetooler.proxy.http

import net.ayataka.marinetooler.utils.info
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.net.Socket

class TCPRelayer(
        private val readFrom: Socket,
        private val writeTo: Socket
) : Thread() {
    override fun run() {
        var bufferedLength = 0
        val buffer = ByteArray(1024)

        val nameFrom = this.readFrom.inetAddress.toString()
        val nameTo = this.writeTo.inetAddress.toString()

        val inputStream = BufferedInputStream(this.readFrom.getInputStream())
        val outputStream = BufferedOutputStream(this.writeTo.getOutputStream())

        try {
            while (!this.readFrom.isClosed && !this.writeTo.isClosed && { bufferedLength = inputStream.read(buffer); bufferedLength }() != -1) {
                outputStream.write(buffer, 0, bufferedLength)
                outputStream.flush()
            }
        } catch (ex: Exception) {
            //ex.printStackTrace()
        } finally {
            try {
                inputStream.close()
                outputStream.close()
            } catch (ignore: Exception) {
            }
        }
    }
}