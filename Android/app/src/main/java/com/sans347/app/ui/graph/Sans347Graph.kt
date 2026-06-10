package com.sans347.app.ui.graph

import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import com.sans347.app.data.GraphConfig
import com.sans347.app.ui.theme.SansColors
import com.sans347.app.ui.theme.colorFromHex
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

data class PlotPointData(
    val x: Double,
    val y: Double,
    val color: Color,
)

private fun formatAxisLabel(value: Double): String {
    if (value >= 1000) {
        val s = String.format("%,.0f", value).replace(',', ' ')
        return s
    }
    return value.toString().replace('.', ',')
}

@Composable
fun Sans347Graph(
    config: GraphConfig,
    plotPoint: PlotPointData? = null,
    fitContainer: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Canvas(
        modifier = if (fitContainer) {
            modifier.fillMaxSize()
        } else {
            modifier
                .fillMaxWidth()
                .aspectRatio(GRAPH_ASPECT_RATIO)
        },
    ) {
        val w = size.width
        val h = size.height
        val sf = w / 750f

        val marginTop = 20f * sf
        val marginRight = 30f * sf
        val marginBottom = 60f * sf
        val marginLeft = 80f * sf
        val plotW = w - marginLeft - marginRight
        val plotH = h - marginTop - marginBottom

        fun logX(v: Double): Float {
            val minLog = log10(config.xMin)
            val maxLog = log10(config.xMax)
            val vv = max(v, config.xMin)
            return marginLeft + ((log10(vv) - minLog) / (maxLog - minLog)).toFloat() * plotW
        }

        fun logY(v: Double): Float {
            val minLog = log10(config.yMin)
            val maxLog = log10(config.yMax)
            val vv = max(v, config.yMin)
            return marginTop + plotH - ((log10(vv) - minLog) / (maxLog - minLog)).toFloat() * plotH
        }

        drawRect(SansColors.White)

        val yMinLog = kotlin.math.floor(log10(config.yMin)).toInt()
        val yMaxLog = kotlin.math.ceil(log10(config.yMax)).toInt()
        for (e in yMinLog..yMaxLog) {
            val `val` = 10.0.pow(e)
            if (`val` >= config.yMin && `val` <= config.yMax) {
                val py = logY(`val`)
                drawLine(
                    SansColors.MajorGrid,
                    Offset(marginLeft, py),
                    Offset(marginLeft + plotW, py),
                    strokeWidth = 0.5f,
                )
            }
            for (m in 2..9) {
                val mval = `val` * m
                if (mval >= config.yMin && mval <= config.yMax) {
                    val py = logY(mval)
                    drawLine(
                        SansColors.MinorGrid,
                        Offset(marginLeft, py),
                        Offset(marginLeft + plotW, py),
                        strokeWidth = 0.5f,
                    )
                }
            }
        }

        val xMinLog = kotlin.math.floor(log10(config.xMin)).toInt()
        val xMaxLog = kotlin.math.ceil(log10(config.xMax)).toInt()
        for (e in xMinLog..xMaxLog) {
            val `val` = 10.0.pow(e)
            if (`val` >= config.xMin && `val` <= config.xMax) {
                val px = logX(`val`)
                drawLine(
                    SansColors.MajorGrid,
                    Offset(px, marginTop),
                    Offset(px, marginTop + plotH),
                    strokeWidth = 0.5f,
                )
            }
            for (m in 2..9) {
                val mval = `val` * m
                if (mval >= config.xMin && mval <= config.xMax) {
                    val px = logX(mval)
                    drawLine(
                        SansColors.MinorGrid,
                        Offset(px, marginTop),
                        Offset(px, marginTop + plotH),
                        strokeWidth = 0.5f,
                    )
                }
            }
        }

        drawRect(
            color = SansColors.Gray700,
            topLeft = Offset(marginLeft, marginTop),
            size = Size(plotW, plotH),
            style = Stroke(width = 1f),
        )

        for (line in config.lines) {
            val x1p = logX(line.x1)
            val y1p = logY(line.y1)
            val x2p = logX(line.x2)
            val y2p = logY(line.y2)
            val lx1 = max(marginLeft, min(marginLeft + plotW, x1p))
            val ly1 = max(marginTop, min(marginTop + plotH, y1p))
            val lx2 = max(marginLeft, min(marginLeft + plotW, x2p))
            val ly2 = max(marginTop, min(marginTop + plotH, y2p))
            val lineColor = line.color?.let { colorFromHex(it) } ?: SansColors.BoundaryRed
            drawLine(
                lineColor,
                Offset(lx1, ly1),
                Offset(lx2, ly2),
                strokeWidth = 1.5f,
            )

            line.label?.let { labelText ->
                val labelPosX = line.labelX?.let { logX(it) } ?: (x1p + x2p) / 2f
                val labelPosY = line.labelY?.let { logY(it) } ?: (y1p + y2p) / 2f
                val angleRad = atan2((y2p - y1p).toDouble(), (x2p - x1p).toDouble())
                val angleDeg = (angleRad * 180.0 / PI).toFloat()
                val maxY = max(line.y1, line.y2)
                val maxX = max(line.x1, line.x2)
                val isHorizontal = abs(line.y1 - line.y2) < 0.01 * maxY
                val isVertical = abs(line.x1 - line.x2) < 0.01 * maxX
                val fillCol = if (isHorizontal || isVertical) SansColors.Gray700 else SansColors.BoundaryRed
                val textSizePx = max(8f, 10f * sf)
                val baseOffset = -6f * sf
                val extraOffset = (line.labelOffset ?: 0.0).toFloat() * sf

                drawIntoCanvas { canvas ->
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.argb(
                            (fillCol.alpha * 255).toInt(),
                            (fillCol.red * 255).toInt(),
                            (fillCol.green * 255).toInt(),
                            (fillCol.blue * 255).toInt(),
                        )
                        textSize = textSizePx
                        textAlign = Paint.Align.CENTER
                    }
                    canvas.nativeCanvas.save()
                    canvas.nativeCanvas.translate(labelPosX, labelPosY)
                    when {
                        !isHorizontal && !isVertical -> canvas.nativeCanvas.rotate(angleDeg)
                        isVertical -> canvas.nativeCanvas.rotate(-90f)
                    }
                    canvas.nativeCanvas.drawText(labelText, 0f, baseOffset + extraOffset, paint)
                    canvas.nativeCanvas.restore()
                }
            }
        }

        for (zone in config.categoryZones) {
            val zx = logX(zone.x)
            val zy = logY(zone.y)
            if (zx > marginLeft && zx < marginLeft + plotW && zy > marginTop && zy < marginTop + plotH) {
                val textSizePx = max(10f, 14f * sf)
                drawIntoCanvas { canvas ->
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.parseColor("#6b7280")
                        textSize = textSizePx
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                        textAlign = Paint.Align.CENTER
                    }
                    canvas.nativeCanvas.drawText(zone.label, zx, zy, paint)
                }
            }
        }

        val axisTextSize = max(9f, 11f * sf)
        for (e in yMinLog..yMaxLog) {
            val `val` = 10.0.pow(e)
            if (`val` >= config.yMin && `val` <= config.yMax) {
                val py = logY(`val`)
                val label = formatAxisLabel(`val`)
                drawIntoCanvas { canvas ->
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.parseColor("#374151")
                        textSize = axisTextSize
                        textAlign = Paint.Align.RIGHT
                    }
                    canvas.nativeCanvas.drawText(label, marginLeft - 8f * sf, py + 4f, paint)
                }
            }
        }

        for (e in xMinLog..xMaxLog) {
            val `val` = 10.0.pow(e)
            if (`val` >= config.xMin && `val` <= config.xMax) {
                val px = logX(`val`)
                val label = formatAxisLabel(`val`)
                drawIntoCanvas { canvas ->
                    val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.parseColor("#374151")
                        textSize = axisTextSize
                        textAlign = Paint.Align.CENTER
                    }
                    canvas.nativeCanvas.drawText(label, px, marginTop + plotH + 18f * sf, paint)
                }
            }
        }

        val titleSize = max(10f, 12f * sf)
        drawIntoCanvas { canvas ->
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.parseColor("#1f2937")
                textSize = titleSize
                typeface = android.graphics.Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }
            canvas.nativeCanvas.drawText(
                config.xAxisLabel,
                marginLeft + plotW / 2f,
                h - 10f * sf,
                paint,
            )
        }
        drawIntoCanvas { canvas ->
            val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = android.graphics.Color.parseColor("#1f2937")
                textSize = titleSize
                typeface = android.graphics.Typeface.DEFAULT_BOLD
                textAlign = Paint.Align.CENTER
            }
            canvas.nativeCanvas.save()
            canvas.nativeCanvas.translate(15f * sf, marginTop + plotH / 2f)
            canvas.nativeCanvas.rotate(-90f)
            canvas.nativeCanvas.drawText(config.yAxisLabel, 0f, 0f, paint)
            canvas.nativeCanvas.restore()
        }

        plotPoint?.let { pt ->
            val px = logX(pt.x)
            val py = logY(pt.y)
            if (px >= marginLeft && px <= marginLeft + plotW && py >= marginTop && py <= marginTop + plotH) {
                val dash = PathEffect.dashPathEffect(floatArrayOf(4f, 4f), 0f)
                drawLine(
                    pt.color,
                    Offset(px, marginTop),
                    Offset(px, marginTop + plotH),
                    strokeWidth = 1.5f,
                    pathEffect = dash,
                )
                drawLine(
                    pt.color,
                    Offset(marginLeft, py),
                    Offset(marginLeft + plotW, py),
                    strokeWidth = 1.5f,
                    pathEffect = dash,
                )
                val r = max(5f, 8f * sf)
                drawCircle(pt.color, r, Offset(px, py))
                drawCircle(
                    SansColors.White,
                    r,
                    Offset(px, py),
                    style = Stroke(width = 2f),
                )

                val labelText = "Your Equipment"
                val labelSize = max(9f, 11f * sf)
                drawIntoCanvas { canvas ->
                    val tp = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.argb(
                            (pt.color.alpha * 255).toInt(),
                            (pt.color.red * 255).toInt(),
                            (pt.color.green * 255).toInt(),
                            (pt.color.blue * 255).toInt(),
                        )
                        textSize = labelSize
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                    }
                    val tw = tp.measureText(labelText)
                    val pillPad = 6f * sf
                    val rx = px + 12f * sf - pillPad
                    val ry = py - 12f * sf - 12f * sf
                    val rw = tw + pillPad * 2f
                    val rh = 20f * sf
                    val radius = 4f * sf
                    val rect = RectF(rx, ry, rx + rw, ry + rh)
                    val path = Path().apply {
                        addRoundRect(rect, radius, radius, Path.Direction.CW)
                    }
                    val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                        color = android.graphics.Color.argb(
                            (pt.color.alpha * 255).toInt(),
                            (pt.color.red * 255).toInt(),
                            (pt.color.green * 255).toInt(),
                            (pt.color.blue * 255).toInt(),
                        )
                        style = Paint.Style.FILL
                    }
                    canvas.nativeCanvas.drawPath(path, fillPaint)
                    tp.color = android.graphics.Color.WHITE
                    tp.textAlign = Paint.Align.LEFT
                    canvas.nativeCanvas.drawText(labelText, px + 12f * sf, py - 12f * sf + 2f, tp)
                }
            }
        }
    }
}
