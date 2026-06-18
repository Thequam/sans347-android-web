import SwiftUI

/// Single source of truth, mirroring Android's `SansUiState` + `Sans347ViewModel`.
/// Page indices match the other platforms exactly:
/// 0 = Tables, 1 = Home/Input (default), 2 = Graphs, 3 = Results.
final class AppState: ObservableObject {
    @Published var currentPage: Int = 1
    @Published var showHeader: Bool = true
    @Published var equipmentType: String? = nil
    @Published var stateOfContents: String? = nil
    @Published var fluidGroup: String? = nil
    @Published var designPressure: String = ""
    @Published var volumeOrDiameter: String = ""
    @Published var currentGraphIndex: Int = 0
    @Published var result: ResultData? = nil

    func setCurrentPage(_ page: Int) {
        currentPage = page
    }

    func toggleHeader() {
        showHeader.toggle()
    }

    func setCurrentGraphIndex(_ index: Int) {
        currentGraphIndex = min(max(index, 0), 8)
    }

    /// Mirrors `Sans347ViewModel.clearAll()` — resets inputs and result but
    /// preserves the current page, header state and graph index.
    func clearAll() {
        equipmentType = nil
        stateOfContents = nil
        fluidGroup = nil
        designPressure = ""
        volumeOrDiameter = ""
        result = nil
    }

    /// Parses a numeric field, tolerating the SA/EU decimal comma (e.g. "1,5").
    private func parseNumber(_ s: String) -> Double? {
        Double(s.replacingOccurrences(of: ",", with: "."))
    }

    /// Mirrors `Sans347ViewModel.calculateAndGoToResults()`.
    func calculateAndGoToResults() {
        guard let eq = equipmentType else { return }
        guard let ps = parseNumber(designPressure), let v = parseNumber(volumeOrDiameter) else { return }
        if ps <= 0 || v <= 0 { return }

        let figureId: Int
        let st: String
        let fg: String
        if eq == "Steam Generator" {
            figureId = 5
            st = ""
            fg = ""
        } else {
            guard let s = stateOfContents else { return }
            guard let f = fluidGroup else { return }
            st = s
            fg = f
            figureId = selectFigure(equipmentType: eq, stateOfContents: st, fluidGroup: fg)
        }

        let category = determineCategory(figureId: figureId, ps: ps, vOrDn: v)
        let product = ps * v
        result = ResultData(
            category: category,
            figureId: figureId,
            product: product,
            ps: ps,
            vOrDn: v,
            equipmentType: eq,
            stateOfContents: st,
            fluidGroup: fg
        )
        currentPage = 3
    }

    /// Mirrors `SansUiState.formValid()`.
    var formValid: Bool {
        let ps = parseNumber(designPressure)
        let v = parseNumber(volumeOrDiameter)
        if equipmentType == "Steam Generator" {
            guard let ps, let v else { return false }
            return ps > 0 && v > 0
        }
        guard equipmentType != nil, stateOfContents != nil, fluidGroup != nil,
              let ps, let v else { return false }
        return ps > 0 && v > 0
    }
}
