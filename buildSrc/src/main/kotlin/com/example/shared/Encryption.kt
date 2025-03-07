package com.example.shared

import kotlin.experimental.xor
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

@OptIn(ExperimentalEncodingApi::class)
fun String.encrypt(xorKey: Byte): String {
    return this.toByteArray(Charsets.UTF_8).map { it xor xorKey }.toByteArray()
        .let { Base64.encode(it) }
}
