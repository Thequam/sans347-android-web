import SwiftUI

/// Dark collapsible top header, mirroring `ui/components/AppChrome.kt`
/// `CollapsibleHeader`. Tables (left) / SANS 347 title (center -> Home) /
/// Graphs (right), with a chevron row that toggles the header.
struct CollapsibleHeader: View {
    let showHeader: Bool
    let onToggleHeader: () -> Void
    let onTables: () -> Void
    let onHomeTitle: () -> Void
    let onGraphs: () -> Void

    var body: some View {
        VStack(spacing: 0) {
            if showHeader {
                HStack {
                    Button(action: onTables) {
                        HStack(spacing: 4) {
                            Image(systemName: "doc.text")
                                .font(.system(size: 16))
                            Text("Tables").font(.system(size: 14))
                        }
                        .foregroundColor(SansColors.gray300)
                        .padding(.horizontal, 8).padding(.vertical, 6)
                    }
                    .buttonStyle(.plain)

                    Button(action: onHomeTitle) {
                        VStack(spacing: 0) {
                            Text("SANS 347")
                                .font(.system(size: 18, weight: .bold))
                                .foregroundColor(.white)
                            Text("2024 3rd Edition")
                                .font(.system(size: 12, weight: .medium))
                                .foregroundColor(SansColors.primaryCyan)
                        }
                        .frame(maxWidth: .infinity)
                    }
                    .buttonStyle(.plain)

                    Button(action: onGraphs) {
                        HStack(spacing: 4) {
                            Text("Graphs").font(.system(size: 14))
                            Image(systemName: "chart.bar")
                                .font(.system(size: 16))
                        }
                        .foregroundColor(SansColors.gray300)
                        .padding(.horizontal, 8).padding(.vertical, 6)
                    }
                    .buttonStyle(.plain)
                }
                .padding(.horizontal, 8)
                .padding(.vertical, 8)
            }

            Button(action: onToggleHeader) {
                Image(systemName: showHeader ? "chevron.up" : "chevron.down")
                    .font(.system(size: 12))
                    .foregroundColor(SansColors.gray500)
                    .frame(maxWidth: .infinity)
                    .padding(.vertical, 2)
            }
            .buttonStyle(.plain)
        }
        .frame(maxWidth: .infinity)
        .background(SansColors.darkBackground.ignoresSafeArea(edges: .top))
        .animation(.easeInOut(duration: 0.3), value: showHeader)
    }
}

/// Fixed bottom navigation bar, mirroring `BottomNavBar` in `AppChrome.kt`.
/// Order: Home (1), Results (3), Graphs (2), Tables (0).
struct BottomNavBar: View {
    let currentPage: Int
    let onSelect: (Int) -> Void

    var body: some View {
        VStack(spacing: 0) {
            Rectangle()
                .fill(SansColors.navBorder)
                .frame(height: 1)
            HStack {
                navItem(1, "Home", "house")
                navItem(3, "Results", "list.bullet.rectangle")
                navItem(2, "Graphs", "chart.bar")
                navItem(0, "Tables", "doc.text")
            }
            .frame(maxWidth: .infinity)
            .padding(.vertical, 6)
        }
        .frame(maxWidth: .infinity)
        .background(SansColors.darkBackground.ignoresSafeArea(edges: .bottom))
    }

    private func navItem(_ page: Int, _ label: String, _ symbol: String) -> some View {
        let active = currentPage == page
        let color = active ? SansColors.primaryCyan : SansColors.gray500
        return Button {
            onSelect(page)
        } label: {
            VStack(spacing: 2) {
                Image(systemName: symbol)
                    .font(.system(size: 20))
                Text(label)
                    .font(.system(size: 10, weight: .medium))
            }
            .foregroundColor(color)
            .frame(maxWidth: .infinity)
            .padding(.horizontal, 8)
            .padding(.vertical, 4)
        }
        .buttonStyle(.plain)
    }
}
