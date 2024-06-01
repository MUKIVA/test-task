package com.github.mukiva.testtask.data.utils

import java.security.MessageDigest
import java.util.UUID

object HashGenerator {
    fun generate(): String {
        val uuid = UUID.randomUUID().toString()
        return getHash(uuid)
    }

    private fun getHash(plainText: String, algorithm: String = "SHA-256"): String {
        val bytes = MessageDigest.getInstance(algorithm).digest(plainText.toByteArray())
        return toHex(bytes)
    }

    private fun toHex(byteArray: ByteArray): String {
        return byteArray.joinToString("") { "%02x".format(it) }
    }
}