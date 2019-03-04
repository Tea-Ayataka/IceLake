package net.ayataka.marinetooler.gui

import com.teamdev.jxbrowser.chromium.*
import com.teamdev.jxbrowser.chromium.javafx.BrowserView
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Button
import javafx.scene.control.ChoiceBox
import javafx.scene.control.PasswordField
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import net.ayataka.marinetooler.pigg.swf.SWFInjector
import net.ayataka.marinetooler.utils.*
import okhttp3.JavaNetCookieJar
import okhttp3.OkHttpClient
import potato.jx.crack.JxBrowserHackUtil
import potato.jx.crack.JxVersion
import java.awt.Desktop
import java.net.CookieManager
import java.net.CookiePolicy
import java.net.URI
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption
import java.util.*

lateinit var browser: Browser

class BrowserWindow : Initializable {
    // Accounts
    private val accountFilePath = Paths.get("accounts.bin")
    private val accounts: MutableMap<String, String> = mutableMapOf()

    // Root
    @FXML
    lateinit var rootPane: AnchorPane
    lateinit var browserView: BrowserView

    // Login panel
    @FXML
    lateinit var loginPanel: Pane
    @FXML
    lateinit var accountChoiceBox: ChoiceBox<String>
    @FXML
    lateinit var loginButton: Button
    @FXML
    lateinit var removeAccountButton: Button
    @FXML
    lateinit var addAccountButton: Button

    // Register panel
    @FXML
    lateinit var registerPanel: Pane
    @FXML
    lateinit var amebaIdTextField: TextField
    @FXML
    lateinit var passwordField: PasswordField
    @FXML
    lateinit var confirmButton: Button
    @FXML
    lateinit var cancelButton: Button

    init {
        // Load accounts
        if (Files.exists(accountFilePath)) {
            Files.readAllBytes(accountFilePath).cryptXor().toString(Charsets.UTF_8).lines().forEach {
                val amebaId = it.split(":")[0]
                val password = it.split(":")[1]

                accounts[amebaId] = password
                println("ID: $amebaId, Password: $password")
            }
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        // Initialize browser
        JxBrowserHackUtil.hack(JxVersion.V6_22)

        val params = BrowserContextParams("data")
        browser = Browser(BrowserType.HEAVYWEIGHT, BrowserContext(params))

        browser.cacheStorage.clearCache()

        browser.context.protocolService.setProtocolHandler("https") {
            if (it.url.contains("://pigg.ameba.jp/swf/pigg.swf")) {
                return@setProtocolHandler URLResponse().apply {
                    data = SWFInjector.inject(URL(it.url).readBytes())
                    headers.setHeader("Content-Type", "application/x-shockwave-flash")
                }
            }
            return@setProtocolHandler null
        }

        browser.loadHandler = object : DefaultLoadHandler() {
            override fun onLoad(params: LoadParams): Boolean {
                if (params.url.contains("logout")) {
                    info("Canceled loading due to logout")
                    Platform.runLater {
                        browser.loadURL("about:blank")
                        rootPane.children.remove(browserView)
                        loginPanel.isVisible = true
                    }
                    return true
                }
                return false
            }

            override fun onCertificateError(params: CertificateErrorParams?): Boolean {
                return false
            }
        }

        browser.setPopupHandler {
            try {
                if (Desktop.isDesktopSupported()) {
                    Desktop.getDesktop().browse(URI(it.url))
                }
            } catch (ex: Exception) {
            }
            null
        }

        browserView = BrowserView(browser).apply {
            AnchorPane.setTopAnchor(this, 0.0)
            AnchorPane.setBottomAnchor(this, 0.0)
            AnchorPane.setLeftAnchor(this, 0.0)
            AnchorPane.setRightAnchor(this, 0.0)
        }

        // Login panel
        accountChoiceBox.items.addAll(accounts.keys)
        if (accounts.isNotEmpty()) {
            accountChoiceBox.selectionModel.select(0)
        }

        loginButton.setOnAction {
            val amebaId = accountChoiceBox.selectionModel.selectedItem ?: return@setOnAction
            val password = accounts[amebaId] ?: return@setOnAction

            (it.source as Button).isDisable = true

            Thread {
                val cookie = CookieManager(null, CookiePolicy.ACCEPT_ALL)
                val client = OkHttpClient().newBuilder().cookieJar(JavaNetCookieJar(cookie)).build()

                val csrf = client.get("https://dauth.user.ameba.jp/accounts/login").let {
                    Regex("name=\"csrf_token\" value=\"([a-z0-9]+)\"").find(it)!!.groups[1]!!.value
                }

                client.post("https://dauth.user.ameba.jp/accounts/login", "accountId=$amebaId&password=$password&csrf_token=$csrf") {
                    header("Referrer", "https://dauth.user.ameba.jp/login/ameba")
                    header("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/48.0.2564.23 Mobile Safari/537.36")
                }

                client.get("https://pigg.ameba.jp/core/main")

                val N = cookie.cookieStore.cookies.find { it.name == "N" }?.value
                val pigg = cookie.cookieStore.cookies.find { it.name == "pigg" }?.value

                trace("N: $N")
                trace("pigg: $pigg")

                browser.cookieStorage.setSessionCookie("http://ameba.jp", "N", N ?: "", ".ameba.jp", "/", false, false)
                browser.cookieStorage.setSessionCookie("http://pigg.ameba.jp", "pigg", pigg ?: "", ".pigg.ameba.jp", "/", false, false)

                Platform.runLater {
                    loginButton.isDisable = false
                    loginPanel.isVisible = false
                    registerPanel.isVisible = false

                    rootPane.children.add(browserView)

                    browser.loadURL("https://pigg.ameba.jp/core/main")
                }
            }.start()
        }

        addAccountButton.setOnAction {
            loginPanel.isVisible = false
            registerPanel.isVisible = true
        }

        removeAccountButton.setOnAction {
            val amebaId = accountChoiceBox.selectionModel.selectedItem ?: return@setOnAction
            accounts.remove(amebaId)
            accountChoiceBox.items.setAll(accounts.keys)

            saveAccounts()
        }

        // Register panel
        confirmButton.setOnAction {
            val amebaId = amebaIdTextField.text.ifEmpty { null } ?: return@setOnAction
            val password = passwordField.text.ifEmpty { null } ?: return@setOnAction

            accounts[amebaId] = password
            saveAccounts()
            accountChoiceBox.items.setAll(accounts.keys)

            registerPanel.isVisible = false
            loginPanel.isVisible = true
        }

        cancelButton.setOnAction {
            registerPanel.isVisible = false
            loginPanel.isVisible = true
        }
    }

    private fun saveAccounts() {
        val dataToWrite = accounts
                .map { "${it.key}:${it.value}" }
                .joinToString("\n")
                .toByteArray()
                .cryptXor()

        Files.write(accountFilePath, dataToWrite, StandardOpenOption.CREATE)
    }
}