package com.sans347.app.export

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.pdf.PdfDocument
import com.sans347.app.data.ConformityModules
import com.sans347.app.data.GraphConfig
import com.sans347.app.data.ResultData
import com.sans347.app.data.getCategoryColorHex
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ReportPdfExporter {
    private const val PAGE_WIDTH = 612
    private const val PAGE_HEIGHT = 792
    private const val MARGIN = 40f
    private const val CARD_GAP = 12f
    private const val CARD_PADDING = 10f
    private const val CARD_RADIUS = 8f
    private const val HEADER_CARD_HEIGHT = 100f
    private const val COMPACT_LINE = 11f
    private const val CONTENT_WIDTH = PAGE_WIDTH - MARGIN * 2

    private const val CARD_FILL = "#F9FAFB"
    private const val CARD_BORDER = "#E5E7EB"
    private const val WARNING_BG = "#FEFCE8"
    private const val WARNING_BORDER = "#FDE68A"
    private const val PRIMARY_CYAN = "#00C2FF"

    fun exportToCache(
        context: Context,
        result: ResultData,
        graph: GraphConfig,
        catRisk: String,
        conformity: ConformityModules,
        plotPoint: ExportPlotPoint?,
    ): File {
        val graphBitmap = GraphBitmapRenderer.render(config = graph, plotPoint = plotPoint)
        val document = PdfDocument()
        val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
        val page = document.startPage(pageInfo)
        val canvas = page.canvas

        var y = MARGIN
        y = drawTitle(canvas, y, "SANS 347 Calculation Results")
        y += 6f

        y = drawHeaderCards(canvas, y, result, graph, catRisk)
        y += 6f

        val graphWidth = CONTENT_WIDTH
        val graphHeight = graphWidth * (GRAPH_BASE_HEIGHT / GRAPH_BASE_WIDTH)
        canvas.drawBitmap(
            graphBitmap,
            null,
            RectF(MARGIN, y, MARGIN + graphWidth, y + graphHeight),
            null,
        )
        y += graphHeight + 6f

        y = drawFooterCards(canvas, y, result, conformity)
        y += 8f

        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US).format(Date())
        drawTextLine(canvas, MARGIN, y, "Figure ${graph.id} — ${graph.footerText}", 8f, ExportColors.GRAY_500)
        drawTextLine(canvas, MARGIN, y + 10f, "Generated $timestamp", 8f, ExportColors.GRAY_500)

        document.finishPage(page)

        val safeCategory = result.category.replace(" ", "_")
        val file = File(context.cacheDir, "sans347-report-$safeCategory-figure-${graph.id}.pdf")
        file.outputStream().use { output ->
            document.writeTo(output)
        }
        document.close()
        graphBitmap.recycle()
        return file
    }

    private fun drawHeaderCards(
        canvas: Canvas,
        top: Float,
        result: ResultData,
        graph: GraphConfig,
        catRisk: String,
    ): Float {
        val cardWidth = (CONTENT_WIDTH - CARD_GAP) / 2f
        val leftX = MARGIN
        val rightX = MARGIN + cardWidth + CARD_GAP

        drawRoundedCard(canvas, leftX, top, cardWidth, HEADER_CARD_HEIGHT, CARD_FILL, CARD_BORDER)
        drawRoundedCard(canvas, rightX, top, cardWidth, HEADER_CARD_HEIGHT, CARD_FILL, CARD_BORDER)

        var leftY = top + CARD_PADDING + 10f
        val leftTextX = leftX + CARD_PADDING
        val leftMaxWidth = cardWidth - CARD_PADDING * 2

        leftY = drawTextLine(
            canvas,
            leftTextX,
            leftY,
            "Your Equipment Category",
            10f,
            ExportColors.AXIS_TITLE,
            bold = true,
        )
        leftY += 2f
        leftY = drawTextLine(
            canvas,
            leftTextX,
            leftY,
            formatCategoryDisplayLabel(result.category),
            10f,
            getCategoryColorHex(result.category),
            bold = true,
        )
        leftY = drawTextLine(canvas, leftTextX, leftY, catRisk, 9f, ExportColors.GRAY_500)
        val figureLine = buildString {
            append("Figure ${graph.id} — ${graph.title}")
            if (graph.subtitle.isNotEmpty()) append(" — ${graph.subtitle}")
        }
        drawWrappedText(
            canvas,
            leftTextX,
            leftY,
            figureLine,
            leftMaxWidth,
            9f,
            COMPACT_LINE,
            ExportColors.GRAY_700,
        )

        var rightY = top + CARD_PADDING + 10f
        val rightTextX = rightX + CARD_PADDING
        val rightMaxWidth = cardWidth - CARD_PADDING * 2
        val isPiping = graph.xVariable == "DN"
        val equipLabel = if (graph.equipmentType == "Pressure Vessels") "Vessels" else graph.equipmentType
        val isSteamGen = result.equipmentType == "Steam Generator"

        rightY = drawTextLine(
            canvas,
            rightTextX,
            rightY,
            "Input Parameters",
            10f,
            ExportColors.AXIS_TITLE,
            bold = true,
        )
        rightY += 2f
        rightY = drawLabelValueRow(canvas, rightTextX, rightY, rightMaxWidth, "Equipment Type:", equipLabel)
        rightY = drawLabelValueRow(canvas, rightTextX, rightY, rightMaxWidth, "Design Pressure:", "${fmtNum(result.ps)} kPa")
        if (!isSteamGen) {
            rightY = drawLabelValueRow(canvas, rightTextX, rightY, rightMaxWidth, "State Contents:", result.stateOfContents)
        }
        rightY = drawLabelValueRow(
            canvas,
            rightTextX,
            rightY,
            rightMaxWidth,
            if (isPiping) "Diameter:" else "Volume:",
            "${fmtNum(result.vOrDn)} ${if (isPiping) "DN" else "L"}",
        )
        if (!isSteamGen) {
            drawLabelValueRow(canvas, rightTextX, rightY, rightMaxWidth, "Fluid Group:", result.fluidGroup)
        }

        return top + HEADER_CARD_HEIGHT
    }

    private fun drawFooterCards(
        canvas: Canvas,
        top: Float,
        result: ResultData,
        conformity: ConformityModules,
    ): Float {
        var y = top
        val cardWidth = CONTENT_WIDTH
        val textX = MARGIN + CARD_PADDING
        val maxWidth = cardWidth - CARD_PADDING * 2
        val bodySize = 8.5f
        val headingBlock = CARD_PADDING + 22f

        val prEngText = if (requiresPrEng(result.category)) {
            "Professional Review: Pr Eng sign-off required. Category ${result.category} pressure equipment must be signed off by a Professional Registered Engineer (Pr Eng) in terms of the Engineering Profession Act."
        } else {
            "Professional Review: No Pr Eng sign-off required for this category. It is still recommended to have these results reviewed by a qualified pressure equipment engineer."
        }
        val notesTexts = listOf(
            "Note: This categorization is based on SANS 347:2024 Edition 3.1 standards.",
            "Compliance: All pressure equipment must comply with the applicable conformity assessment procedures for the determined category.",
            prEngText,
        )
        val notesBodyHeight = measureWrappedTextsHeight(notesTexts, maxWidth, bodySize, COMPACT_LINE)
        var notesHeight = headingBlock + notesBodyHeight + CARD_PADDING

        val hasConformity = result.category != "SEP" && result.category != "Not regulated"
        val conformityTexts = if (hasConformity) {
            listOf(
                "Without QMS: ${conformity.withoutQuality}",
                "With QMS: ${conformity.withQuality}",
            )
        } else {
            emptyList()
        }
        val conformityBodyHeight = if (hasConformity) {
            measureWrappedTextsHeight(conformityTexts, maxWidth, bodySize, COMPACT_LINE)
        } else {
            0f
        }
        var conformityHeight = if (hasConformity) {
            headingBlock + conformityBodyHeight + CARD_PADDING
        } else {
            0f
        }

        val footerBudget = PAGE_HEIGHT - MARGIN - top - 28f
        val gap = 6f
        val totalFooter = notesHeight + if (hasConformity) gap + conformityHeight else 0f
        if (totalFooter > footerBudget) {
            if (hasConformity) {
                val available = footerBudget - gap
                val notesCap = minOf(notesHeight, available * 0.55f)
                val confCap = available - notesCap
                notesHeight = maxOf(headingBlock + CARD_PADDING, notesCap)
                conformityHeight = maxOf(headingBlock + CARD_PADDING, confCap)
            } else {
                notesHeight = minOf(notesHeight, footerBudget)
            }
        }

        drawRoundedCard(canvas, MARGIN, y, cardWidth, notesHeight, WARNING_BG, WARNING_BORDER)
        drawTextLine(
            canvas,
            textX,
            y + CARD_PADDING + 9f,
            "Important Notes",
            10f,
            ExportColors.AXIS_TITLE,
            bold = true,
        )
        var textY = y + CARD_PADDING + 22f
        for (noteText in notesTexts) {
            textY = drawWrappedText(
                canvas,
                textX,
                textY,
                noteText,
                maxWidth,
                bodySize,
                COMPACT_LINE,
                ExportColors.GRAY_700,
            )
        }

        y += notesHeight + gap

        if (hasConformity) {
            drawRoundedCard(canvas, MARGIN, y, cardWidth, conformityHeight, CARD_FILL, CARD_BORDER)
            drawTextLine(
                canvas,
                textX,
                y + CARD_PADDING + 9f,
                "Required Conformity Assessment Modules",
                10f,
                ExportColors.AXIS_TITLE,
                bold = true,
            )
            textY = y + CARD_PADDING + 22f
            for (line in conformityTexts) {
                textY = drawWrappedText(
                    canvas,
                    textX,
                    textY,
                    line,
                    maxWidth,
                    bodySize,
                    COMPACT_LINE,
                    ExportColors.GRAY_700,
                )
            }
            y += conformityHeight + gap
        }

        return y
    }

    private fun drawRoundedCard(
        canvas: Canvas,
        left: Float,
        top: Float,
        width: Float,
        height: Float,
        fillHex: String,
        borderHex: String,
    ) {
        val rect = RectF(left, top, left + width, top + height)
        val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ExportColors.parseArgb(fillHex)
            style = Paint.Style.FILL
        }
        val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ExportColors.parseArgb(borderHex)
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }
        canvas.drawRoundRect(rect, CARD_RADIUS, CARD_RADIUS, fillPaint)
        canvas.drawRoundRect(rect, CARD_RADIUS, CARD_RADIUS, borderPaint)
    }

    private fun drawTitle(canvas: Canvas, y: Float, text: String): Float {
        drawTextLine(canvas, MARGIN, y + 16f, text, 16f, ExportColors.AXIS_TITLE, bold = true)
        return y + 22f
    }

    private fun drawTextLine(
        canvas: Canvas,
        x: Float,
        y: Float,
        text: String,
        textSize: Float,
        colorHex: String,
        bold: Boolean = false,
    ): Float {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ExportColors.parseArgb(colorHex)
            this.textSize = textSize
            if (bold) typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        canvas.drawText(text, x, y, paint)
        return y + COMPACT_LINE
    }

    private fun drawLabelValueRow(
        canvas: Canvas,
        x: Float,
        y: Float,
        maxWidth: Float,
        label: String,
        value: String,
    ): Float {
        val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ExportColors.parseArgb(ExportColors.GRAY_500)
            textSize = 9f
        }
        val valuePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ExportColors.parseArgb(PRIMARY_CYAN)
            textSize = 9f
            typeface = android.graphics.Typeface.DEFAULT_BOLD
        }
        canvas.drawText(label, x, y, labelPaint)
        val labelWidth = labelPaint.measureText(label)
        val valueX = x + labelWidth + 4f
        val available = maxWidth - labelWidth - 4f
        val displayValue = if (valuePaint.measureText(value) > available) {
            truncateToWidth(value, valuePaint, available)
        } else {
            value
        }
        canvas.drawText(displayValue, valueX, y, valuePaint)
        return y + COMPACT_LINE
    }

    private fun truncateToWidth(text: String, paint: Paint, maxWidth: Float): String {
        if (paint.measureText(text) <= maxWidth) return text
        var truncated = text
        while (truncated.length > 1 && paint.measureText("$truncated…") > maxWidth) {
            truncated = truncated.dropLast(1)
        }
        return "$truncated…"
    }

    private fun drawWrappedText(
        canvas: Canvas,
        x: Float,
        y: Float,
        text: String,
        maxWidth: Float,
        textSize: Float,
        lineHeight: Float,
        colorHex: String,
    ): Float {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = ExportColors.parseArgb(colorHex)
            this.textSize = textSize
        }
        val words = text.split(' ')
        var line = ""
        var currentY = y
        for (word in words) {
            val test = if (line.isEmpty()) word else "$line $word"
            if (paint.measureText(test) > maxWidth && line.isNotEmpty()) {
                canvas.drawText(line, x, currentY, paint)
                currentY += lineHeight
                line = word
            } else {
                line = test
            }
        }
        if (line.isNotEmpty()) {
            canvas.drawText(line, x, currentY, paint)
            currentY += lineHeight
        }
        return currentY
    }

    private fun measureWrappedTextHeight(
        text: String,
        maxWidth: Float,
        textSize: Float,
        lineHeight: Float,
    ): Float {
        val paint = Paint().apply { this.textSize = textSize }
        val words = text.split(' ')
        var line = ""
        var lines = 0
        for (word in words) {
            val test = if (line.isEmpty()) word else "$line $word"
            if (paint.measureText(test) > maxWidth && line.isNotEmpty()) {
                lines++
                line = word
            } else {
                line = test
            }
        }
        if (line.isNotEmpty()) lines++
        return lines * lineHeight
    }

    private fun measureWrappedTextsHeight(
        texts: List<String>,
        maxWidth: Float,
        textSize: Float,
        lineHeight: Float,
    ): Float = texts.sumOf { text ->
        measureWrappedTextHeight(text, maxWidth, textSize, lineHeight).toDouble()
    }.toFloat()

    private fun requiresPrEng(category: String): Boolean = category in listOf("II", "III", "IV")

    private fun fmtNum(v: Double): String = String.format(Locale.US, "%,.0f", v).replace(',', ' ')
}
