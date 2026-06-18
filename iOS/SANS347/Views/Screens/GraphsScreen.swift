import SwiftUI

/// Graphs page, mirroring `GraphsScreen.kt` — browse all 9 figures.
struct GraphsScreen: View {
    @EnvironmentObject private var state: AppState
    var immersiveGraph: Bool = false

    private var currentIndex: Int { min(max(state.currentGraphIndex, 0), 8) }
    private var graph: GraphConfig { allGraphs[currentIndex] }

    var body: some View {
        if immersiveGraph {
            immersiveLayout
        } else {
            portraitLayout
        }
    }

    private var portraitLayout: some View {
        ScrollView {
            VStack(spacing: 16) {
                headerCard
                SansCard(padding: 8) {
                    ZoomableGraphBox {
                        Sans347GraphView(config: graph)
                            .aspectRatio(GRAPH_ASPECT_RATIO, contentMode: .fit)
                    }
                }
                applicationCard
                Text(graph.footerText)
                    .font(.system(size: 14))
                    .foregroundColor(SansColors.gray400)
                    .multilineTextAlignment(.center)
                    .frame(maxWidth: .infinity)
                thumbnailRow
                legendCard
            }
            .maxContentWidth(896)
            .padding(.horizontal, 16)
            .padding(.bottom, 100)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(SansColors.white)
    }

    /// Landscape immersive layout (chrome hidden) — graph fills, controls in a
    /// compact sidebar. Mirrors `GraphsImmersiveLayout` in `GraphsScreen.kt`.
    private var immersiveLayout: some View {
        HStack(spacing: 8) {
            VStack(alignment: .leading, spacing: 8) {
                Text(graph.title).font(.system(size: 14, weight: .bold)).foregroundColor(.black)
                if !graph.subtitle.isEmpty {
                    Text(graph.subtitle).font(.system(size: 12, weight: .medium)).foregroundColor(SansColors.primaryCyan)
                }
                Text("Figure \(graph.id) · \(currentIndex + 1)/9").font(.system(size: 12)).foregroundColor(SansColors.gray500)
                HStack(spacing: 6) {
                    navButton(systemName: "chevron.left", label: "Prev", enabled: currentIndex > 0) {
                        state.setCurrentGraphIndex(currentIndex - 1)
                    }
                    navButton(label: "Next", systemName: "chevron.right", enabled: currentIndex < 8) {
                        state.setCurrentGraphIndex(currentIndex + 1)
                    }
                }
                ScrollView {
                    Text(graph.applicationText)
                        .font(.system(size: 11)).foregroundColor(SansColors.gray700)
                }
                Spacer(minLength: 0)
            }
            .frame(width: 200)

            ZoomableGraphBox {
                Sans347GraphView(config: graph)
                    .aspectRatio(GRAPH_ASPECT_RATIO, contentMode: .fit)
                    .frame(maxWidth: .infinity, maxHeight: .infinity)
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)
        }
        .padding(8)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(SansColors.white)
    }

    private var headerCard: some View {
        VStack(spacing: 0) {
            Text("\(currentIndex + 1) of 9")
                .font(.system(size: 14)).foregroundColor(SansColors.gray400)
                .padding(.horizontal, 8).padding(.vertical, 4)
                .background(SansColors.gray200)
                .clipShape(Capsule())
                .frame(maxWidth: .infinity, alignment: .trailing)
            Text(graph.title).font(.system(size: 18, weight: .bold)).foregroundColor(.black)
                .multilineTextAlignment(.center)
            if !graph.subtitle.isEmpty {
                Text(graph.subtitle).font(.system(size: 14, weight: .medium)).foregroundColor(SansColors.primaryCyan)
            }
            HStack {
                Text("Figure \(graph.id)").font(.system(size: 14)).foregroundColor(SansColors.gray500)
                Spacer()
                HStack(spacing: 6) {
                    navButton(systemName: "chevron.left", label: "Prev", enabled: currentIndex > 0) {
                        state.setCurrentGraphIndex(currentIndex - 1)
                    }
                    navButton(label: "Next", systemName: "chevron.right", enabled: currentIndex < 8) {
                        state.setCurrentGraphIndex(currentIndex + 1)
                    }
                }
            }
            .padding(.top, 8)
        }
        .padding(16)
        .frame(maxWidth: .infinity)
        .background(SansColors.gray50)
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(SansColors.gray200, lineWidth: 1))
    }

    private func navButton(systemName: String? = nil, label: String, enabled: Bool, action: @escaping () -> Void) -> some View {
        navButton(label: label, leadingSystemName: systemName, trailingSystemName: nil, enabled: enabled, action: action)
    }

    private func navButton(label: String, systemName: String, enabled: Bool, action: @escaping () -> Void) -> some View {
        navButton(label: label, leadingSystemName: nil, trailingSystemName: systemName, enabled: enabled, action: action)
    }

    private func navButton(label: String, leadingSystemName: String?, trailingSystemName: String?, enabled: Bool, action: @escaping () -> Void) -> some View {
        Button(action: action) {
            HStack(spacing: 4) {
                if let leadingSystemName { Image(systemName: leadingSystemName) }
                Text(label).font(.system(size: 12))
                if let trailingSystemName { Image(systemName: trailingSystemName) }
            }
            .foregroundColor(SansColors.gray700)
            .padding(.horizontal, 8).padding(.vertical, 4)
            .overlay(RoundedRectangle(cornerRadius: 8).stroke(SansColors.gray300, lineWidth: 1))
        }
        .buttonStyle(.plain)
        .opacity(enabled ? 1 : 0.3)
        .disabled(!enabled)
    }

    private var applicationCard: some View {
        Text(graph.applicationText)
            .font(.system(size: 14)).foregroundColor(SansColors.gray700)
            .multilineTextAlignment(.center)
            .frame(maxWidth: .infinity)
            .padding(16)
            .background(SansColors.white)
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .overlay(RoundedRectangle(cornerRadius: 12).stroke(SansColors.gray200, lineWidth: 1))
    }

    private var thumbnailRow: some View {
        HStack(spacing: 4) {
            ForEach(Array(allGraphs.enumerated()), id: \.element.id) { index, g in
                thumbnail(g, selected: index == currentIndex) {
                    state.setCurrentGraphIndex(index)
                }
            }
        }
    }

    private func thumbnail(_ g: GraphConfig, selected: Bool, action: @escaping () -> Void) -> some View {
        let abbr: String
        switch g.equipmentType {
        case "Pressure Vessels": abbr = "Vessels"
        case "Steam Generator": abbr = "Steam Gen"
        default: abbr = "Piping"
        }
        return Button(action: action) {
            VStack(spacing: 2) {
                Text("Fig \(g.id)")
                    .font(.system(size: 9, weight: .bold))
                    .foregroundColor(selected ? SansColors.primaryCyan : SansColors.gray700)
                Text(abbr).font(.system(size: 7)).foregroundColor(SansColors.gray400).lineLimit(1)
            }
            .frame(maxWidth: .infinity)
            .padding(.horizontal, 4).padding(.vertical, 6)
            .background(selected ? SansColors.thumbSelectedBg : SansColors.white)
            .clipShape(RoundedRectangle(cornerRadius: 8))
            .overlay(RoundedRectangle(cornerRadius: 8).stroke(selected ? SansColors.primaryCyan : SansColors.gray200, lineWidth: 2))
        }
        .buttonStyle(.plain)
    }

    private var legendCard: some View {
        SansCard {
            VStack(spacing: 12) {
                Text("Category Legend").font(.system(size: 16, weight: .bold)).foregroundColor(.black)
                HStack(spacing: 8) {
                    legendItem("SEP", SansColors.success, "Sound Engineering Practice")
                    legendItem("I", SansColors.categoryI, "Category I")
                    legendItem("II", SansColors.categoryII, "Category II")
                    legendItem("III", SansColors.categoryIII, "Category III")
                    legendItem("IV", SansColors.categoryIV, "Category IV")
                }
            }
            .frame(maxWidth: .infinity)
        }
    }

    private func legendItem(_ label: String, _ color: Color, _ desc: String) -> some View {
        VStack(spacing: 4) {
            Text(label)
                .font(.system(size: 14, weight: .bold)).foregroundColor(.white)
                .frame(maxWidth: .infinity).padding(.vertical, 8)
                .background(color).clipShape(RoundedRectangle(cornerRadius: 8))
            Text(desc).font(.system(size: 10)).foregroundColor(SansColors.gray500).multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity)
    }
}
