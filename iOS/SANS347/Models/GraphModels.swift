import Foundation

/// A boundary segment in log-log data space. Mirrors `LineSegment` in
/// `data/GraphModels.kt` and the TS interface in `lib/graphData.ts`.
struct LineSegment {
    let x1: Double
    let y1: Double
    let x2: Double
    let y2: Double
    var label: String? = nil
    var labelPosition: String? = nil
    var labelOffset: Double? = nil
    var labelX: Double? = nil
    var labelY: Double? = nil
    var color: String? = nil

    init(
        _ x1: Double,
        _ y1: Double,
        _ x2: Double,
        _ y2: Double,
        label: String? = nil,
        labelPosition: String? = nil,
        labelOffset: Double? = nil,
        labelX: Double? = nil,
        labelY: Double? = nil,
        color: String? = nil
    ) {
        self.x1 = x1
        self.y1 = y1
        self.x2 = x2
        self.y2 = y2
        self.label = label
        self.labelPosition = labelPosition
        self.labelOffset = labelOffset
        self.labelX = labelX
        self.labelY = labelY
        self.color = color
    }
}

/// A category label anchored in log-log data space (display only).
struct CategoryZone {
    let label: String
    let x: Double
    let y: Double

    init(_ label: String, _ x: Double, _ y: Double) {
        self.label = label
        self.x = x
        self.y = y
    }
}

/// Static configuration for one of the nine categorization figures.
struct GraphConfig {
    let id: Int
    let title: String
    let subtitle: String
    let equipmentType: String
    let fluidType: String
    let xAxisLabel: String
    let yAxisLabel: String
    let xMin: Double
    let xMax: Double
    let yMin: Double
    let yMax: Double
    let lines: [LineSegment]
    let categoryZones: [CategoryZone]
    let footerText: String
    let applicationText: String
    /// "V" | "DN"
    let xVariable: String
}

/// The computed result of a categorization, retaining the full set of inputs
/// (matching the Android `ResultData` so the summary never has to be inferred).
struct ResultData: Equatable {
    let category: String
    let figureId: Int
    let product: Double
    let ps: Double
    let vOrDn: Double
    let equipmentType: String
    let stateOfContents: String
    let fluidGroup: String
}

/// Required conformity-assessment modules for a category (Table 2).
struct ConformityModules {
    let withoutQuality: String
    let withQuality: String
}
