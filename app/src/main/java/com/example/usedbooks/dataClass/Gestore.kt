package com.example.usedbooks.dataClass

import java.nio.charset.StandardCharsets.UTF_8
import java.security.MessageDigest

abstract class Gestore {
    companion object {
        val md = MessageDigest.getInstance("SHA-256")

        fun ByteArray.toHex() = joinToString(separator = "") {
            "%02x".format(it)
        }

        fun getHash(input : String) : String {
            return md.digest(input.toByteArray(UTF_8)).toHex()
        }

        fun checkHash(input: String, daCheck : String) : Boolean {
            return getHash(input).equals(daCheck)
        }
    }
}