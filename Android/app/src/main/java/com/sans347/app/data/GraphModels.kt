package com.sans347.app.data

data class LineSegment(
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double,
    val label: String? = null,
    val labelPosition: String? = null,
    val labelOffset: Double? = null,
    val labelX: Double? = null,
    val labelY: Double? = null,
    val color: String? = null,
)

data class CategoryZone(
    val label: String,
    val x: Double,
    val y: Double,
)

data class GraphConfig(
    val id: Int,
    val title: String,
    val subtitle: String,
    val equipmentType: String,
    val fluidType: String,
    val xAxisLabel: String,
    val yAxisLabel: String,
    val xMin: Double,
    val xMax: Double,
    val yMin: Double,
    val yMax: Double,
    val lines: List<LineSegment>,
    val categoryZones: List<CategoryZone>,
    val footerText: String,
    val applicationText: String,
    val xVariable: String, // "V" | "DN"
)

data class ResultData(
    val category: String,
    val figureId: Int,
    val product: Double,
    val ps: Double,
    val vOrDn: Double,
    val equipmentType: String,
    val stateOfContents: String,
    val fluidGroup: String,
)

data class ConformityModules(
    val withoutQuality: String,
    val withQuality: String,
)
