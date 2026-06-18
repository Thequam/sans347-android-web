import SwiftUI

/// App shell, mirroring `MainActivity.kt`. Renders the active page by an
/// integer switch, with a collapsible header and fixed bottom nav. In
/// landscape on the Graphs/Results pages the chrome is hidden for an
/// immersive graph view (matching Android's `graphImmersive`).
struct RootView: View {
    @EnvironmentObject private var state: AppState
    @Environment(\.verticalSizeClass) private var verticalSizeClass

    private var landscape: Bool { verticalSizeClass == .compact }
    private var graphImmersive: Bool { landscape && (state.currentPage == 2 || state.currentPage == 3) }

    var body: some View {
        VStack(spacing: 0) {
            if !graphImmersive {
                CollapsibleHeader(
                    showHeader: state.showHeader,
                    onToggleHeader: { state.toggleHeader() },
                    onTables: { state.setCurrentPage(0) },
                    onHomeTitle: { state.setCurrentPage(1) },
                    onGraphs: { state.setCurrentPage(2) }
                )
            }

            ZStack {
                switch state.currentPage {
                case 0:
                    TablesScreen()
                case 2:
                    GraphsScreen(immersiveGraph: graphImmersive)
                case 3:
                    ResultsScreen(immersiveGraph: graphImmersive)
                default:
                    HomeScreen()
                }
            }
            .frame(maxWidth: .infinity, maxHeight: .infinity)

            if !graphImmersive {
                BottomNavBar(
                    currentPage: state.currentPage,
                    onSelect: { state.setCurrentPage($0) }
                )
            }
        }
    }
}
