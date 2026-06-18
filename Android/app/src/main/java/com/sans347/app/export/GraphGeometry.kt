package com.sans347.app.export

import com.sans347.app.data.GraphConfig
import kotlin.math.log10
import kotlin.math.max
import kotlin.math.pow

const val GRAPH_BASE_WIDTH = 750f
const val GRAPH_BASE_HEIGHT = 520f

data class GraphLayoutMetrics(
    val width: Float,
    val height: Float,
    val scaleFactor: Float,
    val marginTop: Float,
    val marginRight: Float,
    val marginBottom: Float,
    val marginLeft: Float,
    val plotW: Float,
    val plotH: Float,
)

class GraphGeometry(
    private val config: GraphConfig,
    val metrics: GraphLayoutMetrics,
) {
    private val xMinLog = log10(config.xMin)
    private val xMaxLog = log10(config.xMax)
    private val yMinLog = log10(config.yMin)
    private val yMaxLog = log10(config.yMax)

    constructor(config: GraphConfig, width: Float, height: Float) : this(
        config,
        buildMetrics(width, height),
    )

    fun logX(value: Double): Float {
        val clamped = max(value, config.xMin)
        return metrics.marginLeft +
            ((log10(clamped) - xMinLog) / (xMaxLog - xMinLog)).toFloat() * metrics.plotW
    }

    fun logY(value: Double): Float {
        val clamped = max(value, config.yMin)
        return metrics.marginTop + metrics.plotH -
            ((log10(clamped) - yMinLog) / (yMaxLog - yMinLog)).toFloat() * metrics.plotH
    }

    fun yAxisExponentRange(): IntRange {
        val min = kotlin.math.floor(log10(config.yMin)).toInt()
        val max = kotlin.math.ceil(log10(config.yMax)).toInt()
        return min..max
    }

    fun xAxisExponentRange(): IntRange {
        val min = kotlin.math.floor(log10(config.xMin)).toInt()
        val max = kotlin.math.ceil(log10(config.xMax)).toInt()
        return min..max
    }

    fun axisPowerValue(exponent: Int): Double = 10.0.pow(exponent)

    private companion object {
        fun buildMetrics(width: Float, height: Float): GraphLayoutMetrics {
            val sf = width / GRAPH_BASE_WIDTH
            val marginTop = 20f * sf
            val marginRight = 30f * sf
            val marginBottom = 60f * sf
            val marginLeft = 80f * sf
            val plotW = width - marginLeft - marginRight
            val plotH = height - marginTop - marginBottom
            return GraphLayoutMetrics(
                width = width,
                height = height,
                scaleFactor = sf,
                marginTop = marginTop,
                marginRight = marginRight,
                marginBottom = marginBottom,
                marginLeft = marginLeft,
                plotW = plotW,
                plotH = plotH,
            )
        }
    }
}

fun formatAxisLabel(value: Double): String {
    if (value >= 1000) {
        return String.format("%,.0f", value).replace(',', ' ')
    }
    return value.toString().replace('.', ',')
}

fun formatCategoryDisplayLabel(category: String): String = when (category) {
    "Not regulated" -> "Not Regulated"
    "SEP" -> "Sound Engineering Practice"
    else -> "Category $category"
}
