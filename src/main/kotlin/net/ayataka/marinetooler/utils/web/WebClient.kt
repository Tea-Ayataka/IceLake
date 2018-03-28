package net.ayataka.marinetooler.utils.web

import java.io.IOException
import java.net.*
import java.nio.charset.Charset

class WebClient(
        val cookie: CookieManager = CookieManager(),
        val timeout: Int = 10000, // 10 sec
        val encoding: String = "UTF-8"
) {
    val headers = hashMapOf<String, String>()

    fun get(link: String): String {
        return request(link, RequestMethod.GET)
    }

    fun post(link: String, postData: String) : String {
        return request(link, RequestMethod.POST, postData)
    }

    private fun request(link: String, method: RequestMethod, postData: String? = null): String {
        val connection: HttpURLConnection = URL(link).openConnection() as HttpURLConnection

        connection.requestMethod = method.name
        connection.readTimeout = timeout
        connection.connectTimeout = timeout

        // デフォルトヘッダー
        connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
        connection.addRequestProperty("Accept-Language", "ja,en-US;q=0.8,en;q=0.6")
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36")

        if (method == RequestMethod.POST) {
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        }

        // クッキー
        connection.addRequestProperty("Cookie", cookie.cookieStore.get(URL(link).toURI()).joinToString(separator = "; "))
        println("$link with Cookie ${cookie.cookieStore.get(URL(link).toURI()).joinToString(separator = "; ")}")

        // カスタムヘッダー (オーバーライド)
        headers.forEach {
            connection.addRequestProperty(it.key, it.value)
        }

        // POSTデータを送信
        if (method == RequestMethod.POST) {
            connection.doOutput = true
            connection.outputStream.write(postData?.toByteArray(Charset.forName(encoding)))
        }

        // GET!
        try {
            connection.connect()
        } catch (e: IOException) {
            println("[WebClient] ${e.message}")
        }

        // Cookieをパースして保存する
        connection.headerFields["Set-Cookie"]?.forEach {
            HttpCookie.parse(it).forEach {
                cookie.cookieStore.add(URL(link).toURI(), it)
                println("SetCookie $it")
            }
        }

        // リファラーをセット
        headers["Referer"] = link

        println("${connection.responseCode} ${connection.responseMessage}")

        // リダイレクト
        if (connection.responseCode == 302 || connection.responseCode == 301) {
            return this.request(connection.getHeaderField("Location"), RequestMethod.GET, postData)
        }

        return connection.inputStream.readBytes().toString(Charset.forName(encoding))
    }
}

enum class RequestMethod {
    GET, POST
}

// test
fun main(args: Array<String>) {
    val client = WebClient(cookie = CookieManager())
    println(client.get("https://httpbin.org/get?test=ok"))
    println(client.post("https://httpbin.org/anything",  "isHot=true&isCold=false"))
    println(client.get("https://dauth.user.ameba.jp/login/ameba"))
}