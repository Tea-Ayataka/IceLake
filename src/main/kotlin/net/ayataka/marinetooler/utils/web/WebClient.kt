package net.ayataka.marinetooler.utils.web

import java.net.*
import java.nio.charset.Charset

class WebClient(
        val cookie: CookieManager = CookieManager(),
        val timeout: Int = 10000, // 10 sec
        val encoding: String = "UTF-8"
) {
    val headers = hashMapOf<String, String>()

    // FIXME: 後処理してない
    // TODO: エスケープ処理してない
    fun get(link: String): String {
        val domain = URI(URL(link).host)
        val connection: HttpURLConnection = URL(link).openConnection() as HttpURLConnection

        // メソッド・ヘッダーをセットする
        connection.requestMethod = "GET"
        connection.addRequestProperty("Cookie", cookie.cookieStore.get(domain).joinToString { it.name + "=" + it.value + ";" })
        headers.forEach {
            connection.addRequestProperty(it.key, it.value)
        }

        // GET!
        connection.connect()

        // Cookieをパースして保存する
        connection.getHeaderField("Set-Cookie")?.split(";")?.forEach {
            cookie.cookieStore.remove(domain, cookie.cookieStore.get(domain).find { i -> i.name == it.split("=")[0] })
            cookie.cookieStore.add(domain, HttpCookie(it.split("=")[0], it.split("=")[1]))
        }

        return connection.inputStream.readBytes().toString(Charset.forName(encoding))
    }
}