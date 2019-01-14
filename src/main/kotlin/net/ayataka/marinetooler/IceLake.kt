package net.ayataka.marinetooler

import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.stage.Stage
import net.ayataka.marinetooler.browser.InternetExplorer
import net.ayataka.marinetooler.gui.MainWindow
import net.ayataka.marinetooler.pigg.Pigg
import net.ayataka.marinetooler.proxy.http.HttpProxy
import net.ayataka.marinetooler.utils.info
import java.net.URL

// Singleton
lateinit var ICE_LAKE: IceLake

class IceLake : Application() {
    val oldAreas = hashMapOf<String, String>()
    var browser: InternetExplorer? = null

    @Volatile
    var mainWindow: MainWindow? = null

    @Volatile
    var targetUser = ""

    init {
        ICE_LAKE = this
    }

    override fun start(stage: Stage) {
        info("Starting IceLake")

        // Show main window
        val scene = Scene(FXMLLoader.load(javaClass.classLoader.getResource("MainWindow.fxml")))
        //scene.stylesheets.add("modena_dark.css")
        stage.scene = scene
        stage.title = "IceLake (BETA)"
        stage.isResizable = false
        stage.height = 390.toDouble()
        stage.width = 600.toDouble()
        stage.isAlwaysOnTop = true
        stage.icons.add(Image("icon.png"))
        stage.setOnHidden {
            browser?.shutdown()
            System.exit(0)
        }
        stage.show()

        // Initialize Pigg instance
        Pigg

        // Start Http Proxy
        HttpProxy(8080, true).start()

        // Start chromium browser
        browser = InternetExplorer()

        Thread {
            for (line in URL("https://www.dropbox.com/s/pw0thqb8ak118c3/areacode.txt?dl=1").readText(Charsets.UTF_8).lines()) {
                try {
                    val name = line.split(":")[0]
                    val code = line.split(":")[1]

                    oldAreas[name] = code
                } catch (ex: IndexOutOfBoundsException) {
                    ex.printStackTrace()
                    println(line)
                }
            }

            while (mainWindow == null) {
                Thread.sleep(100)
            }

            Platform.runLater {
                mainWindow?.areaList?.items?.clear()
                mainWindow?.areaList?.items?.addAll(oldAreas.keys)
                mainWindow?.areaList?.items?.sort()
            }
        }.start()
    }

    fun showError(msg: String) {
        val alert = Alert(Alert.AlertType.ERROR, msg, ButtonType.OK)
        alert.headerText = null
        val stage = alert.dialogPane.scene.window as Stage
        stage.isAlwaysOnTop = true
        alert.showAndWait()
    }

    fun showMessage(msg: String) {
        val alert = Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK)
        alert.headerText = null
        val stage = alert.dialogPane.scene.window as Stage
        stage.isAlwaysOnTop = true
        alert.showAndWait()
    }
}