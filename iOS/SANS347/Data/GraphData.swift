import Foundation

// Figure configurations transcribed verbatim from
// `Android/.../data/GraphData.kt` (the Kotlin and TS data are identical).
// Coordinates are in log-log data space; do not "tidy" these values — they
// encode the standard's boundary curves.

private let figure1 = GraphConfig(
    id: 1,
    title: "Pressure Vessels",
    subtitle: "Dangerous gas",
    equipmentType: "Pressure Vessels",
    fluidType: "Dangerous gas",
    xAxisLabel: "Volume (V) L",
    yAxisLabel: "Design pressure (PS) kPa",
    xMin: 0.1,
    xMax: 10000.0,
    yMin: 0.1,
    yMax: 400000.0,
    lines: [
        LineSegment(0.1, 100000.0, 1.0, 100000.0, label: "PS = 100 000", color: "#dc2626"),
        LineSegment(1.0, 100000.0, 2000.0, 50.0, label: "PS×V = 100 000", color: "#dc2626"),
        LineSegment(0.1, 20000.0, 1.0, 20000.0, label: "PS = 20 000", color: "#dc2626"),
        LineSegment(1.0, 20000.0, 400.0, 50.0, label: "PS×V = 20 000", color: "#dc2626"),
        LineSegment(1.0, 2000.0, 1.0, 100000.0, label: "V = 1", color: "#dc2626"),
        LineSegment(1.0, 5000.0, 100.0, 50.0, label: "PS×V = 5 000", color: "#dc2626"),
        LineSegment(0.1, 50.0, 100000.0, 50.0, label: "PS = 50", labelX: 0.3, labelY: 55.0, color: "#dc2626"),
        LineSegment(1.0, 2000.0, 40.0, 50.0, label: "PS×V = 2 000", color: "#dc2626"),
    ],
    categoryZones: [
        CategoryZone("SEP", 0.3, 300.0),
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 45.0, 60.0),
        CategoryZone("II", 150.0, 60.0),
        CategoryZone("III", 600.0, 60.0),
        CategoryZone("IV", 600.0, 30000.0),
    ],
    footerText: "Figure 1 — Graph for vessels — Dangerous gas",
    applicationText: "Vessels that fall within categories I or II and that are intended to contain an unstable gas, shall be classified as category III (see figure 1).",
    xVariable: "V"
)

private let figure2 = GraphConfig(
    id: 2,
    title: "Pressure Vessels",
    subtitle: "Non-dangerous gas",
    equipmentType: "Pressure Vessels",
    fluidType: "Non-dangerous gas",
    xAxisLabel: "Volume (V) L",
    yAxisLabel: "Design pressure (PS) kPa",
    xMin: 0.1,
    xMax: 10000.0,
    yMin: 0.1,
    yMax: 500000.0,
    lines: [
        LineSegment(0.1, 300000.0, 1.0, 300000.0, label: "PS = 300 000", color: "#dc2626"),
        LineSegment(1.0, 300000.0, 750.0, 400.0, label: "PS×V = 300 000", color: "#dc2626"),
        LineSegment(0.1, 100000.0, 1.0, 100000.0, label: "PS = 100 000", color: "#dc2626"),
        LineSegment(1.0, 100000.0, 2000.0, 50.0, label: "PS×V = 100 000", color: "#dc2626"),
        LineSegment(0.1, 50.0, 10000.0, 50.0, label: "PS = 50", color: "#dc2626"),
        LineSegment(1.0, 20000.0, 400.0, 50.0, label: "PS×V = 20 000", color: "#dc2626"),
        LineSegment(750.0, 400.0, 10000.0, 400.0, label: "PS = 400", color: "#dc2626"),
        LineSegment(1.0, 5000.0, 100.0, 50.0, label: "PS×V = 5 000", color: "#dc2626"),
        LineSegment(1.0, 5000.0, 1.0, 100000.0, label: "V = 1", color: "#dc2626"),
    ],
    categoryZones: [
        CategoryZone("SEP", 0.3, 300.0),
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 150.0, 60.0),
        CategoryZone("II", 600.0, 60.0),
        CategoryZone("III", 3000.0, 60.0),
        CategoryZone("IV", 3000.0, 30000.0),
    ],
    footerText: "Figure 2 — Graph for vessels — Non-dangerous gas",
    applicationText: "Portable fire extinguishers up to 3 000 kPa shall be classified as at least category III (see figure 2).",
    xVariable: "V"
)

private let figure3 = GraphConfig(
    id: 3,
    title: "Pressure Vessels",
    subtitle: "Dangerous liquids",
    equipmentType: "Pressure Vessels",
    fluidType: "Dangerous liquids",
    xAxisLabel: "Volume (V) L",
    yAxisLabel: "Design pressure (PS) kPa",
    xMin: 0.1,
    xMax: 10000.0,
    yMin: 0.1,
    yMax: 500000.0,
    lines: [
        LineSegment(0.1, 50000.0, 10000.0, 50000.0, label: "PS = 50 000", color: "#dc2626"),
        LineSegment(1.0, 20000.0, 400.0, 50.0, label: "PS×V = 20 000", labelX: 5.0, labelY: 4000.0, color: "#dc2626"),
        LineSegment(20.0, 1000.0, 10000.0, 1000.0, label: "PS = 1 000", color: "#dc2626"),
        LineSegment(0.1, 50.0, 10000.0, 50.0, label: "PS = 50", color: "#dc2626"),
        LineSegment(1.0, 20000.0, 1.0, 500000.0, label: "V = 1", color: "#dc2626"),
    ],
    categoryZones: [
        CategoryZone("SEP", 0.3, 300.0),
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 1200.0, 60.0),
        CategoryZone("II", 0.3, 100000.0),
        CategoryZone("II", 1200.0, 5000.0),
        CategoryZone("III", 1200.0, 150000.0),
    ],
    footerText: "Figure 3 — Graph for vessels — Dangerous liquids",
    applicationText: "Figure 3 shows the various categories for dangerous liquids contained in vessels.",
    xVariable: "V"
)

private let figure4 = GraphConfig(
    id: 4,
    title: "Pressure Vessels",
    subtitle: "Non-dangerous liquids",
    equipmentType: "Pressure Vessels",
    fluidType: "Non-dangerous liquids",
    xAxisLabel: "Volume (V) L",
    yAxisLabel: "Design pressure (PS) kPa",
    xMin: 0.1,
    xMax: 100000.0,
    yMin: 0.1,
    yMax: 500000.0,
    lines: [
        LineSegment(0.1, 50.0, 100000.0, 50.0, label: "PS = 50", color: "#dc2626"),
        LineSegment(10.0, 100000.0, 1000.0, 1000.0, label: "PS×V = 1 000 000", color: "#dc2626"),
        LineSegment(0.1, 100000.0, 10.0, 100000.0, label: "PS = 100 000", color: "#dc2626"),
        LineSegment(1000.0, 1000.0, 100000.0, 1000.0, label: "PS = 1 000", color: "#dc2626"),
        LineSegment(20.0, 50000.0, 100000.0, 50000.0, label: "PS = 50 000", color: "#dc2626"),
        LineSegment(10.0, 100000.0, 10.0, 500000.0, label: "V = 10", color: "#dc2626"),
    ],
    categoryZones: [
        CategoryZone("SEP", 0.3, 300.0),
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 2.0, 200000.0),
        CategoryZone("I", 15000.0, 5000.0),
        CategoryZone("II", 15000.0, 150000.0),
    ],
    footerText: "Figure 4 — Graph for vessels — Non-dangerous liquids",
    applicationText: "Assemblies intended for generating warm water shall be subjected to a type approval. See table 2 category 3 for warm water.",
    xVariable: "V"
)

private let figure5 = GraphConfig(
    id: 5,
    title: "Steam generators",
    subtitle: "",
    equipmentType: "Steam Generator",
    fluidType: "",
    xAxisLabel: "Volume (V) L",
    yAxisLabel: "Design pressure (PS) kPa",
    xMin: 0.1,
    xMax: 10000.0,
    yMin: 0.1,
    yMax: 100000.0,
    lines: [
        LineSegment(0.1, 50.0, 10000.0, 50.0, label: "PS = 50", color: "#dc2626"),
        LineSegment(93.75, 3200.0, 1000.0, 300.0, label: "PS×V = 300 000", color: "#dc2626"),
        LineSegment(2.0, 3200.0, 93.75, 3200.0, label: "PS = 3 200", color: "#dc2626"),
        LineSegment(6.25, 3200.0, 400.0, 50.0, label: "PS×V = 20 000", color: "#dc2626"),
        LineSegment(2.0, 2500.0, 2.0, 100000.0, label: "V = 2", color: "#dc2626"),
        LineSegment(2.0, 2500.0, 100.0, 50.0, label: "PS×V = 5 000", color: "#dc2626"),
        LineSegment(1000.0, 50.0, 1000.0, 300.0, label: "V = 1 000", color: "#dc2626"),
    ],
    categoryZones: [
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 0.5, 1000.0),
        CategoryZone("II", 50.0, 150.0),
        CategoryZone("III", 200.0, 500.0),
        CategoryZone("IV", 500.0, 15000.0),
    ],
    footerText: "Figure 5 — Graph for steam generators",
    applicationText: "The design of jacketed pressure cookers shall be subjected to a conformity assessment procedure equivalent to at least one of the category III modules (see figure 5).",
    xVariable: "V"
)

private let figure6 = GraphConfig(
    id: 6,
    title: "Piping",
    subtitle: "Dangerous gas",
    equipmentType: "Piping",
    fluidType: "Dangerous gas",
    xAxisLabel: "DN",
    yAxisLabel: "Design pressure (PS) kPa",
    xMin: 1.0,
    xMax: 2000.0,
    yMin: 1.0,
    yMax: 100000.0,
    lines: [
        LineSegment(1.0, 50.0, 2000.0, 50.0, label: "PS = 50", color: "#dc2626"),
        LineSegment(350.0, 1000.0, 100.0, 3500.0, label: "PS×DN = 350 000", color: "#dc2626"),
        LineSegment(25.0, 50.0, 25.0, 100000.0, label: "DN = 25", color: "#dc2626"),
        LineSegment(100.0, 1000.0, 25.0, 4000.0, label: "PS×DN = 100 000", color: "#dc2626"),
        LineSegment(100.0, 3500.0, 100.0, 100000.0, label: "DN = 100", color: "#dc2626"),
        LineSegment(350.0, 50.0, 350.0, 1000.0, label: "DN = 350", color: "#dc2626"),
        LineSegment(100.0, 50.0, 100.0, 1000.0, color: "#dc2626"),
    ],
    categoryZones: [
        CategoryZone("SEP", 5.0, 300.0),
        CategoryZone("Not regulated", 50.0, 5.0),
        CategoryZone("I", 50.0, 300.0),
        CategoryZone("II", 200.0, 300.0),
        CategoryZone("III", 700.0, 300.0),
    ],
    footerText: "Figure 6 — Graph for piping — Dangerous gas",
    applicationText: "Piping that is intended for unstable gases that fall within categories I or II shall be classified as category III (see figure 6).",
    xVariable: "DN"
)

private let figure7 = GraphConfig(
    id: 7,
    title: "Piping",
    subtitle: "Non-dangerous gas",
    equipmentType: "Piping",
    fluidType: "Non-dangerous gas",
    xAxisLabel: "DN",
    yAxisLabel: "Design pressure (PS) kPa",
    xMin: 1.0,
    xMax: 20000.0,
    yMin: 1.0,
    yMax: 200000.0,
    lines: [
        LineSegment(0.1, 50.0, 20000.0, 50.0, label: "PS = 50", color: "#dc2626"),
        LineSegment(250.0, 2000.0, 10000.0, 50.0, label: "PS×DN = 500 000", color: "#dc2626"),
        LineSegment(32.0, 3125.0, 32.0, 200000.0, label: "DN = 32", color: "#dc2626"),
        LineSegment(100.0, 3500.0, 7000.0, 50.0, label: "PS×DN = 350 000", labelX: 800.0, labelY: 250.0, color: "#dc2626"),
        LineSegment(100.0, 3500.0, 100.0, 200000.0, label: "DN = 100", color: "#dc2626"),
        LineSegment(32.0, 3125.0, 2000.0, 50.0, label: "PS×DN = 100 000", color: "#dc2626"),
        LineSegment(250.0, 2000.0, 250.0, 200000.0, label: "DN = 250", color: "#dc2626"),
    ],
    categoryZones: [
        CategoryZone("SEP", 5.0, 300.0),
        CategoryZone("Not regulated", 50.0, 5.0),
        CategoryZone("I", 50.0, 25000.0),
        CategoryZone("II", 150.0, 25000.0),
        CategoryZone("III", 1000.0, 25000.0),
    ],
    footerText: "Figure 7 — Graph for piping — Non-dangerous gas",
    applicationText: "All piping that contains fluids at a temperature greater than 350 °C (not applicable to nonmetallic piping) and that falls into category II shall be classified as category III (see figure 7).",
    xVariable: "DN"
)

private let figure8 = GraphConfig(
    id: 8,
    title: "Piping",
    subtitle: "Dangerous liquids",
    equipmentType: "Piping",
    fluidType: "Dangerous liquids",
    xAxisLabel: "DN",
    yAxisLabel: "Design pressure (PS) kPa",
    xMin: 1.0,
    xMax: 10000.0,
    yMin: 1.0,
    yMax: 200000.0,
    lines: [
        LineSegment(25.0, 50000.0, 10000.0, 50000.0, label: "PS = 50 000", color: "#dc2626"),
        LineSegment(25.0, 8000.0, 2000.0, 100.0, label: "PS×DN = 200 000", labelX: 80.0, labelY: 2500.0, color: "#dc2626"),
        LineSegment(200.0, 1000.0, 10000.0, 1000.0, label: "PS = 1 000", color: "#dc2626"),
        LineSegment(0.1, 50.0, 10000.0, 50.0, label: "PS = 50", color: "#dc2626"),
        LineSegment(2000.0, 100.0, 10000.0, 100.0, label: "PS = 100", color: "#dc2626"),
        LineSegment(25.0, 8000.0, 25.0, 200000.0, label: "DN = 25", color: "#dc2626"),
    ],
    categoryZones: [
        CategoryZone("SEP", 5.0, 300.0),
        CategoryZone("Not regulated", 50.0, 5.0),
        CategoryZone("I", 1500.0, 300.0),
        CategoryZone("II", 1500.0, 5000.0),
        CategoryZone("III", 1500.0, 100000.0),
    ],
    footerText: "Figure 8 — Graph for piping — Dangerous liquids",
    applicationText: "Figure 8 shows the various categories for dangerous liquids contained in piping.",
    xVariable: "DN"
)

private let figure9 = GraphConfig(
    id: 9,
    title: "Piping",
    subtitle: "Non-dangerous liquids",
    equipmentType: "Piping",
    fluidType: "Non-dangerous liquids",
    xAxisLabel: "DN",
    yAxisLabel: "Design pressure (PS) kPa",
    xMin: 1.0,
    xMax: 10000.0,
    yMin: 1.0,
    yMax: 200000.0,
    lines: [
        LineSegment(1.0, 50.0, 10000.0, 50.0, label: "PS = 50", color: "#dc2626"),
        LineSegment(200.0, 2500.0, 500.0, 1000.0, label: "PS×DN = 500 000", color: "#dc2626"),
        LineSegment(500.0, 1000.0, 10000.0, 1000.0, label: "PS = 1 000", color: "#dc2626"),
        LineSegment(200.0, 2500.0, 200.0, 200000.0, label: "DN = 200", color: "#dc2626"),
        LineSegment(200.0, 50000.0, 10000.0, 50000.0, label: "PS = 50 000", color: "#dc2626"),
    ],
    categoryZones: [
        CategoryZone("SEP", 5.0, 300.0),
        CategoryZone("Not regulated", 30.0, 5.0),
        CategoryZone("I", 1500.0, 5000.0),
        CategoryZone("II", 1500.0, 100000.0),
    ],
    footerText: "Figure 9 — Graph for piping — Non-dangerous liquids",
    applicationText: "Figure 9 shows the various categories for non-dangerous liquids contained in piping.",
    xVariable: "DN"
)

let allGraphs: [GraphConfig] = [
    figure1, figure2, figure3, figure4, figure5,
    figure6, figure7, figure8, figure9,
]

func graphById(_ id: Int) -> GraphConfig? {
    allGraphs.first { $0.id == id }
}
