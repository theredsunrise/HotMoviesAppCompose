package com.example.hotmovies.appplication.login.interfaces

import android.graphics.Bitmap
import kotlinx.coroutines.flow.Flow
import java.io.InputStream

interface MovieImageRepositoryInterface {
    fun getImage(id: String): InputStream
    fun getImageAsync(id: String): Flow<Bitmap>
}