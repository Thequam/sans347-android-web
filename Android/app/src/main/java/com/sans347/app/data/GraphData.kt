package com.sans347.app.data

private val figure1 = GraphConfig(
    id = 1,
    title = "Pressure Vessels",
    subtitle = "Dangerous gas",
    equipmentType = "Pressure Vessels",
    fluidType = "Dangerous gas",
    xAxisLabel = "Volume (V) L",
    yAxisLabel = "Design pressure (PS) kPa",
    xMin = 0.1,
    xMax = 10000.0,
    yMin = 0.1,
    yMax = 400000.0,
    lines = listOf(
        LineSegment(0.1, 100000.0, 1.0, 100000.0, label = "PS = 100 000", color = "#dc2626"),
        LineSegment(1.0, 100000.0, 2000.0, 50.0, label = "PS×V = 100 000", color = "#dc2626"),
        LineSegment(0.1, 20000.0, 1.0, 20000.0, label = "PS = 20 000", color = "#dc2626"),
        LineSegment(1.0, 20000.0, 400.0, 50.0, label = "PS×V = 20 000", color = "#dc2626"),
        LineSegment(1.0, 2000.0, 1.0, 100000.0, label = "V = 1", color = "#dc2626"),
        LineSegment(1.0, 5000.0, 100.0, 50.0, label = "PS×V = 5 000", color = "#dc2626"),
        LineSegment(0.1, 50.0, 100000.0, 50.0, label = "PS = 50", labelX = 0.3, labelY = 55.0, color = "#dc2626"),
        LineSegment(1.0, 2000.0, 40.0, 50.0, label = "PS×V = 2 000", color = "#dc2626"),
    ),
    categoryZones = listOf(
        CategoryZone("SEP", 0.3, 300.0),
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 45.0, 60.0),
        CategoryZone("II", 150.0, 60.0),
        CategoryZone("III", 600.0, 60.0),
        CategoryZone("IV", 600.0, 30000.0),
    ),
    footerText = "Figure 1 — Graph for vessels — Dangerous gas",
    applicationText = "Vessels that fall within categories I or II and that are intended to contain an unstable gas, shall be classified as category III (see figure 1).",
    xVariable = "V",
)

private val figure2 = GraphConfig(
    id = 2,
    title = "Pressure Vessels",
    subtitle = "Non-dangerous gas",
    equipmentType = "Pressure Vessels",
    fluidType = "Non-dangerous gas",
    xAxisLabel = "Volume (V) L",
    yAxisLabel = "Design pressure (PS) kPa",
    xMin = 0.1,
    xMax = 10000.0,
    yMin = 0.1,
    yMax = 500000.0,
    lines = listOf(
        LineSegment(0.1, 300000.0, 1.0, 300000.0, label = "PS = 300 000", color = "#dc2626"),
        LineSegment(1.0, 300000.0, 750.0, 400.0, label = "PS×V = 300 000", color = "#dc2626"),
        LineSegment(0.1, 100000.0, 1.0, 100000.0, label = "PS = 100 000", color = "#dc2626"),
        LineSegment(1.0, 100000.0, 2000.0, 50.0, label = "PS×V = 100 000", color = "#dc2626"),
        LineSegment(0.1, 50.0, 10000.0, 50.0, label = "PS = 50", color = "#dc2626"),
        LineSegment(1.0, 20000.0, 400.0, 50.0, label = "PS×V = 20 000", color = "#dc2626"),
        LineSegment(750.0, 400.0, 10000.0, 400.0, label = "PS = 400", color = "#dc2626"),
        LineSegment(1.0, 5000.0, 100.0, 50.0, label = "PS×V = 5 000", color = "#dc2626"),
        LineSegment(1.0, 5000.0, 1.0, 100000.0, label = "V = 1", color = "#dc2626"),
    ),
    categoryZones = listOf(
        CategoryZone("SEP", 0.3, 300.0),
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 150.0, 60.0),
        CategoryZone("II", 600.0, 60.0),
        CategoryZone("III", 3000.0, 60.0),
        CategoryZone("IV", 3000.0, 30000.0),
    ),
    footerText = "Figure 2 — Graph for vessels — Non-dangerous gas",
    applicationText = "Portable fire extinguishers up to 3 000 kPa shall be classified as at least category III (see figure 2).",
    xVariable = "V",
)

private val figure3 = GraphConfig(
    id = 3,
    title = "Pressure Vessels",
    subtitle = "Dangerous liquids",
    equipmentType = "Pressure Vessels",
    fluidType = "Dangerous liquids",
    xAxisLabel = "Volume (V) L",
    yAxisLabel = "Design pressure (PS) kPa",
    xMin = 0.1,
    xMax = 10000.0,
    yMin = 0.1,
    yMax = 500000.0,
    lines = listOf(
        LineSegment(0.1, 50000.0, 10000.0, 50000.0, label = "PS = 50 000", color = "#dc2626"),
        LineSegment(1.0, 20000.0, 400.0, 50.0, label = "PS×V = 20 000", labelX = 5.0, labelY = 4000.0, color = "#dc2626"),
        LineSegment(20.0, 1000.0, 10000.0, 1000.0, label = "PS = 1 000", color = "#dc2626"),
        LineSegment(0.1, 50.0, 10000.0, 50.0, label = "PS = 50", color = "#dc2626"),
        LineSegment(1.0, 20000.0, 1.0, 500000.0, label = "V = 1", color = "#dc2626"),
    ),
    categoryZones = listOf(
        CategoryZone("SEP", 0.3, 300.0),
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 1200.0, 60.0),
        CategoryZone("II", 0.3, 100000.0),
        CategoryZone("II", 1200.0, 5000.0),
        CategoryZone("III", 1200.0, 150000.0),
    ),
    footerText = "Figure 3 — Graph for vessels — Dangerous liquids",
    applicationText = "Figure 3 shows the various categories for dangerous liquids contained in vessels.",
    xVariable = "V",
)

private val figure4 = GraphConfig(
    id = 4,
    title = "Pressure Vessels",
    subtitle = "Non-dangerous liquids",
    equipmentType = "Pressure Vessels",
    fluidType = "Non-dangerous liquids",
    xAxisLabel = "Volume (V) L",
    yAxisLabel = "Design pressure (PS) kPa",
    xMin = 0.1,
    xMax = 100000.0,
    yMin = 0.1,
    yMax = 500000.0,
    lines = listOf(
        LineSegment(0.1, 50.0, 100000.0, 50.0, label = "PS = 50", color = "#dc2626"),
        LineSegment(10.0, 100000.0, 1000.0, 1000.0, label = "PS×V = 1 000 000", color = "#dc2626"),
        LineSegment(0.1, 100000.0, 10.0, 100000.0, label = "PS = 100 000", color = "#dc2626"),
        LineSegment(1000.0, 1000.0, 100000.0, 1000.0, label = "PS = 1 000", color = "#dc2626"),
        LineSegment(20.0, 50000.0, 100000.0, 50000.0, label = "PS = 50 000", color = "#dc2626"),
        LineSegment(10.0, 100000.0, 10.0, 500000.0, label = "V = 10", color = "#dc2626"),
    ),
    categoryZones = listOf(
        CategoryZone("SEP", 0.3, 300.0),
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 2.0, 200000.0),
        CategoryZone("I", 15000.0, 5000.0),
        CategoryZone("II", 15000.0, 150000.0),
        // CategoryZone("III", 50000.0, 150000.0),

    ),
    footerText = "Figure 4 — Graph for vessels — Non-dangerous liquids",
    applicationText = "Assemblies intended for generating warm water shall be subjected to a type approval. See table 2 category 3 for warm water.",
    xVariable = "V",
)

private val figure5 = GraphConfig(
    id = 5,
    title = "Steam generators",
    subtitle = "",
    equipmentType = "Steam Generator",
    fluidType = "",
    xAxisLabel = "Volume (V) L",
    yAxisLabel = "Design pressure (PS) kPa",
    xMin = 0.1,
    xMax = 10000.0,
    yMin = 0.1,
    yMax = 100000.0,
    lines = listOf(
        LineSegment(0.1, 50.0, 10000.0, 50.0, label = "PS = 50", color = "#dc2626"),
        LineSegment(93.75, 3200.0, 1000.0, 300.0, label = "PS×V = 300 000", color = "#dc2626"),
        LineSegment(2.0, 3200.0, 93.75, 3200.0, label = "PS = 3 200", color = "#dc2626"),
        LineSegment(6.25, 3200.0, 400.0, 50.0, label = "PS×V = 20 000", color = "#dc2626"),
        LineSegment(2.0, 2500.0, 2.0, 100000.0, label = "V = 2", color = "#dc2626"),
        LineSegment(2.0, 2500.0, 100.0, 50.0, label = "PS×V = 5 000", color = "#dc2626"),
        LineSegment(1000.0, 50.0, 1000.0, 300.0, label = "V = 1 000", color = "#dc2626"),
    ),
    categoryZones = listOf(
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 0.5, 1000.0),
        CategoryZone("II", 50.0, 150.0),
        CategoryZone("III", 200.0, 500.0),
        CategoryZone("IV", 500.0, 15000.0),
    ),
    footerText = "Figure 5 — Graph for steam generators",
    applicationText = "The design of jacketed pressure cookers shall be subjected to a conformity assessment procedure equivalent to at least one of the category III modules (see figure 5).",
    xVariable = "V",
)

private val figure6 = GraphConfig(
    id = 6,
    title = "Piping",
    subtitle = "Dangerous gas",
    equipmentType = "Piping",
    fluidType = "Dangerous gas",
    xAxisLabel = "DN",
    yAxisLabel = "Design pressure (PS) kPa",
    xMin = 1.0,
    xMax = 2000.0,
    yMin = 1.0,
    yMax = 100000.0,
    lines = listOf(
        LineSegment(1.0, 50.0, 2000.0, 50.0, label = "PS = 50", color = "#dc2626"),
        LineSegment(350.0, 1000.0, 100.0, 3500.0, label = "PS×DN = 350 000", color = "#dc2626"),
        LineSegment(25.0, 50.0, 25.0, 100000.0, label = "DN = 25", color = "#dc2626"),
        LineSegment(100.0, 1000.0, 25.0, 4000.0, label = "PS×DN = 100 000", color = "#dc2626"),
        LineSegment(100.0, 3500.0, 100.0, 100000.0, label = "DN = 100", color = "#dc2626"),
        LineSegment(350.0, 50.0, 350.0, 1000.0, label = "DN = 350", color = "#dc2626"),
        LineSegment(100.0, 50.0, 100.0, 1000.0, color = "#dc2626"),
    ),
    categoryZones = listOf(
        CategoryZone("SEP", 5.0, 300.0),
        CategoryZone("Not regulated", 50.0, 5.0),
        CategoryZone("I", 50.0, 300.0),
        CategoryZone("II", 200.0, 300.0),
        CategoryZone("III", 700.0, 300.0),
    ),
    footerText = "Figure 6 — Graph for piping — Dangerous gas",
    applicationText = "Piping that is intended for unstable gases that fall within categories I or II shall be classified as category III (see figure 6).",
    xVariable = "DN",
)

private val figure7 = GraphConfig(
    id = 7,
    title = "Piping",
    subtitle = "Non-dangerous gas",
    equipmentType = "Piping",
    fluidType = "Non-dangerous gas",
    xAxisLabel = "DN",
    yAxisLabel = "Design pressure (PS) kPa",
    xMin = 1.0,
    xMax = 20000.0,
    yMin = 1.0,
    yMax = 200000.0,
    lines = listOf(
        LineSegment(0.1, 50.0, 20000.0, 50.0, label = "PS = 50", color = "#dc2626"),
        LineSegment(250.0, 2000.0, 10000.0, 50.0, label = "PS×DN = 500 000", color = "#dc2626"),
        LineSegment(32.0, 3125.0, 32.0, 200000.0, label = "DN = 32", color = "#dc2626"),
        LineSegment(100.0, 3500.0, 7000.0, 50.0, label = "PS×DN = 350 000", labelX = 800.0, labelY = 250.0, color = "#dc2626"),
        LineSegment(100.0, 3500.0, 100.0, 200000.0, label = "DN = 100", color = "#dc2626"),
        LineSegment(32.0, 3125.0, 2000.0, 50.0, label = "PS×DN = 100 000", color = "#dc2626"),
        LineSegment(250.0, 2000.0, 250.0, 200000.0, label = "DN = 250", color = "#dc2626"),
    ),
    categoryZones = listOf(
        CategoryZone("SEP", 5.0, 300.0),
        CategoryZone("Not regulated", 50.0, 5.0),
        CategoryZone("I", 50.0, 25000.0),
        CategoryZone("II", 150.0, 25000.0),
        CategoryZone("III", 1000.0, 25000.0),
    ),
    footerText = "Figure 7 — Graph for piping — Non-dangerous gas",
    applicationText = "All piping that contains fluids at a temperature greater than 350 °C (not applicable to nonmetallic piping) and that falls into category II shall be classified as category III (see figure 7).",
    xVariable = "DN",
)

private val figure8 = GraphConfig(
    id = 8,
    title = "Piping",
    subtitle = "Dangerous liquids",
    equipmentType = "Piping",
    fluidType = "Dangerous liquids",
    xAxisLabel = "DN",
    yAxisLabel = "Design pressure (PS) kPa",
    xMin = 1.0,
    xMax = 10000.0,
    yMin = 1.0,
    yMax = 200000.0,
    lines = listOf(
        LineSegment(25.0, 50000.0, 10000.0, 50000.0, label = "PS = 50 000", color = "#dc2626"),
        LineSegment(25.0, 8000.0, 2000.0, 100.0, label = "PS×DN = 200 000",labelX = 80.0, labelY = 2500.0, color = "#dc2626"),
        LineSegment(200.0, 1000.0, 10000.0, 1000.0, label = "PS = 1 000", color = "#dc2626"),
        LineSegment(0.1, 50.0, 10000.0, 50.0, label = "PS = 50", color = "#dc2626"),
        LineSegment(2000.0, 100.0, 10000.0, 100.0, label = "PS = 100", color = "#dc2626"),
        LineSegment(25.0, 8000.0, 25.0, 200000.0, label = "DN = 25", color = "#dc2626"),
    ),
    categoryZones = listOf(
        CategoryZone("SEP", 5.0, 300.0),
        CategoryZone("Not regulated", 50.0, 5.0),
        CategoryZone("I", 1500.0, 300.0),
        CategoryZone("II", 1500.0, 5000.0),
        CategoryZone("III", 1500.0, 100000.0),
    ),
    footerText = "Figure 8 — Graph for piping — Dangerous liquids",
    applicationText = "Figure 8 shows the various categories for dangerous liquids contained in piping.",
    xVariable = "DN",
)

private val figure9 = GraphConfig(
    id = 9,
    title = "Piping",
    subtitle = "Non-dangerous liquids",
    equipmentType = "Piping",
    fluidType = "Non-dangerous liquids",
    xAxisLabel = "DN",
    yAxisLabel = "Design pressure (PS) kPa",
    xMin = 1.0,
    xMax = 10000.0,
    yMin = 1.0,
    yMax = 200000.0,
    lines = listOf(
        LineSegment(1.0, 50.0, 10000.0, 50.0, label = "PS = 50", color = "#dc2626"),
        LineSegment(200.0, 2500.0, 500.0, 1000.0, label = "PS×DN = 500 000", color = "#dc2626"),
        LineSegment(500.0, 1000.0, 10000.0, 1000.0, label = "PS = 1 000", color = "#dc2626"),
        LineSegment(200.0, 2500.0, 200.0, 200000.0, label = "DN = 200", color = "#dc2626"),
        LineSegment(200.0, 50000.0, 10000.0, 50000.0, label = "PS = 50 000", color = "#dc2626"),
    ),
    categoryZones = listOf(
        CategoryZone("SEP", 5.0, 300.0),
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 1500.0, 5000.0),
        CategoryZone("II", 1500.0, 100000.0),
    ),
    footerText = "Figure 9 — Graph for piping — Non-dangerous liquids",
    applicationText = "Figure 9 shows the various categories for non-dangerous liquids contained in piping.",
    xVariable = "DN",
)

val allGraphs: List<GraphConfig> = listOf(
    figure1, figure2, figure3, figure4, figure5,
    figure6, figure7, figure8, figure9,
)

fun selectFigure(
    equipmentType: String,
    stateOfContents: String,
    fluidGroup: String,
): Int {
    val dangerous = fluidGroup == "Dangerous"
    return if (equipmentType == "Pressure Vessels") {
        if (stateOfContents == "Gas") {
            if (dangerous) 1 else 2
        } else {
            if (dangerous) 3 else 4
        }
    } else {
        if (stateOfContents == "Gas") {
            if (dangerous) 6 else 7
        } else {
            if (dangerous) 8 else 9
        }
    }
}

fun determineCategory(
    figureId: Int,
    ps: Double,
    vOrDn: Double,
): String {
    val product = ps * vOrDn
    return when (figureId) {
        1 -> when {
            ps < 50 -> "Not regulated"
            vOrDn < 1 && ps < 20000 -> "SEP"
            vOrDn < 1 && ps >= 20000 && ps < 100000 -> "SEP"
            vOrDn < 1 && ps >= 100000 -> "IV"
            product <= 2000 && ps >= 50 -> "SEP"
            product > 2000 && product <= 5000 && ps >= 50 -> "I"
            product > 5000 && product <= 20000 && ps >= 50 -> "II"
            product > 20000 && product <= 100000 && ps >= 50 -> "III"
            product > 100000 -> "IV"
            else -> "SEP"
        }
        2 -> when {
            ps < 50 -> "Not regulated"
            vOrDn < 1 && ps < 100000 -> "SEP"
            vOrDn < 1 && ps >= 100000 && ps < 300000 -> "SEP"
            vOrDn < 1 && ps >= 300000 -> "IV"
            product <= 5000 && ps >= 50 && ps < 400 -> "SEP"
            product <= 5000 && ps >= 400 -> "SEP"
            product > 5000 && product <= 20000 && ps >= 50 -> "I"
            product > 20000 && product <= 100000 && ps >= 50 -> "II"
            product > 100000 && product <= 300000 && ps >= 50 -> "III"
            product > 300000 || (ps > 400 && product > 300000) -> "IV"
            else -> "SEP"
        }
        3 -> when {
            ps < 50 -> "Not regulated"
            vOrDn < 1 -> "SEP"
            ps >= 50 && ps < 1000 && product <= 20000 -> "SEP"
            product <= 20000 && ps >= 50 && ps < 1000 -> "SEP"
            ps >= 50 && ps < 1000 && product > 20000 -> "I"
            ps >= 1000 && ps < 50000 -> "II"
            ps >= 50000 -> "III"
            product > 20000 && ps < 1000 -> "I"
            else -> "SEP"
        }
        4 -> when {
            ps < 50 -> "Not regulated"
            vOrDn < 10 -> "SEP"
            ps >= 50 && ps < 1000 && product <= 1000000 -> "SEP"
            product > 1000000 && ps < 1000 -> "I"
            ps >= 1000 && ps < 50000 -> "II"
            ps >= 50000 -> "III"
            else -> "SEP"
        }
        5 -> when {
            ps < 50 -> "Not regulated"
            vOrDn < 2 -> "SEP"
            product <= 5000 && ps >= 50 && ps < 3200 -> "SEP"
            product > 5000 && product <= 20000 && ps >= 50 -> "I"
            product > 20000 && ps >= 50 && ps <= 3200 -> "II"
            (product > 20000 && ps > 3200) || product > 300000 -> "III"
            vOrDn > 1000 && ps > 300 -> "IV"
            product > 300000 -> "IV"
            else -> "II"
        }
        6 -> when {
            ps < 50 -> "Not regulated"
            vOrDn < 25 -> "SEP"
            vOrDn >= 25 && vOrDn < 100 && ps >= 50 && product <= 100000 -> "I"
            vOrDn >= 25 && vOrDn < 100 && product > 100000 -> "II"
            vOrDn >= 100 && vOrDn < 350 && ps < 3500 && product <= 350000 -> "II"
            vOrDn >= 100 && ps >= 3500 -> "III"
            vOrDn >= 350 || product > 350000 -> "III"
            else -> "I"
        }
        7 -> when {
            ps < 50 -> "Not regulated"
            vOrDn < 32 -> "SEP"
            vOrDn >= 32 && vOrDn < 100 && product <= 100000 -> "SEP"
            vOrDn >= 32 && product > 100000 && product <= 350000 -> "I"
            vOrDn >= 100 && vOrDn < 250 && product > 350000 -> "II"
            vOrDn >= 250 || product > 500000 -> "III"
            product > 350000 && vOrDn >= 100 -> "II"
            else -> "I"
        }
        8 -> when {
            ps < 50 -> "Not regulated"
            vOrDn < 25 -> "SEP"
            ps >= 50 && ps < 100 && vOrDn >= 25 -> "SEP"
            ps >= 100 && ps < 1000 && product <= 200000 -> "I"
            ps >= 1000 && ps < 50000 -> "II"
            ps >= 50000 -> "III"
            product > 200000 -> "II"
            else -> "I"
        }
        9 -> when {
            ps < 50 -> "Not regulated"
            vOrDn < 200 -> "SEP"
            ps >= 50 && ps < 1000 && product <= 500000 -> "SEP"
            product > 500000 && ps < 1000 -> "I"
            ps >= 1000 && ps < 50000 -> "II"
            ps >= 50000 -> "III"
            else -> "I"
        }
        else -> "Not regulated"
    }
}

fun getConformityModules(category: String): ConformityModules = when (category) {
    "I" -> ConformityModules("A", "A")
    "II" -> ConformityModules("A2", "A2 or D1 or E1")
    "III" -> ConformityModules(
        "B (design type) + F or B (production type) + C2",
        "H or B (production type) + E or B (design type) + D",
    )
    "IV" -> ConformityModules(
        "G or B (production type) + F",
        "H1 or B (production type) + D",
    )
    else -> ConformityModules("N/A", "N/A")
}

fun getCategoryColorHex(category: String): String = when (category) {
    "SEP" -> "#10b981"
    "I" -> "#3b82f6"
    "II" -> "#eab308"
    "III" -> "#f97316"
    "IV" -> "#ef4444"
    else -> "#6b7280"
}

fun getCategoryRisk(category: String): String = when (category) {
    "SEP" -> "Sound Engineering Practice"
    "Not regulated" -> "Below regulation threshold"
    "I" -> "Low Risk"
    "II" -> "Medium Risk"
    "III" -> "High Risk"
    "IV" -> "Very High Risk"
    else -> ""
}

fun graphById(id: Int): GraphConfig? = allGraphs.find { it.id == id }
