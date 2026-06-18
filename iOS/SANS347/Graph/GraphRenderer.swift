import UIKit
import CoreGraphics

struct GraphPlotPoint {
    let x: Double
    let y: Double
    let colorHex: String
}

/// The single canonical draw pipeline, ported from `ui/graph/Sans347Graph.kt`
/// (and the export `GraphBitmapRenderer.kt`, which is identical). Used for the
/// on-screen view, the PNG export and the PDF report so they are pixel-aligned.
///
/// The passed `ctx` must be UIKit-oriented (top-left origin, y increasing
/// downward) and be the current UIKit graphics context, since text is drawn
/// with `NSAttributedString`. This holds for `UIView.draw(_:)`,
/// `UIGraphicsImageRenderer` and `UIGraphicsPDFRenderer`.
enum GraphRenderer {
    static func draw(config: GraphConfig, plotPoint: GraphPlotPoint?, in ctx: CGContext, size: CGSize) {
        let w = size.width
        let h = size.height
        let sf = w / 750.0
        let geo = GraphGeometry(config: config, width: w, height: h)
        let m = geo.metrics

        // Layer 0 — white background.
        ctx.setFillColor(UIColor.white.cgColor)
        ctx.fill(CGRect(x: 0, y: 0, width: w, height: h))

        // Layer 1 — gridlines.
        drawGrid(config: config, geo: geo, m: m, ctx: ctx)

        // Layer 2 — plot border.
        ctx.setStrokeColor(UIColor(hex: "#374151").cgColor)
        ctx.setLineWidth(1)
        ctx.stroke(CGRect(x: m.marginLeft, y: m.marginTop, width: m.plotW, height: m.plotH))

        // Layer 3 — boundary lines + labels.
        drawLines(config: config, geo: geo, m: m, sf: sf, ctx: ctx)

        // Layer 4 — category zone labels.
        drawZones(config: config, geo: geo, m: m, sf: sf, ctx: ctx)

        // Layer 5 — axis tick labels.
        drawTickLabels(config: config, geo: geo, m: m, sf: sf, ctx: ctx)

        // Layer 6 — axis titles.
        drawAxisTitles(config: config, m: m, sf: sf, w: w, h: h, ctx: ctx)

        // Layer 7 — user plot point.
        if let pt = plotPoint {
            drawPlotPoint(pt: pt, geo: geo, m: m, sf: sf, ctx: ctx)
        }
    }

    // MARK: - Layers

    private static func drawGrid(config: GraphConfig, geo: GraphGeometry, m: GraphLayoutMetrics, ctx: CGContext) {
        let major = UIColor(hex: "#9CA3AF").cgColor
        let minor = UIColor(hex: "#C8CCD2").cgColor
        ctx.setLineWidth(0.5)

        for e in geo.yAxisExponentRange {
            let base = pow(10.0, Double(e))
            if base >= config.yMin && base <= config.yMax {
                let py = geo.logY(base)
                stroke(ctx, color: major, from: CGPoint(x: m.marginLeft, y: py), to: CGPoint(x: m.marginLeft + m.plotW, y: py))
            }
            for mm in 2...9 {
                let v = base * Double(mm)
                if v >= config.yMin && v <= config.yMax {
                    let py = geo.logY(v)
                    stroke(ctx, color: minor, from: CGPoint(x: m.marginLeft, y: py), to: CGPoint(x: m.marginLeft + m.plotW, y: py))
                }
            }
        }

        for e in geo.xAxisExponentRange {
            let base = pow(10.0, Double(e))
            if base >= config.xMin && base <= config.xMax {
                let px = geo.logX(base)
                stroke(ctx, color: major, from: CGPoint(x: px, y: m.marginTop), to: CGPoint(x: px, y: m.marginTop + m.plotH))
            }
            for mm in 2...9 {
                let v = base * Double(mm)
                if v >= config.xMin && v <= config.xMax {
                    let px = geo.logX(v)
                    stroke(ctx, color: minor, from: CGPoint(x: px, y: m.marginTop), to: CGPoint(x: px, y: m.marginTop + m.plotH))
                }
            }
        }
    }

    private static func drawLines(config: GraphConfig, geo: GraphGeometry, m: GraphLayoutMetrics, sf: CGFloat, ctx: CGContext) {
        for line in config.lines {
            let x1p = geo.logX(line.x1)
            let y1p = geo.logY(line.y1)
            let x2p = geo.logX(line.x2)
            let y2p = geo.logY(line.y2)
            let lx1 = clampValue(x1p, m.marginLeft, m.marginLeft + m.plotW)
            let ly1 = clampValue(y1p, m.marginTop, m.marginTop + m.plotH)
            let lx2 = clampValue(x2p, m.marginLeft, m.marginLeft + m.plotW)
            let ly2 = clampValue(y2p, m.marginTop, m.marginTop + m.plotH)
            let lineColor = (line.color.map { UIColor(hex: $0) } ?? UIColor(hex: "#DC2626")).cgColor
            ctx.setLineWidth(1.5)
            stroke(ctx, color: lineColor, from: CGPoint(x: lx1, y: ly1), to: CGPoint(x: lx2, y: ly2))

            guard let labelText = line.label else { continue }
            let labelPosX = line.labelX.map { geo.logX($0) } ?? (x1p + x2p) / 2
            let labelPosY = line.labelY.map { geo.logY($0) } ?? (y1p + y2p) / 2
            let angle = atan2(Double(y2p - y1p), Double(x2p - x1p))
            let maxY = max(line.y1, line.y2)
            let maxX = max(line.x1, line.x2)
            let isHorizontal = abs(line.y1 - line.y2) < 0.01 * maxY
            let isVertical = abs(line.x1 - line.x2) < 0.01 * maxX
            let fillColor = (isHorizontal || isVertical) ? UIColor(hex: "#374151") : UIColor(hex: "#DC2626")
            let textSize = max(8, 10 * sf)
            let baseOffset = -6 * sf
            let extraOffset = CGFloat(line.labelOffset ?? 0) * sf

            ctx.saveGState()
            ctx.translateBy(x: labelPosX, y: labelPosY)
            if !isHorizontal && !isVertical {
                ctx.rotate(by: CGFloat(angle))
            } else if isVertical {
                ctx.rotate(by: -.pi / 2)
            }
            drawBaselineText(
                labelText,
                baselineX: 0,
                baselineY: baseOffset + extraOffset,
                font: UIFont.systemFont(ofSize: textSize),
                color: fillColor,
                align: .center
            )
            ctx.restoreGState()
        }
    }

    private static func drawZones(config: GraphConfig, geo: GraphGeometry, m: GraphLayoutMetrics, sf: CGFloat, ctx: CGContext) {
        let font = UIFont.boldSystemFont(ofSize: max(10, 14 * sf))
        let color = UIColor(hex: "#6b7280")
        for zone in config.categoryZones {
            let zx = geo.logX(zone.x)
            let zy = geo.logY(zone.y)
            if zx > m.marginLeft && zx < m.marginLeft + m.plotW && zy > m.marginTop && zy < m.marginTop + m.plotH {
                drawBaselineText(zone.label, baselineX: zx, baselineY: zy, font: font, color: color, align: .center)
            }
        }
    }

    private static func drawTickLabels(config: GraphConfig, geo: GraphGeometry, m: GraphLayoutMetrics, sf: CGFloat, ctx: CGContext) {
        let font = UIFont.systemFont(ofSize: max(9, 11 * sf))
        let color = UIColor(hex: "#374151")
        for e in geo.yAxisExponentRange {
            let base = pow(10.0, Double(e))
            if base >= config.yMin && base <= config.yMax {
                let py = geo.logY(base)
                drawBaselineText(formatAxisLabel(base), baselineX: m.marginLeft - 8 * sf, baselineY: py + 4, font: font, color: color, align: .right)
            }
        }
        for e in geo.xAxisExponentRange {
            let base = pow(10.0, Double(e))
            if base >= config.xMin && base <= config.xMax {
                let px = geo.logX(base)
                drawBaselineText(formatAxisLabel(base), baselineX: px, baselineY: m.marginTop + m.plotH + 18 * sf, font: font, color: color, align: .center)
            }
        }
    }

    private static func drawAxisTitles(config: GraphConfig, m: GraphLayoutMetrics, sf: CGFloat, w: CGFloat, h: CGFloat, ctx: CGContext) {
        let font = UIFont.boldSystemFont(ofSize: max(10, 12 * sf))
        let color = UIColor(hex: "#1f2937")
        drawBaselineText(config.xAxisLabel, baselineX: m.marginLeft + m.plotW / 2, baselineY: h - 10 * sf, font: font, color: color, align: .center)

        ctx.saveGState()
        ctx.translateBy(x: 15 * sf, y: m.marginTop + m.plotH / 2)
        ctx.rotate(by: -.pi / 2)
        drawBaselineText(config.yAxisLabel, baselineX: 0, baselineY: 0, font: font, color: color, align: .center)
        ctx.restoreGState()
    }

    private static func drawPlotPoint(pt: GraphPlotPoint, geo: GraphGeometry, m: GraphLayoutMetrics, sf: CGFloat, ctx: CGContext) {
        let px = geo.logX(pt.x)
        let py = geo.logY(pt.y)
        guard px >= m.marginLeft && px <= m.marginLeft + m.plotW && py >= m.marginTop && py <= m.marginTop + m.plotH else { return }
        let color = UIColor(hex: pt.colorHex)

        // Dashed crosshairs.
        ctx.saveGState()
        ctx.setLineDash(phase: 0, lengths: [4, 4])
        ctx.setLineWidth(1.5)
        stroke(ctx, color: color.cgColor, from: CGPoint(x: px, y: m.marginTop), to: CGPoint(x: px, y: m.marginTop + m.plotH))
        stroke(ctx, color: color.cgColor, from: CGPoint(x: m.marginLeft, y: py), to: CGPoint(x: m.marginLeft + m.plotW, y: py))
        ctx.restoreGState()

        // Marker: filled circle + white ring.
        let r = max(5, 8 * sf)
        let rect = CGRect(x: px - r, y: py - r, width: r * 2, height: r * 2)
        ctx.setFillColor(color.cgColor)
        ctx.fillEllipse(in: rect)
        ctx.setStrokeColor(UIColor.white.cgColor)
        ctx.setLineWidth(2)
        ctx.strokeEllipse(in: rect)

        // "Your Equipment" pill.
        let labelText = "Your Equipment"
        let labelFont = UIFont.boldSystemFont(ofSize: max(9, 11 * sf))
        let tw = (labelText as NSString).size(withAttributes: [.font: labelFont]).width
        let pillPad = 6 * sf
        let rx = px + 12 * sf - pillPad
        let ry = py - 12 * sf - 12 * sf
        let rw = tw + pillPad * 2
        let rh = 20 * sf
        let radius = 4 * sf
        let pillRect = CGRect(x: rx, y: ry, width: rw, height: rh)
        let path = UIBezierPath(roundedRect: pillRect, cornerRadius: radius)
        ctx.setFillColor(color.cgColor)
        ctx.addPath(path.cgPath)
        ctx.fillPath()
        drawBaselineText(labelText, baselineX: px + 12 * sf, baselineY: py - 12 * sf + 2, font: labelFont, color: .white, align: .left)
    }

    // MARK: - Helpers

    private static func stroke(_ ctx: CGContext, color: CGColor, from: CGPoint, to: CGPoint) {
        ctx.setStrokeColor(color)
        ctx.beginPath()
        ctx.move(to: from)
        ctx.addLine(to: to)
        ctx.strokePath()
    }

    private static func clampValue(_ v: CGFloat, _ lo: CGFloat, _ hi: CGFloat) -> CGFloat {
        min(max(v, lo), hi)
    }

    /// Draws text whose baseline sits at (baselineX, baselineY), matching the
    /// Android `Canvas.drawText` semantics. `NSAttributedString` positions by
    /// the top-left, so we offset by the font ascender and the measured width.
    private static func drawBaselineText(_ s: String, baselineX: CGFloat, baselineY: CGFloat, font: UIFont, color: UIColor, align: NSTextAlignment) {
        let attrs: [NSAttributedString.Key: Any] = [.font: font, .foregroundColor: color]
        let textSize = (s as NSString).size(withAttributes: attrs)
        var x = baselineX
        switch align {
        case .center: x = baselineX - textSize.width / 2
        case .right: x = baselineX - textSize.width
        default: break
        }
        let top = baselineY - font.ascender
        (s as NSString).draw(at: CGPoint(x: x, y: top), withAttributes: attrs)
    }
}
