package com.sans347.app.export

import androidx.compose.ui.graphics.Color
import com.sans347.app.ui.theme.SansColors
import com.sans347.app.ui.theme.colorFromHex

object ExportColors {
    const val MAJOR_GRID = "#9CA3AF"
    const val MINOR_GRID = "#C8CCD2"
    const val BOUNDARY_RED = "#DC2626"
    const val GRAY_700 = "#374151"
    const val GRAY_500 = "#6B7280"
    const val AXIS_TITLE = "#1F2937"
    const val WHITE = "#FFFFFF"

    fun lineColor(hex: String?): String = hex ?: BOUNDARY_RED

    fun composeToHex(color: Color): String {
        val r = (color.red * 255).toInt()
        val g = (color.green * 255).toInt()
        val b = (color.blue * 255).toInt()
        return String.format("#%02X%02X%02X", r, g, b)
    }

    fun composeToArgb(color: Color): Int {
        val a = (color.alpha * 255).toInt()
        val r = (color.red * 255).toInt()
        val g = (color.green * 255).toInt()
        val b = (color.blue * 255).toInt()
        return (a shl 24) or (r shl 16) or (g shl 8) or b
    }

    fun parseArgb(hex: String): Int = android.graphics.Color.parseColor(hex)

    fun boundaryRedCompose(): Color = SansColors.BoundaryRed

    fun lineComposeColor(hex: String?): Color = hex?.let { colorFromHex(it) } ?: SansColors.BoundaryRed
}

data class ExportPlotPoint(
    val x: Double,
    val y: Double,
    val colorHex: String,
)
