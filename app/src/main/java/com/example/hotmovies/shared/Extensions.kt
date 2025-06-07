package com.example.hotmovies.shared

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import android.util.Base64
import androidx.paging.LoadState
import okhttp3.ResponseBody
import kotlin.experimental.xor

fun checkMainThread() {
    if (System.getProperty("isTest") == null) {
        assert(Looper.myLooper() == Looper.getMainLooper())
    }
}

fun checkNotMainThread() {
    if (System.getProperty("isTest") == null) {
        assert(Looper.myLooper() != Looper.getMainLooper())
    }
}

//xor string with a char key
fun String.toUse(value: Byte): String {
    return Base64.decode(this, Base64.DEFAULT).map { it xor value }.toByteArray()
        .toString(Charsets.UTF_8)
}

fun String.fromBase64ToByteArray(): ByteArray {
    return Base64.decode(this, Base64.DEFAULT)
}

fun ByteArray.toBase64(): String {
    return Base64.encodeToString(this, Base64.DEFAULT)
}

fun ResponseBody.toBitmap(): Bitmap {
    return byteStream().use { BitmapFactory.decodeStream(it) }
}

val LoadState.isLoading: Boolean get() = this is LoadState.Loading
val LoadState.failure: Throwable? get() = (this as? LoadState.Error)?.let { it.error }

fun Exception.getMessage(): String {
    return localizedMessage ?: message ?: this::class.simpleName ?: "Unknown error."
}