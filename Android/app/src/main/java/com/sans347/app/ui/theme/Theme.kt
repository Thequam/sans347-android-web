package com.sans347.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightScheme = lightColorScheme(
    primary = SansColors.PrimaryCyan,
    onPrimary = SansColors.White,
    background = SansColors.PageBackground,
    surface = SansColors.White,
)

@Composable
fun Sans347Theme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightScheme,
        typography = SansTypography,
        content = content,
    )
}
