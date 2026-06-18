import UIKit

/// US-Letter (612x792 pt) one-page PDF report, ported from
/// `export/ReportPdfExporter.kt`. Reuses the shared `GraphRenderer` (via
/// `GraphImageExporter`) for the embedded figure so the graph matches the app.
enum ReportPDFExporter {
    private static let pageWidth: CGFloat = 612
    private static let pageHeight: CGFloat = 792
    private static let margin: CGFloat = 40
    private static let cardGap: CGFloat = 12
    private static let cardPadding: CGFloat = 10
    private static let cardRadius: CGFloat = 8
    private static let headerCardHeight: CGFloat = 100
    private static let compactLine: CGFloat = 11
    private static var contentWidth: CGFloat { pageWidth - margin * 2 }

    private static let cardFill = "#F9FAFB"
    private static let cardBorder = "#E5E7EB"
    private static let warningBg = "#FEFCE8"
    private static let warningBorder = "#FDE68A"
    private static let primaryCyan = "#00C2FF"
    private static let gray500 = "#6B7280"
    private static let gray700 = "#374151"
    private static let axisTitle = "#1F2937"

    static func exportToTempFile(
        result: ResultData,
        graph: GraphConfig,
        catRisk: String,
        conformity: ConformityModules,
        plotPoint: GraphPlotPoint?
    ) -> URL? {
        let graphImage = GraphImageExporter.renderImage(config: graph, plotPoint: plotPoint)
        let bounds = CGRect(x: 0, y: 0, width: pageWidth, height: pageHeight)
        let renderer = UIGraphicsPDFRenderer(bounds: bounds)

        let safeCategory = result.category.replacingOccurrences(of: " ", with: "_")
        let url = FileManager.default.temporaryDirectory
            .appendingPathComponent("sans347-report-\(safeCategory)-figure-\(graph.id).pdf")

        do {
            try renderer.writePDF(to: url) { rendererContext in
                rendererContext.beginPage()
                let ctx = rendererContext.cgContext

                var y = margin
                y = drawTitle(ctx, y, "SANS 347 Calculation Results")
                y += 6

                y = drawHeaderCards(ctx, y, result, graph, catRisk)
                y += 6

                let graphWidth = contentWidth
                let graphHeight = graphWidth * (GRAPH_BASE_HEIGHT / GRAPH_BASE_WIDTH)
                graphImage.draw(in: CGRect(x: margin, y: y, width: graphWidth, height: graphHeight))
                y += graphHeight + 6

                y = drawFooterCards(ctx, y, result, conformity)
                y += 8

                let formatter = DateFormatter()
                formatter.dateFormat = "yyyy-MM-dd HH:mm"
                let timestamp = formatter.string(from: Date())
                _ = drawTextLine(ctx, margin, y, "Figure \(graph.id) — \(graph.footerText)", 8, gray500)
                _ = drawTextLine(ctx, margin, y + 10, "Generated \(timestamp)", 8, gray500)
            }
            return url
        } catch {
            return nil
        }
    }

    // MARK: - Sections

    private static func drawHeaderCards(_ ctx: CGContext, _ top: CGFloat, _ result: ResultData, _ graph: GraphConfig, _ catRisk: String) -> CGFloat {
        let cardWidth = (contentWidth - cardGap) / 2
        let leftX = margin
        let rightX = margin + cardWidth + cardGap

        drawRoundedCard(ctx, leftX, top, cardWidth, headerCardHeight, cardFill, cardBorder)
        drawRoundedCard(ctx, rightX, top, cardWidth, headerCardHeight, cardFill, cardBorder)

        var leftY = top + cardPadding + 10
        let leftTextX = leftX + cardPadding
        let leftMaxWidth = cardWidth - cardPadding * 2

        leftY = drawTextLine(ctx, leftTextX, leftY, "Your Equipment Category", 10, axisTitle, bold: true)
        leftY += 2
        leftY = drawTextLine(ctx, leftTextX, leftY, formatCategoryDisplayLabel(result.category), 10, getCategoryColorHex(result.category), bold: true)
        leftY = drawTextLine(ctx, leftTextX, leftY, catRisk, 9, gray500)
        var figureLine = "Figure \(graph.id) — \(graph.title)"
        if !graph.subtitle.isEmpty { figureLine += " — \(graph.subtitle)" }
        _ = drawWrappedText(ctx, leftTextX, leftY, figureLine, leftMaxWidth, 9, compactLine, gray700)

        var rightY = top + cardPadding + 10
        let rightTextX = rightX + cardPadding
        let rightMaxWidth = cardWidth - cardPadding * 2
        let isPiping = graph.xVariable == "DN"
        let equipLabel = graph.equipmentType == "Pressure Vessels" ? "Vessels" : graph.equipmentType
        let isSteamGen = result.equipmentType == "Steam Generator"

        rightY = drawTextLine(ctx, rightTextX, rightY, "Input Parameters", 10, axisTitle, bold: true)
        rightY += 2
        rightY = drawLabelValueRow(ctx, rightTextX, rightY, rightMaxWidth, "Equipment Type:", equipLabel)
        rightY = drawLabelValueRow(ctx, rightTextX, rightY, rightMaxWidth, "Design Pressure:", "\(fmtNum(result.ps)) kPa")
        if !isSteamGen {
            rightY = drawLabelValueRow(ctx, rightTextX, rightY, rightMaxWidth, "State Contents:", result.stateOfContents)
        }
        rightY = drawLabelValueRow(ctx, rightTextX, rightY, rightMaxWidth, isPiping ? "Diameter:" : "Volume:", "\(fmtNum(result.vOrDn)) \(isPiping ? "DN" : "L")")
        if !isSteamGen {
            _ = drawLabelValueRow(ctx, rightTextX, rightY, rightMaxWidth, "Fluid Group:", result.fluidGroup)
        }

        return top + headerCardHeight
    }

    private static func drawFooterCards(_ ctx: CGContext, _ top: CGFloat, _ result: ResultData, _ conformity: ConformityModules) -> CGFloat {
        var y = top
        let cardWidth = contentWidth
        let textX = margin + cardPadding
        let maxWidth = cardWidth - cardPadding * 2
        let bodySize: CGFloat = 8.5
        let headingBlock = cardPadding + 22

        let prEngText = requiresPrEng(result.category)
            ? "Professional Review: Pr Eng sign-off required. Category \(result.category) pressure equipment must be signed off by a Professional Registered Engineer (Pr Eng) in terms of the Engineering Profession Act."
            : "Professional Review: No Pr Eng sign-off required for this category. It is still recommended to have these results reviewed by a qualified pressure equipment engineer."
        let notesTexts = [
            "Note: This categorization is based on SANS 347:2024 Edition 3.1 standards.",
            "Compliance: All pressure equipment must comply with the applicable conformity assessment procedures for the determined category.",
            prEngText,
        ]
        let notesBodyHeight = measureWrappedTextsHeight(notesTexts, maxWidth, bodySize, compactLine)
        var notesHeight = headingBlock + notesBodyHeight + cardPadding

        let hasConformity = result.category != "SEP" && result.category != "Not regulated"
        let conformityTexts = hasConformity
            ? ["Without QMS: \(conformity.withoutQuality)", "With QMS: \(conformity.withQuality)"]
            : []
        let conformityBodyHeight = hasConformity ? measureWrappedTextsHeight(conformityTexts, maxWidth, bodySize, compactLine) : 0
        var conformityHeight = hasConformity ? headingBlock + conformityBodyHeight + cardPadding : 0

        let footerBudget = pageHeight - margin - top - 28
        let gap: CGFloat = 6
        let totalFooter = notesHeight + (hasConformity ? gap + conformityHeight : 0)
        if totalFooter > footerBudget {
            if hasConformity {
                let available = footerBudget - gap
                let notesCap = min(notesHeight, available * 0.55)
                let confCap = available - notesCap
                notesHeight = max(headingBlock + cardPadding, notesCap)
                conformityHeight = max(headingBlock + cardPadding, confCap)
            } else {
                notesHeight = min(notesHeight, footerBudget)
            }
        }

        drawRoundedCard(ctx, margin, y, cardWidth, notesHeight, warningBg, warningBorder)
        _ = drawTextLine(ctx, textX, y + cardPadding + 9, "Important Notes", 10, axisTitle, bold: true)
        var textY = y + cardPadding + 22
        for note in notesTexts {
            textY = drawWrappedText(ctx, textX, textY, note, maxWidth, bodySize, compactLine, gray700)
        }
        y += notesHeight + gap

        if hasConformity {
            drawRoundedCard(ctx, margin, y, cardWidth, conformityHeight, cardFill, cardBorder)
            _ = drawTextLine(ctx, textX, y + cardPadding + 9, "Required Conformity Assessment Modules", 10, axisTitle, bold: true)
            textY = y + cardPadding + 22
            for line in conformityTexts {
                textY = drawWrappedText(ctx, textX, textY, line, maxWidth, bodySize, compactLine, gray700)
            }
            y += conformityHeight + gap
        }

        return y
    }

    // MARK: - Primitives

    private static func drawRoundedCard(_ ctx: CGContext, _ left: CGFloat, _ top: CGFloat, _ width: CGFloat, _ height: CGFloat, _ fillHex: String, _ borderHex: String) {
        let rect = CGRect(x: left, y: top, width: width, height: height)
        let path = UIBezierPath(roundedRect: rect, cornerRadius: cardRadius)
        ctx.setFillColor(UIColor(hex: fillHex).cgColor)
        ctx.addPath(path.cgPath)
        ctx.fillPath()
        ctx.setStrokeColor(UIColor(hex: borderHex).cgColor)
        ctx.setLineWidth(1)
        ctx.addPath(path.cgPath)
        ctx.strokePath()
    }

    private static func drawTitle(_ ctx: CGContext, _ y: CGFloat, _ text: String) -> CGFloat {
        _ = drawTextLine(ctx, margin, y + 16, text, 16, axisTitle, bold: true)
        return y + 22
    }

    @discardableResult
    private static func drawTextLine(_ ctx: CGContext, _ x: CGFloat, _ baselineY: CGFloat, _ text: String, _ size: CGFloat, _ colorHex: String, bold: Bool = false) -> CGFloat {
        drawBaseline(text, x: x, baselineY: baselineY, font: font(size, bold: bold), color: UIColor(hex: colorHex), align: .left)
        return baselineY + compactLine
    }

    private static func drawLabelValueRow(_ ctx: CGContext, _ x: CGFloat, _ baselineY: CGFloat, _ maxWidth: CGFloat, _ label: String, _ value: String) -> CGFloat {
        let labelFont = font(9, bold: false)
        let valueFont = font(9, bold: true)
        drawBaseline(label, x: x, baselineY: baselineY, font: labelFont, color: UIColor(hex: gray500), align: .left)
        let labelWidth = (label as NSString).size(withAttributes: [.font: labelFont]).width
        let valueX = x + labelWidth + 4
        let available = maxWidth - labelWidth - 4
        let display = truncate(value, valueFont, available)
        drawBaseline(display, x: valueX, baselineY: baselineY, font: valueFont, color: UIColor(hex: primaryCyan), align: .left)
        return baselineY + compactLine
    }

    private static func truncate(_ text: String, _ font: UIFont, _ maxWidth: CGFloat) -> String {
        if (text as NSString).size(withAttributes: [.font: font]).width <= maxWidth { return text }
        var t = text
        while t.count > 1 && ("\(t)…" as NSString).size(withAttributes: [.font: font]).width > maxWidth {
            t.removeLast()
        }
        return "\(t)…"
    }

    @discardableResult
    private static func drawWrappedText(_ ctx: CGContext, _ x: CGFloat, _ y: CGFloat, _ text: String, _ maxWidth: CGFloat, _ size: CGFloat, _ lineHeight: CGFloat, _ colorHex: String) -> CGFloat {
        let f = font(size, bold: false)
        let color = UIColor(hex: colorHex)
        let words = text.split(separator: " ").map(String.init)
        var line = ""
        var currentY = y
        for word in words {
            let test = line.isEmpty ? word : "\(line) \(word)"
            if (test as NSString).size(withAttributes: [.font: f]).width > maxWidth && !line.isEmpty {
                drawBaseline(line, x: x, baselineY: currentY, font: f, color: color, align: .left)
                currentY += lineHeight
                line = word
            } else {
                line = test
            }
        }
        if !line.isEmpty {
            drawBaseline(line, x: x, baselineY: currentY, font: f, color: color, align: .left)
            currentY += lineHeight
        }
        return currentY
    }

    private static func measureWrappedTextHeight(_ text: String, _ maxWidth: CGFloat, _ size: CGFloat, _ lineHeight: CGFloat) -> CGFloat {
        let f = font(size, bold: false)
        let words = text.split(separator: " ").map(String.init)
        var line = ""
        var lines = 0
        for word in words {
            let test = line.isEmpty ? word : "\(line) \(word)"
            if (test as NSString).size(withAttributes: [.font: f]).width > maxWidth && !line.isEmpty {
                lines += 1
                line = word
            } else {
                line = test
            }
        }
        if !line.isEmpty { lines += 1 }
        return CGFloat(lines) * lineHeight
    }

    private static func measureWrappedTextsHeight(_ texts: [String], _ maxWidth: CGFloat, _ size: CGFloat, _ lineHeight: CGFloat) -> CGFloat {
        texts.reduce(0) { $0 + measureWrappedTextHeight($1, maxWidth, size, lineHeight) }
    }

    private static func font(_ size: CGFloat, bold: Bool) -> UIFont {
        bold ? .boldSystemFont(ofSize: size) : .systemFont(ofSize: size)
    }

    private static func drawBaseline(_ s: String, x: CGFloat, baselineY: CGFloat, font: UIFont, color: UIColor, align: NSTextAlignment) {
        let attrs: [NSAttributedString.Key: Any] = [.font: font, .foregroundColor: color]
        let textSize = (s as NSString).size(withAttributes: attrs)
        var drawX = x
        if align == .right { drawX = x - textSize.width }
        else if align == .center { drawX = x - textSize.width / 2 }
        (s as NSString).draw(at: CGPoint(x: drawX, y: baselineY - font.ascender), withAttributes: attrs)
    }
}
