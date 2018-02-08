package net.ayataka.marinetooler.utils

import net.ayataka.marinetooler.pigg.Pigg
import java.io.File
import java.io.FileInputStream
import java.security.KeyStore
import java.security.SecureRandom
import javax.net.ssl.KeyManagerFactory
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

fun getSSLContextFromPFXFile(path: String, certPath: String, pass: String): SSLContext {
    val keystore = KeyStore.getInstance("PKCS12")
    keystore.load(Pigg.javaClass.classLoader.getResourceAsStream(path), pass.toCharArray())

    val factory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm())
    factory.init(keystore, pass.toCharArray())

    val trustStore = KeyStore.getInstance("JKS")
    trustStore.load(Pigg.javaClass.classLoader.getResourceAsStream(certPath), null)

    val trustManager = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManager.init(trustStore)

    val context = SSLContext.getInstance("TLS")
    context.init(factory.keyManagers, trustManager.trustManagers, SecureRandom())

    return context
}