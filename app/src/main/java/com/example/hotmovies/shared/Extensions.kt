package com.example.hotmovies.shared

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Looper
import androidx.paging.LoadState
import okhttp3.ResponseBody
import kotlin.experimental.xor
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi


fun checkMainThread() = assert(Looper.myLooper() == Looper.getMainLooper())
fun checkNotMainThread() = assert(Looper.myLooper() != Looper.getMainLooper())

//xor string with a char key
@OptIn(ExperimentalEncodingApi::class)
fun String.toUse(value: Byte): String {
    return Base64.decode(this).map { it xor value }.toByteArray().toString(Charsets.UTF_8)
}

fun ResponseBody.toBitmap(): Bitmap {
    return byteStream().use { BitmapFactory.decodeStream(it) }
}

val LoadState.isLoading: Boolean get() = this is LoadState.Loading
val LoadState.failure: Throwable? get() = (this as? LoadState.Error)?.let { it.error }