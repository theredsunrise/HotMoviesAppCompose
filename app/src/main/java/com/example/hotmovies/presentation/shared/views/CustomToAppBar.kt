package com.example.hotmovies.presentation.shared.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.hotmovies.presentation.theme.HotMoviesAppComposeTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTopAppBar(
    modifier: Modifier,
    title: String,
    isBackButtonAvailable: Boolean,
    windowInsets: WindowInsets = TopAppBarDefaults.windowInsets,
    onBackPress: () -> Unit
) {
    TopAppBar(
        title = { Text(title) },
        modifier,
        colors = TopAppBarDefaults.topAppBarColors().copy(containerColor = Color.Transparent),
        windowInsets = windowInsets,
        navigationIcon = {
            if (isBackButtonAvailable) {
                SafeClickableIconButton(onClick = { onBackPress() }) {
                    val arrowColor = MaterialTheme.colorScheme.primary
                    val backgroundColor = MaterialTheme.colorScheme.background
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        Modifier
                            .drawBehind {
                                drawCircle(backgroundColor)
                            }
                            .padding(5.dp),
                        tint = arrowColor
                    )
                }
            }
        })
}

@Preview
@Composable
private fun PreviewCustomTopAppBar() {
    HotMoviesAppComposeTheme {
        Box(Modifier.background(Color.Red)) {
            CustomTopAppBar(Modifier.fillMaxWidth(), "Toolbar", true) {}
        }
    }

}
