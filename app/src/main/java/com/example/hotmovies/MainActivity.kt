package com.example.hotmovies

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.MotionEvent
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.hotmovies.presentation.login.navigation.LoginGraph
import com.example.hotmovies.presentation.login.navigation.loginGraph
import com.example.hotmovies.presentation.movies.navigation.moviesGraph
import com.example.hotmovies.presentation.shared.LocalSharedTransitionScope
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme

interface UserInteractionConfigurableComponent {
    var isEnabled: Boolean
}

class MainActivity : ComponentActivity(), UserInteractionConfigurableComponent {

    override var isEnabled: Boolean = true

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return if (isEnabled) super.dispatchTouchEvent(ev) else true
    }

    @SuppressLint("RestrictedApi")
    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        return if (isEnabled) super.dispatchKeyEvent(event) else true
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        } else {
            window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    @OptIn(ExperimentalSharedTransitionApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HotMoviesAppComposeTheme {
                Surface(Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    SharedTransitionLayout(Modifier.fillMaxSize()) {
                        CompositionLocalProvider(
                            LocalSharedTransitionScope provides this
                        ) {
                            NavHost(
                                navController, startDestination = LoginGraph,
                                enterTransition = { EnterTransition.None },
                                exitTransition = { ExitTransition.None },
                                popExitTransition = { ExitTransition.None },
                                popEnterTransition = { EnterTransition.None }
                            )
                            {
                                loginGraph(getString(R.string.redirect_uri), navController)
                                moviesGraph(navController)
                            }
                        }
                    }
                }
            }
        }
    }
}