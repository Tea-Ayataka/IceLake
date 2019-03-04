package net.ayataka.marinetooler

import com.google.gson.Gson
import javafx.application.Application
import javafx.application.Platform
import javafx.fxml.FXMLLoader
import javafx.scene.Scene
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.image.Image
import javafx.stage.Stage
import net.ayataka.marinetooler.gui.MainWindow
import net.ayataka.marinetooler.gui.browser
import net.ayataka.marinetooler.pigg.PiggProxy
import net.ayataka.marinetooler.utils.IceLakeApi
import net.ayataka.marinetooler.utils.info
import java.io.File

// Singleton
lateinit var ICE_LAKE: IceLake

class IceLake : Application() {
    private val configFile = File("config.json").apply { if (!exists()) createNewFile() }
    val config = Gson().fromJson(configFile.readText(), Config::class.java) ?: Config()

    val actions = hashMapOf<String, String>()
    val shops = hashMapOf<String, String>()
    val oldAreas = hashMapOf<String, String>()

    @Volatile
    var mainWindow: MainWindow? = null

    @Volatile
    var targetUser = ""

    init {
        ICE_LAKE = this
    }

    override fun start(stage: Stage) {
        try {
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
                Thread {
                    browser.dispose()
                    System.exit(0)
                }.start()
            }
            stage.show()

            // Initialize Pigg proxy instance
            PiggProxy

            // Start chromium browser
            try {
                val resource = javaClass.classLoader.getResource("BrowserWindow.fxml")
                val browserScene = Scene(FXMLLoader.load(resource))
                browserScene.stylesheets.add("flatsharp.css")

                Stage().apply {
                    setScene(browserScene)
                    title = "IceLake Player"

                    setOnHidden {
                        Thread {
                            browser.dispose()
                            System.exit(0)
                        }.start()
                    }
                }.show()
            } catch (ex: Exception) {
                ex.printStackTrace()
            }

            Thread {
                oldAreas.putAll(IceLakeApi.getAreas())
                actions.putAll(IceLakeApi.getActions())

                while (mainWindow == null) {
                    Thread.sleep(100)
                }

                Platform.runLater {
                    mainWindow?.areaList?.items?.clear()
                    mainWindow?.areaList?.items?.addAll(oldAreas.values)
                    mainWindow?.areaList?.items?.sort()
                }
            }.start()
        } catch (ex: Exception) {
            ex.printStackTrace()
            showError("エラー: ${ex.message}")
        }
    }

    fun saveConfig() {
        configFile.writeText(Gson().toJson(config))
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