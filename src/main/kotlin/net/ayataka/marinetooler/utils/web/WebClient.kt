package net.ayataka.marinetooler.utils.web

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
        val domain = URI(URL(link).host)
        val connection: HttpURLConnection = URL(link).openConnection() as HttpURLConnection

        // メソッド・ヘッダーをセットする
        connection.requestMethod = method.name

        // デフォルトヘッダー
        connection.addRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8")
        connection.addRequestProperty("Accept-Language", "en-US;q=0.8,en;q=0.6")
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/60.0.3112.113 Safari/537.36")

        if (method == RequestMethod.POST) {
            connection.addRequestProperty("Content-Type", "application/x-www-form-urlencoded")
        }

        // クッキー
        connection.addRequestProperty("Cookie", cookie.cookieStore.get(domain).joinToString { it.name + "=" + it.value + ";" })

        // カスタムヘッダー (オーバーライド)
        headers.forEach {
            connection.addRequestProperty(it.key, it.value)
        }

        if (method == RequestMethod.POST) {
            connection.doOutput = true
            connection.outputStream.write(postData?.toByteArray(Charset.forName(encoding)))
        }

        // GET!
        connection.connect()

        // Cookieをパースして保存する
        connection.headerFields["Set-Cookie"]?.forEach {
            val elements = it.split(";")
            val insert = HttpCookie(elements.first().split("=")[0], elements.first().split("=")[1])

            // TODO: ちゃんとぱーすする
            /* elements.drop(1).forEach {

            } */

            cookie.cookieStore.get(domain).find { i -> i.name == it.split("=")[0] }?.let {
                cookie.cookieStore.remove(domain, it)
            }
            cookie.cookieStore.add(domain, insert)
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