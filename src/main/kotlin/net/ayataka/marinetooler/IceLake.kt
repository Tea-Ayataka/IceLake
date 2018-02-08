package net.ayataka.marinetooler

import javafx.application.Application
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

// Singleton
val Tooler = IceLake()

class IceLake : Application() {
    var browser: InternetExplorer? = null
    var mainWindow: MainWindow? = null
    var targetUser = ""

    override fun start(stage: Stage) {
        info("Starting IceLake v0.1")

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
            this.browser?.shutdown()
            System.exit(0)
        }
        stage.show()

        // Initialize Pigg instance
        Pigg

        // HTTP Interceptor
        HttpProxy(8080, false).start()

        // Start chromium browser
        this.browser = InternetExplorer()
    }

    fun showError(msg: String) {
        val alert = Alert(Alert.AlertType.ERROR, msg, ButtonType.OK)
        alert.headerText = null
        val stage = alert.dialogPane.scene.window as Stage
        stage.isAlwaysOnTop = true
        alert.showAndWait()
    }
}