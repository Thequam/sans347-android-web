package com.sans347.app.export

import android.graphics.Bitmap
import android.graphics.Canvas as AndroidCanvas
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import com.sans347.app.data.GraphConfig
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.atan2
import kotlin.math.max
import kotlin.math.min

object GraphBitmapRenderer {
    fun render(
        config: GraphConfig,
        plotPoint: ExportPlotPoint? = null,
        width: Int = 1500,
        height: Int = 1040,
    ): Bitmap {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = AndroidCanvas(bitmap)
        draw(config, canvas, width.toFloat(), height.toFloat(), plotPoint)
        return bitmap
    }

    fun draw(
        config: GraphConfig,
        canvas: AndroidCanvas,
        width: Float,
        height: Float,
        plotPoint: ExportPlotPoint? = null,
    ) {
        val geometry = GraphGeometry(config, width, height)
        val m = geometry.metrics
        val sf = m.scaleFactor

        canvas.drawColor(ExportColors.parseArgb(ExportColors.WHITE))

        val majorGridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ExportColors.parseArgb(ExportColors.MAJOR_GRID)
            strokeWidth = 0.5f
        }
        val minorGridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ExportColors.parseArgb(ExportColors.MINOR_GRID)
            strokeWidth = 0.5f
        }
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ExportColors.parseArgb(ExportColors.GRAY_700)
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }

        for (e in geometry.yAxisExponentRange()) {
            val value = geometry.axisPowerValue(e)
            if (value >= config.yMin && value <= config.yMax) {
                val py = geometry.logY(value)
                canvas.drawLine(m.marginLeft, py, m.marginLeft + m.plotW, py, majorGridPaint)
            }
            for (multiplier in 2..9) {
                val mval = value * multiplier
                if (mval >= config.yMin && mval <= config.yMax) {
                    val py = geometry.logY(mval)
                    canvas.drawLine(m.marginLeft, py, m.marginLeft + m.plotW, py, minorGridPaint)
                }
            }
        }

        for (e in geometry.xAxisExponentRange()) {
            val value = geometry.axisPowerValue(e)
            if (value >= config.xMin && value <= config.xMax) {
                val px = geometry.logX(value)
                canvas.drawLine(px, m.marginTop, px, m.marginTop + m.plotH, majorGridPaint)
            }
            for (multiplier in 2..9) {
                val mval = value * multiplier
                if (mval >= config.xMin && mval <= config.xMax) {
                    val px = geometry.logX(mval)
                    canvas.drawLine(px, m.marginTop, px, m.marginTop + m.plotH, minorGridPaint)
                }
            }
        }

        canvas.drawRect(
            m.marginLeft,
            m.marginTop,
            m.marginLeft + m.plotW,
            m.marginTop + m.plotH,
            borderPaint,
        )

        for (line in config.lines) {
            val x1p = geometry.logX(line.x1)
            val y1p = geometry.logY(line.y1)
            val x2p = geometry.logX(line.x2)
            val y2p = geometry.logY(line.y2)
            val lx1 = max(m.marginLeft, min(m.marginLeft + m.plotW, x1p))
            val ly1 = max(m.marginTop, min(m.marginTop + m.plotH, y1p))
            val lx2 = max(m.marginLeft, min(m.marginLeft + m.plotW, x2p))
            val ly2 = max(m.marginTop, min(m.marginTop + m.plotH, y2p))
            val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = ExportColors.parseArgb(ExportColors.lineColor(line.color))
                strokeWidth = 1.5f
            }
            canvas.drawLine(lx1, ly1, lx2, ly2, linePaint)

            line.label?.let { labelText ->
                val labelPosX = line.labelX?.let { geometry.logX(it) } ?: (x1p + x2p) / 2f
                val labelPosY = line.labelY?.let { geometry.logY(it) } ?: (y1p + y2p) / 2f
                val angleRad = atan2((y2p - y1p).toDouble(), (x2p - x1p).toDouble())
                val angleDeg = (angleRad * 180.0 / PI).toFloat()
                val maxY = max(line.y1, line.y2)
                val maxX = max(line.x1, line.x2)
                val isHorizontal = abs(line.y1 - line.y2) < 0.01 * maxY
                val isVertical = abs(line.x1 - line.x2) < 0.01 * maxX
                val textColor = if (isHorizontal || isVertical) {
                    ExportColors.GRAY_700
                } else {
                    ExportColors.BOUNDARY_RED
                }
                val textSizePx = max(8f, 10f * sf)
                val baseOffset = -6f * sf
                val extraOffset = (line.labelOffset ?: 0.0).toFloat() * sf
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ExportColors.parseArgb(textColor)
                    textSize = textSizePx
                    textAlign = Paint.Align.CENTER
                }
                canvas.save()
                canvas.translate(labelPosX, labelPosY)
                when {
                    !isHorizontal && !isVertical -> canvas.rotate(angleDeg)
                    isVertical -> canvas.rotate(-90f)
                }
                canvas.drawText(labelText, 0f, baseOffset + extraOffset, paint)
                canvas.restore()
            }
        }

        for (zone in config.categoryZones) {
            val zx = geometry.logX(zone.x)
            val zy = geometry.logY(zone.y)
            if (zx > m.marginLeft && zx < m.marginLeft + m.plotW && zy > m.marginTop && zy < m.marginTop + m.plotH) {
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ExportColors.parseArgb(ExportColors.GRAY_500)
                    textSize = max(10f, 14f * sf)
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                    textAlign = Paint.Align.CENTER
                }
                canvas.drawText(zone.label, zx, zy, paint)
            }
        }

        val axisTextSize = max(9f, 11f * sf)
        for (e in geometry.yAxisExponentRange()) {
            val value = geometry.axisPowerValue(e)
            if (value >= config.yMin && value <= config.yMax) {
                val py = geometry.logY(value)
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ExportColors.parseArgb(ExportColors.GRAY_700)
                    textSize = axisTextSize
                    textAlign = Paint.Align.RIGHT
                }
                canvas.drawText(formatAxisLabel(value), m.marginLeft - 8f * sf, py + 4f, paint)
            }
        }

        for (e in geometry.xAxisExponentRange()) {
            val value = geometry.axisPowerValue(e)
            if (value >= config.xMin && value <= config.xMax) {
                val px = geometry.logX(value)
                val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ExportColors.parseArgb(ExportColors.GRAY_700)
                    textSize = axisTextSize
                    textAlign = Paint.Align.CENTER
                }
                canvas.drawText(formatAxisLabel(value), px, m.marginTop + m.plotH + 18f * sf, paint)
            }
        }

        val titleSize = max(10f, 12f * sf)
        val titlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ExportColors.parseArgb(ExportColors.AXIS_TITLE)
            textSize = titleSize
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            textAlign = Paint.Align.CENTER
        }
        canvas.drawText(
            config.xAxisLabel,
            m.marginLeft + m.plotW / 2f,
            m.height - 10f * sf,
            titlePaint,
        )
        canvas.save()
        canvas.translate(15f * sf, m.marginTop + m.plotH / 2f)
        canvas.rotate(-90f)
        canvas.drawText(config.yAxisLabel, 0f, 0f, titlePaint)
        canvas.restore()

        plotPoint?.let { pt ->
            val px = geometry.logX(pt.x)
            val py = geometry.logY(pt.y)
            if (px >= m.marginLeft && px <= m.marginLeft + m.plotW && py >= m.marginTop && py <= m.marginTop + m.plotH) {
                val dashPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ExportColors.parseArgb(pt.colorHex)
                    strokeWidth = 1.5f
                    pathEffect = DashPathEffect(floatArrayOf(4f, 4f), 0f)
                }
                canvas.drawLine(px, m.marginTop, px, m.marginTop + m.plotH, dashPaint)
                canvas.drawLine(m.marginLeft, py, m.marginLeft + m.plotW, py, dashPaint)
                val r = max(5f, 8f * sf)
                val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ExportColors.parseArgb(pt.colorHex)
                    style = Paint.Style.FILL
                }
                val strokePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ExportColors.parseArgb(ExportColors.WHITE)
                    style = Paint.Style.STROKE
                    strokeWidth = 2f
                }
                canvas.drawCircle(px, py, r, fillPaint)
                canvas.drawCircle(px, py, r, strokePaint)

                val labelText = "Your Equipment"
                val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ExportColors.parseArgb(pt.colorHex)
                    textSize = max(9f, 11f * sf)
                    typeface = android.graphics.Typeface.DEFAULT_BOLD
                }
                val tw = labelPaint.measureText(labelText)
                val pillPad = 6f * sf
                val rx = px + 12f * sf - pillPad
                val ry = py - 12f * sf - 12f * sf
                val rw = tw + pillPad * 2f
                val rh = 20f * sf
                val rect = RectF(rx, ry, rx + rw, ry + rh)
                val path = Path().apply {
                    addRoundRect(rect, 4f * sf, 4f * sf, Path.Direction.CW)
                }
                val pillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = ExportColors.parseArgb(pt.colorHex)
                    style = Paint.Style.FILL
                }
                canvas.drawPath(path, pillPaint)
                labelPaint.color = ExportColors.parseArgb(ExportColors.WHITE)
                labelPaint.textAlign = Paint.Align.LEFT
                canvas.drawText(labelText, px + 12f * sf, py - 12f * sf + 2f, labelPaint)
            }
        }
    }
}
