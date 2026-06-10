package com.sans347.app.ui.theme

import androidx.compose.ui.graphics.Color

fun colorFromHex(hex: String): Color {
    val h = hex.removePrefix("#")
    val v = h.toLong(16)
    return when (h.length) {
        6 -> Color(0xFF000000L or v)
        8 -> Color(v)
        else -> Color.Black
    }
}

object SansColors {
    val PrimaryCyan = Color(0xFF00C2FF)
    val DarkBackground = Color(0xFF0F0F0F)
    val PageBackground = Color(0xFFF3F4F6)
    val White = Color(0xFFFFFFFF)
    val MediumGray = Color(0xFF353E43)
    val DarkGray = Color(0xFF4A5568)
    val Gray700 = Color(0xFF374151)
    val Gray600 = Color(0xFF4B5563)
    val Gray500 = Color(0xFF6B7280)
    val Gray400 = Color(0xFF9CA3AF)
    val Gray300 = Color(0xFFD1D5DB)
    val Gray200 = Color(0xFFE5E7EB)
    val Gray50 = Color(0xFFF9FAFB)
    val DarkBlueGray = Color(0xFF111827)
    val NavBorder = Color(0xFF333333)
    val MajorGrid = Color(0xFF9CA3AF)
    val MinorGrid = Color(0xFFC8CCD2)
    val BoundaryRed = Color(0xFFDC2626)
    val ApplicationBg = Color(0xFFEFF6FF)
    val ApplicationBorder = Color(0xFFBFDBFE)
    val ApplicationLabel = Color(0xFF2563EB)
    val WarningBg = Color(0xFFFEFCE8)
    val WarningBorder = Color(0xFFFDE68A)
    val WarningIcon = Color(0xFFD97706)
    val Success = Color(0xFF10B981)
    val ThumbSelectedBg = Color(0xFFECFEFF)
    val AxisTitle = Color(0xFF1F2937)
    val ScrollTrack = Color(0xFF1F2937)
    val ScrollThumb = Color(0xFF4B5563)
    val CategoryI = Color(0xFF3B82F6)
    val CategoryII = Color(0xFFEAB308)
    val CategoryIII = Color(0xFFF97316)
    val CategoryIV = Color(0xFFEF4444)
}
