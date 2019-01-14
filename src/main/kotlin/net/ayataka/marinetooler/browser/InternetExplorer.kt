package net.ayataka.marinetooler.browser

import com.teamdev.jxbrowser.chromium.*
import com.teamdev.jxbrowser.chromium.swing.BrowserView
import com.teamdev.jxbrowser.chromium.swing.DefaultNetworkDelegate
import potato.jx.crack.JxBrowserHackUtil
import potato.jx.crack.JxVersion
import java.awt.BorderLayout
import javax.swing.JFrame
import javax.swing.WindowConstants

class InternetExplorer {
    private var process: Process? = null
    private val frame: JFrame

    init {
        JxBrowserHackUtil.hack(JxVersion.V6_18)

        val params = BrowserContextParams("data")
        params.setProxyConfig(CustomProxyConfig("http=localhost:8080;https=localhost:8080"))
        val browser = Browser(BrowserContext(params))

        browser.context.networkService.networkDelegate =  object : DefaultNetworkDelegate() {
            override fun onBeforeURLRequest(params: BeforeURLRequestParams?) {
                if (params == null) {
                    return
                }

                if(params.url.startsWith("https://pigg.ameba.jp/swf/pigg.swf")){
                    params.url = params.url.replace("https://", "http://")
                    println("Changed the url")
                }
            }
        }

        val view = BrowserView(browser)

        frame = JFrame().apply {
            defaultCloseOperation = WindowConstants.EXIT_ON_CLOSE
            add(view, BorderLayout.CENTER)
            setSize(1200, 720)
            isVisible = true
        }

        browser.loadURL("https://pigg.ameba.jp/")
    }

    fun shutdown() {
        process?.destroy()
    }
}