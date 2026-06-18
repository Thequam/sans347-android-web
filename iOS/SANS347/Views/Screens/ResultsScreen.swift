import SwiftUI

/// Results page, mirroring `ResultsScreen.kt`.
struct ResultsScreen: View {
    @EnvironmentObject private var state: AppState
    var immersiveGraph: Bool = false

    @State private var showResultCard = true
    @State private var shareItems: [Any] = []
    @State private var showShareSheet = false

    var body: some View {
        if let result = state.result, let graph = graphById(result.figureId) {
            Group {
                if immersiveGraph {
                    immersiveContent(result: result, graph: graph)
                } else {
                    content(result: result, graph: graph)
                }
            }
            .sheet(isPresented: $showShareSheet) {
                ShareSheet(items: shareItems)
            }
        } else {
            emptyState
        }
    }

    /// Landscape immersive layout (chrome hidden) — graph fills, key result and
    /// export actions in a compact sidebar. Mirrors `ResultsImmersiveLayout`.
    private func immersiveContent(result: ResultData, graph: GraphConfig) -> some View {
        let category = result.category
        let catColor = Color(hex: catColorHex(category))
        let catRisk = getCategoryRisk(category)
        let conformity = getConformityModules(category)
        let isPiping = graph.xVariable == "DN"
        return HStack(spacing: 8) {
            VStack(alignment: .leading, spacing: 8) {
                Button {
                    state.setCurrentPage(1)
                } label: {
                    HStack(spacing: 4) {
                        Image(systemName: "arrow.left").font(.system(size: 12))
                        Text("Input").font(.system(size: 12))
                    }
                    .foregroundColor(SansColors.gray700)
                    .padding(.horizontal, 8).padding(.vertical, 4)
                    .overlay(RoundedRectangle(cornerRadius: 8).stroke(SansColors.gray300, lineWidth: 1))
                }
                .buttonStyle(.plain)

                HStack(spacing: 8) {
                    Text(category)
                        .font(.system(size: category.count <= 3 ? 18 : 11, weight: .bold))
                        .foregroundColor(.white)
                        .multilineTextAlignment(.center)
                        .frame(width: 44, height: 44)
                        .background(catColor)
                        .clipShape(Circle())
                    VStack(alignment: .leading, spacing: 2) {
                        Text(category == "Not regulated" ? "Not Regulated" : "Category \(category)")
                            .font(.system(size: 14, weight: .bold)).foregroundColor(catColor)
                        Text(catRisk).font(.system(size: 11)).foregroundColor(SansColors.gray500)
                    }
                }

                Text(productLabel(result, isPiping: isPiping))
                    .font(.system(size: 11)).foregroundColor(SansColors.gray700)

                exportButtons(result: result, graph: graph, catRisk: catRisk, conformity: conformity)

                Spacer(minLength: 0)
            }
            .frame(width: 220)

            ZoomableGraphBox {
                Sans347GraphView(config: graph, plotPoint: plotPoint(result))
                    .aspectRatio(GRAPH_ASPECT_RATIO, contentMode: .fit)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .padding(8)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(SansColors.white)
    }

    // MARK: - Derived

    private func catColorHex(_ category: String) -> String { getCategoryColorHex(category) }

    private func productLabel(_ result: ResultData, isPiping: Bool) -> String {
        if isPiping {
            return "PS×DN = \(fmtNum(result.ps)) × \(fmtNum(result.vOrDn)) = \(fmtNum(result.product)) kPa·DN"
        }
        return "PS×V = \(fmtNum(result.ps)) × \(fmtNum(result.vOrDn)) = \(fmtNum(result.product)) kPa·L"
    }

    private func plotPoint(_ result: ResultData) -> GraphPlotPoint {
        GraphPlotPoint(x: result.vOrDn, y: result.ps, colorHex: catColorHex(result.category))
    }

    // MARK: - Empty state

    private var emptyState: some View {
        VStack(spacing: 16) {
            Text("No calculation yet.")
                .font(.system(size: 16))
                .foregroundColor(SansColors.gray500)
            Button {
                state.setCurrentPage(1)
            } label: {
                Text("Go to Home")
                    .foregroundColor(.white)
                    .padding(.horizontal, 16).padding(.vertical, 10)
                    .background(SansColors.primaryCyan)
                    .clipShape(RoundedRectangle(cornerRadius: 8))
            }
            .buttonStyle(.plain)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(SansColors.white)
    }

    // MARK: - Main content

    private func content(result: ResultData, graph: GraphConfig) -> some View {
        let category = result.category
        let catColor = Color(hex: catColorHex(category))
        let catRisk = getCategoryRisk(category)
        let conformity = getConformityModules(category)
        let isPiping = graph.xVariable == "DN"

        return ScrollView {
            VStack(spacing: 16) {
                headerRow
                categoryCard(result: result, catColor: catColor, catRisk: catRisk)
                graphSection(result: result, graph: graph, catColor: catColor, isPiping: isPiping)
                summaryCard(result: result, graph: graph, isPiping: isPiping)
                importantNotesCard(category: category)
                if category != "SEP" && category != "Not regulated" {
                    conformityCard(conformity)
                }
                newCalculationButton
                exportButtons(result: result, graph: graph, catRisk: catRisk, conformity: conformity)
            }
            .maxContentWidth(896)
            .padding(.horizontal, 16)
            .padding(.top, 16)
            .padding(.bottom, 100)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(SansColors.white)
    }

    private var headerRow: some View {
        HStack {
            HStack(spacing: 8) {
                Image(systemName: "checkmark.circle.fill")
                    .font(.system(size: 24))
                    .foregroundColor(SansColors.success)
                Text("Calculation Results").font(.system(size: 18, weight: .bold)).foregroundColor(.black)
            }
            Spacer()
            Button {
                state.setCurrentPage(1)
            } label: {
                HStack(spacing: 4) {
                    Image(systemName: "arrow.left").font(.system(size: 14))
                    Text("Back to Input").font(.system(size: 14))
                }
                .foregroundColor(SansColors.gray700)
                .padding(.horizontal, 12).padding(.vertical, 6)
                .overlay(RoundedRectangle(cornerRadius: 8).stroke(SansColors.gray300, lineWidth: 1))
            }
            .buttonStyle(.plain)
        }
        .padding(16)
        .frame(maxWidth: .infinity)
        .background(SansColors.gray50)
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(SansColors.gray200, lineWidth: 1))
    }

    private func categoryCard(result: ResultData, catColor: Color, catRisk: String) -> some View {
        let category = result.category
        let circleTextSize: CGFloat = category.count <= 3 ? 48 : (category.count <= 5 ? 24 : 14)
        return SansCard(padding: 24) {
            VStack(spacing: 0) {
                Text("Your Equipment Category")
                    .font(.system(size: 20, weight: .bold)).foregroundColor(.black)
                    .padding(.bottom, 16)
                Text(category)
                    .font(.system(size: circleTextSize, weight: .bold))
                    .foregroundColor(.white)
                    .multilineTextAlignment(.center)
                    .padding(4)
                    .frame(width: 128, height: 128)
                    .background(catColor)
                    .clipShape(Circle())
                    .padding(.bottom, 12)
                Text(category == "Not regulated" ? "Not Regulated" : "Category \(category)")
                    .font(.system(size: 20, weight: .bold)).foregroundColor(catColor)
                Text(catRisk).foregroundColor(SansColors.gray500).padding(.top, 4)
            }
            .frame(maxWidth: .infinity)
        }
    }

    private func graphSection(result: ResultData, graph: GraphConfig, catColor: Color, isPiping: Bool) -> some View {
        SansCard {
            VStack(alignment: .leading, spacing: 0) {
                HStack(spacing: 8) {
                    Image(systemName: "chart.bar").font(.system(size: 18)).foregroundColor(SansColors.gray500)
                    Text("Applicable Categorization Graph").font(.system(size: 16, weight: .bold)).foregroundColor(.black)
                }
                .padding(.bottom, 12)
                Divider().background(SansColors.gray200)
                Text("\(graph.title) — \(graph.subtitle)")
                    .font(.system(size: 18, weight: .bold)).foregroundColor(.black)
                    .padding(.top, 12)
                Text("Figure \(graph.id)").font(.system(size: 14)).foregroundColor(SansColors.primaryCyan)

                ZStack(alignment: .topTrailing) {
                    ZoomableGraphBox {
                        Sans347GraphView(config: graph, plotPoint: plotPoint(result))
                            .aspectRatio(GRAPH_ASPECT_RATIO, contentMode: .fit)
                    }
                    resultOverlay(result: result, catColor: catColor, isPiping: isPiping)
                        .padding(4)
                }
                .padding(.top, 8)

                applicationNote(graph.applicationText)
                    .padding(.top, 12)
            }
        }
    }

    private func resultOverlay(result: ResultData, catColor: Color, isPiping: Bool) -> some View {
        Button {
            showResultCard.toggle()
        } label: {
            Group {
                if showResultCard {
                    VStack(alignment: .leading, spacing: 2) {
                        HStack(spacing: 4) {
                            Circle().fill(catColor).frame(width: 8, height: 8)
                            Text("Your Result").font(.system(size: 10, weight: .bold)).foregroundColor(.black)
                        }
                        Text(productLabel(result, isPiping: isPiping))
                            .font(.system(size: 9)).foregroundColor(SansColors.gray700)
                    }
                } else {
                    HStack(spacing: 4) {
                        Text(result.category)
                            .font(.system(size: 6, weight: .bold)).foregroundColor(.white)
                            .frame(width: 14, height: 14).background(catColor).clipShape(Circle())
                        Text("Result").font(.system(size: 9, weight: .bold)).foregroundColor(SansColors.gray600)
                    }
                }
            }
            .padding(.horizontal, 8).padding(.vertical, 6)
            .background(SansColors.white)
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .overlay(RoundedRectangle(cornerRadius: 8).stroke(SansColors.primaryCyan, lineWidth: 1))
        }
        .buttonStyle(.plain)
    }

    private func applicationNote(_ text: String) -> some View {
        (Text("Application: ").font(.system(size: 14, weight: .bold)).foregroundColor(SansColors.applicationLabel)
            + Text(text).font(.system(size: 14)).foregroundColor(SansColors.gray700))
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(12)
            .background(SansColors.applicationBg)
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .overlay(RoundedRectangle(cornerRadius: 8).stroke(SansColors.applicationBorder, lineWidth: 1))
    }

    private func summaryCard(result: ResultData, graph: GraphConfig, isPiping: Bool) -> some View {
        let equipLabel = graph.equipmentType == "Pressure Vessels" ? "Vessels" : graph.equipmentType
        let isSteamGen = result.equipmentType == "Steam Generator"
        return SansCard {
            VStack(spacing: 0) {
                Text("Input Parameters").font(.system(size: 16, weight: .bold)).foregroundColor(.black)
                    .frame(maxWidth: .infinity, alignment: .leading).padding(.bottom, 12)
                summaryRow("Equipment Type:", equipLabel, valueColor: .black)
                divider
                summaryRow("Design Pressure:", "\(fmtNum(result.ps)) kPa", valueColor: SansColors.primaryCyan)
                if !isSteamGen {
                    divider
                    summaryRow("State Contents:", result.stateOfContents, valueColor: .black)
                }
                divider
                summaryRow(isPiping ? "Diameter:" : "Volume:", "\(fmtNum(result.vOrDn)) \(isPiping ? "DN" : "L")", valueColor: SansColors.primaryCyan)
                if !isSteamGen {
                    divider
                    summaryRow("Fluid Group:", result.fluidGroup, valueColor: .black)
                }
            }
        }
    }

    private var divider: some View {
        Divider().background(SansColors.gray200).padding(.vertical, 8)
    }

    private func summaryRow(_ label: String, _ value: String, valueColor: Color) -> some View {
        HStack {
            Text(label).font(.system(size: 14)).foregroundColor(SansColors.gray500)
            Spacer()
            Text(value).font(.system(size: 14, weight: .medium)).foregroundColor(valueColor)
        }
        .frame(maxWidth: .infinity)
    }

    private func importantNotesCard(category: String) -> some View {
        let prEng = requiresPrEng(category)
        return VStack(spacing: 0) {
            HStack(spacing: 8) {
                Image(systemName: "exclamationmark.triangle").font(.system(size: 18)).foregroundColor(SansColors.warningIcon)
                Text("Important Notes").font(.system(size: 16, weight: .bold)).foregroundColor(.black)
                Spacer()
            }
            .padding(12)
            .background(SansColors.warningBg)
            .overlay(RoundedCorners(radius: 12, corners: [.topLeft, .topRight]).stroke(SansColors.warningBorder, lineWidth: 1))

            VStack(alignment: .leading, spacing: 8) {
                noteText(bold: "Note:", body: " This categorization is based on SANS 347:2024 Edition 3.1 standards.")
                noteText(bold: "Compliance:", body: " All pressure equipment must comply with the applicable conformity assessment procedures for the determined category.")
                professionalReview(category: category, prEng: prEng)
            }
            .padding(16)
            .frame(maxWidth: .infinity, alignment: .leading)
        }
        .background(SansColors.white)
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(SansColors.gray200, lineWidth: 1))
    }

    private func noteText(bold: String, body: String) -> some View {
        (Text(bold).font(.system(size: 14, weight: .bold)).foregroundColor(.black)
            + Text(body).font(.system(size: 14)).foregroundColor(SansColors.gray700))
            .frame(maxWidth: .infinity, alignment: .leading)
    }

    private func professionalReview(category: String, prEng: Bool) -> some View {
        var text = Text("Professional Review:").font(.system(size: 14, weight: .bold)).foregroundColor(.black)
        if prEng {
            text = text
                + Text(" ").font(.system(size: 14))
                + Text("Pr Eng sign-off required.").font(.system(size: 14, weight: .bold)).foregroundColor(SansColors.primaryCyan)
                + Text(" Category \(category) pressure equipment must be signed off by a Professional Registered Engineer (Pr Eng) in terms of the Engineering Profession Act.").font(.system(size: 14)).foregroundColor(SansColors.gray700)
        } else {
            text = text
                + Text(" No Pr Eng sign-off required for this category. It is still recommended to have these results reviewed by a qualified pressure equipment engineer.").font(.system(size: 14)).foregroundColor(SansColors.gray700)
        }
        return text.frame(maxWidth: .infinity, alignment: .leading)
    }

    private func conformityCard(_ conformity: ConformityModules) -> some View {
        SansCard {
            VStack(alignment: .leading, spacing: 0) {
                Text("Required Conformity Assessment Modules").font(.system(size: 16, weight: .bold)).foregroundColor(.black)
                    .padding(.bottom, 12)
                Text("Manufacturer without Certified Quality System").font(.system(size: 14, weight: .semibold)).foregroundColor(SansColors.gray700)
                moduleBox(conformity.withoutQuality)
                Text("Manufacturer with Certified Quality System").font(.system(size: 14, weight: .semibold)).foregroundColor(SansColors.gray700)
                    .padding(.top, 12)
                moduleBox(conformity.withQuality)
            }
        }
    }

    private func moduleBox(_ text: String) -> some View {
        Text(text)
            .font(.system(size: 14)).foregroundColor(SansColors.gray700)
            .frame(maxWidth: .infinity, alignment: .leading)
            .padding(12)
            .background(SansColors.gray50)
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .overlay(RoundedRectangle(cornerRadius: 8).stroke(SansColors.gray200, lineWidth: 1))
    }

    private var newCalculationButton: some View {
        Button {
            state.setCurrentPage(1)
        } label: {
            HStack(spacing: 8) {
                Image(systemName: "arrow.left").font(.system(size: 16))
                Text("New Calculation")
            }
            .foregroundColor(SansColors.gray700)
            .padding(.horizontal, 16).padding(.vertical, 10)
            .overlay(RoundedRectangle(cornerRadius: 8).stroke(SansColors.gray300, lineWidth: 1))
        }
        .buttonStyle(.plain)
        .padding(.top, 8)
    }

    private func exportButtons(result: ResultData, graph: GraphConfig, catRisk: String, conformity: ConformityModules) -> some View {
        VStack(spacing: 8) {
            Button {
                if let url = GraphImageExporter.exportToTempFile(config: graph, plotPoint: plotPoint(result)) {
                    shareItems = [url]
                    showShareSheet = true
                }
            } label: {
                HStack(spacing: 8) {
                    Image(systemName: "chart.bar").font(.system(size: 16))
                    Text("Export graph (PNG)").font(.system(size: 14))
                }
                .foregroundColor(.white)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 10)
                .background(SansColors.primaryCyan)
                .clipShape(RoundedRectangle(cornerRadius: 8))
            }
            .buttonStyle(.plain)

            Button {
                if let url = ReportPDFExporter.exportToTempFile(result: result, graph: graph, catRisk: catRisk, conformity: conformity, plotPoint: plotPoint(result)) {
                    shareItems = [url]
                    showShareSheet = true
                }
            } label: {
                HStack(spacing: 8) {
                    Image(systemName: "doc.text").font(.system(size: 16))
                    Text("Export report (PDF)").font(.system(size: 14))
                }
                .foregroundColor(SansColors.gray700)
                .frame(maxWidth: .infinity)
                .padding(.vertical, 10)
                .overlay(RoundedRectangle(cornerRadius: 8).stroke(SansColors.gray300, lineWidth: 1))
            }
            .buttonStyle(.plain)
        }
        .frame(maxWidth: .infinity)
    }
}
