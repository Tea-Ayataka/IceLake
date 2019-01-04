package net.ayataka.marinetooler.utils

import java.net.MalformedURLException
import java.net.URL

fun validateUrl(urlString: String): Boolean {
    try {
        URL(urlString)
    } catch (ex: MalformedURLException) {
        return false
    }

    return true
}