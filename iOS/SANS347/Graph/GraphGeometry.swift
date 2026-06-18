import Foundation
import CoreGraphics

let GRAPH_BASE_WIDTH: CGFloat = 750
let GRAPH_BASE_HEIGHT: CGFloat = 520
let GRAPH_ASPECT_RATIO: CGFloat = GRAPH_BASE_WIDTH / GRAPH_BASE_HEIGHT

struct GraphLayoutMetrics {
    let width: CGFloat
    let height: CGFloat
    let scaleFactor: CGFloat
    let marginTop: CGFloat
    let marginRight: CGFloat
    let marginBottom: CGFloat
    let marginLeft: CGFloat
    let plotW: CGFloat
    let plotH: CGFloat

    static func build(width: CGFloat, height: CGFloat) -> GraphLayoutMetrics {
        let sf = width / GRAPH_BASE_WIDTH
        let marginTop = 20 * sf
        let marginRight = 30 * sf
        let marginBottom = 60 * sf
        let marginLeft = 80 * sf
        return GraphLayoutMetrics(
            width: width,
            height: height,
            scaleFactor: sf,
            marginTop: marginTop,
            marginRight: marginRight,
            marginBottom: marginBottom,
            marginLeft: marginLeft,
            plotW: width - marginLeft - marginRight,
            plotH: height - marginTop - marginBottom
        )
    }
}

/// Log-log projection, ported from `export/GraphGeometry.kt`.
struct GraphGeometry {
    let config: GraphConfig
    let metrics: GraphLayoutMetrics

    private let xMinLog: Double
    private let xMaxLog: Double
    private let yMinLog: Double
    private let yMaxLog: Double

    init(config: GraphConfig, metrics: GraphLayoutMetrics) {
        self.config = config
        self.metrics = metrics
        self.xMinLog = log10(config.xMin)
        self.xMaxLog = log10(config.xMax)
        self.yMinLog = log10(config.yMin)
        self.yMaxLog = log10(config.yMax)
    }

    init(config: GraphConfig, width: CGFloat, height: CGFloat) {
        self.init(config: config, metrics: .build(width: width, height: height))
    }

    func logX(_ value: Double) -> CGFloat {
        let clamped = max(value, config.xMin)
        return metrics.marginLeft +
            CGFloat((log10(clamped) - xMinLog) / (xMaxLog - xMinLog)) * metrics.plotW
    }

    func logY(_ value: Double) -> CGFloat {
        let clamped = max(value, config.yMin)
        return metrics.marginTop + metrics.plotH -
            CGFloat((log10(clamped) - yMinLog) / (yMaxLog - yMinLog)) * metrics.plotH
    }

    var yAxisExponentRange: ClosedRange<Int> {
        Int(floor(log10(config.yMin)))...Int(ceil(log10(config.yMax)))
    }

    var xAxisExponentRange: ClosedRange<Int> {
        Int(floor(log10(config.xMin)))...Int(ceil(log10(config.xMax)))
    }

    func axisPowerValue(_ exponent: Int) -> Double { pow(10.0, Double(exponent)) }
}

/// Mirrors `formatAxisLabel` in `export/GraphGeometry.kt`: thousands grouped
/// with spaces, otherwise a decimal comma (SA/EU convention).
func formatAxisLabel(_ value: Double) -> String {
    if value >= 1000 {
        let formatter = NumberFormatter()
        formatter.numberStyle = .decimal
        formatter.groupingSeparator = " "
        formatter.maximumFractionDigits = 0
        return formatter.string(from: NSNumber(value: value)) ?? String(format: "%.0f", value)
    }
    return String(value).replacingOccurrences(of: ".", with: ",")
}

/// Mirrors `fmtNum` used across the results UI / PDF: integer with space
/// thousands separators.
func fmtNum(_ value: Double) -> String {
    let formatter = NumberFormatter()
    formatter.numberStyle = .decimal
    formatter.groupingSeparator = " "
    formatter.maximumFractionDigits = 0
    return formatter.string(from: NSNumber(value: value)) ?? String(format: "%.0f", value)
}
