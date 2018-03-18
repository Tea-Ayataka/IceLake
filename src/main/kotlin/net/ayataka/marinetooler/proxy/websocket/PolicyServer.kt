package net.ayataka.marinetooler.proxy.websocket

import net.ayataka.marinetooler.Tooler
import net.ayataka.marinetooler.utils.fromHexToBytes
import net.ayataka.marinetooler.utils.info
import net.ayataka.marinetooler.utils.warn
import java.io.IOException
import java.net.BindException
import java.net.ServerSocket

class PolicyServer : Thread() {
    private var serverSocket: ServerSocket? = null

    init {
        try {
            serverSocket = ServerSocket(843)
        } catch (ex: BindException) {
            Tooler.showError("ポリシーサーバーの初期化に失敗しました。ポート 843 が既に利用されています。")
            System.exit(1)
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun run() {
        // Infinite loop until this thread is canceled.
        while (!Thread.interrupted()) {
            try {
                val socket = serverSocket?.accept()

                val data = ("3C3F786D6C2076657273696F6E3D22312E30223F3E3C21444F43545950452063726F73732D646F6D61696E2D706F6C6963792053595354454D2022687474703A2F2F7777772E61646F62652E636F6D2F786D6C2F647464732F63726F73732D646F6D61696E2D706F6C6963792E647464223E3C63726F73732D646F6D61696E2D706F6C6963793E3C616C6C6F772D6163636573732D66726F6D20646F6D61696E3D272A2720746F2D706F7274733D272A272F3E3C2F63726F73732D646F6D61696E2D706F6C6963793E00").fromHexToBytes()
                socket?.getOutputStream()?.write(data)
               // socket?.close()
                info("[WS POLICY] SENT")
            } catch (ex: InterruptedException) {
                break
            } catch (ex: IOException) {
                warn("IO ERROR occurred during connection thread initialization!")
                ex.printStackTrace()
                break
            }
        }
    }
}
