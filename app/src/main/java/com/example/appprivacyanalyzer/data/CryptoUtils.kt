package com.example.appprivacyanalyzer.utils

import java.security.MessageDigest

object CryptoUtils {

    fun sha256(bytes: ByteArray): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val hash = digest.digest(bytes)
        return hash.joinToString("") { "%02x".format(it) }
    }
}
