import XCTest
@testable import SANS347

/// Parity tests for the safety-critical categorization logic. The expected
/// values mirror the rules in `Android/.../data/GraphData.kt` and the web
/// `lib/graphData.ts`. If you change `determineCategory` or `selectFigure`,
/// these must still pass (or be updated in lock-step with both other platforms).
final class CategorizationTests: XCTestCase {

    // MARK: - Table 1 (selectFigure)

    func testSelectFigureMatrix() {
        XCTAssertEqual(selectFigure(equipmentType: "Pressure Vessels", stateOfContents: "Gas", fluidGroup: "Dangerous"), 1)
        XCTAssertEqual(selectFigure(equipmentType: "Pressure Vessels", stateOfContents: "Gas", fluidGroup: "Non-Dangerous"), 2)
        XCTAssertEqual(selectFigure(equipmentType: "Pressure Vessels", stateOfContents: "Liquid", fluidGroup: "Dangerous"), 3)
        XCTAssertEqual(selectFigure(equipmentType: "Pressure Vessels", stateOfContents: "Liquid", fluidGroup: "Non-Dangerous"), 4)
        XCTAssertEqual(selectFigure(equipmentType: "Piping", stateOfContents: "Gas", fluidGroup: "Dangerous"), 6)
        XCTAssertEqual(selectFigure(equipmentType: "Piping", stateOfContents: "Gas", fluidGroup: "Non-Dangerous"), 7)
        XCTAssertEqual(selectFigure(equipmentType: "Piping", stateOfContents: "Liquid", fluidGroup: "Dangerous"), 8)
        XCTAssertEqual(selectFigure(equipmentType: "Piping", stateOfContents: "Liquid", fluidGroup: "Non-Dangerous"), 9)
    }

    // MARK: - Not regulated floor (PS < 50) for every figure

    func testPressureFloor() {
        for id in 1...9 {
            XCTAssertEqual(determineCategory(figureId: id, ps: 49, vOrDn: 100), "Not regulated", "figure \(id)")
        }
    }

    func testInvalidFigure() {
        XCTAssertEqual(determineCategory(figureId: 42, ps: 100, vOrDn: 100), "Not regulated")
    }

    // MARK: - Per-figure boundary vectors

    func testBoundaryVectors() {
        // (figureId, ps, vOrDn, expected)
        let cases: [(Int, Double, Double, String)] = [
            // Figure 1 — vessels, dangerous gas
            (1, 100, 0.5, "SEP"),
            (1, 25000, 0.5, "SEP"),
            (1, 150000, 0.5, "IV"),
            (1, 100, 10, "SEP"),     // product 1000 <= 2000
            (1, 100, 30, "I"),       // product 3000
            (1, 100, 100, "II"),     // product 10000
            (1, 100, 300, "III"),    // product 30000
            (1, 100, 2000, "IV"),    // product 200000

            // Figure 2 — vessels, non-dangerous gas
            (2, 500, 0.5, "SEP"),
            (2, 150000, 0.5, "SEP"),
            (2, 350000, 0.5, "IV"),
            (2, 100, 10, "SEP"),     // product 1000
            (2, 400, 10, "SEP"),     // product 4000
            (2, 100, 100, "I"),      // product 10000
            (2, 100, 300, "II"),     // product 30000
            (2, 100, 2000, "III"),   // product 200000
            (2, 100, 4000, "IV"),    // product 400000

            // Figure 3 — vessels, dangerous liquids
            (3, 100, 0.5, "SEP"),    // v < 1
            (3, 100, 10, "SEP"),     // product 1000
            (3, 500, 100, "I"),      // product 50000, ps in [50,1000)
            (3, 2000, 10, "II"),
            (3, 60000, 10, "III"),

            // Figure 4 — vessels, non-dangerous liquids
            (4, 100, 5, "SEP"),      // v < 10
            (4, 100, 100, "SEP"),    // product 10000
            (4, 500, 5000, "I"),     // product 2.5e6
            (4, 2000, 20, "II"),
            (4, 60000, 20, "III"),

            // Figure 5 — steam generators
            (5, 100, 1, "SEP"),      // v < 2
            (5, 100, 20, "SEP"),     // product 2000
            (5, 100, 100, "I"),      // product 10000
            (5, 100, 300, "II"),     // product 30000
            (5, 4000, 10, "III"),    // product 40000, ps > 3200

            // Figure 6 — piping, dangerous gas
            (6, 100, 10, "SEP"),     // DN < 25
            (6, 100, 50, "I"),       // product 5000
            (6, 3000, 50, "II"),     // product 150000
            (6, 1000, 200, "II"),    // DN in [100,350), ps<3500, product 200000
            (6, 4000, 150, "III"),   // DN>=100, ps>=3500
            (6, 100, 400, "III"),    // DN>=350

            // Figure 7 — piping, non-dangerous gas
            (7, 100, 10, "SEP"),     // DN < 32
            (7, 100, 50, "SEP"),     // product 5000
            (7, 2000, 100, "I"),     // product 200000
            (7, 5000, 100, "II"),    // product 500000
            (7, 100, 300, "III"),    // DN >= 250

            // Figure 8 — piping, dangerous liquids
            (8, 100, 10, "SEP"),     // DN < 25
            (8, 60, 50, "SEP"),      // ps in [50,100)
            (8, 500, 50, "I"),       // product 25000
            (8, 2000, 50, "II"),
            (8, 60000, 50, "III"),

            // Figure 9 — piping, non-dangerous liquids
            (9, 100, 50, "SEP"),     // DN < 200
            (9, 100, 300, "SEP"),    // product 30000
            (9, 500, 2000, "I"),     // product 1e6
            (9, 2000, 300, "II"),
            (9, 60000, 300, "III"),
        ]

        for (id, ps, v, expected) in cases {
            XCTAssertEqual(
                determineCategory(figureId: id, ps: ps, vOrDn: v),
                expected,
                "figure \(id), ps=\(ps), v/dn=\(v)"
            )
        }
    }

    // MARK: - Helpers

    func testConformityModules() {
        XCTAssertEqual(getConformityModules("I").withoutQuality, "A")
        XCTAssertEqual(getConformityModules("II").withQuality, "A2 or D1 or E1")
        XCTAssertEqual(getConformityModules("III").withoutQuality, "B (design type) + F or B (production type) + C2")
        XCTAssertEqual(getConformityModules("IV").withQuality, "H1 or B (production type) + D")
        XCTAssertEqual(getConformityModules("SEP").withoutQuality, "N/A")
    }

    func testCategoryRiskAndColor() {
        XCTAssertEqual(getCategoryRisk("III"), "High Risk")
        XCTAssertEqual(getCategoryRisk("Not regulated"), "Below regulation threshold")
        XCTAssertEqual(getCategoryColorHex("IV"), "#ef4444")
        XCTAssertEqual(getCategoryColorHex("Anything"), "#6b7280")
    }

    func testPrEngRequirement() {
        XCTAssertFalse(requiresPrEng("I"))
        XCTAssertTrue(requiresPrEng("II"))
        XCTAssertTrue(requiresPrEng("III"))
        XCTAssertTrue(requiresPrEng("IV"))
    }

    // MARK: - Steam Generator bypass via AppState

    @MainActor
    func testSteamGeneratorBypass() {
        let state = AppState()
        state.equipmentType = "Steam Generator"
        state.designPressure = "100"
        state.volumeOrDiameter = "100"
        XCTAssertTrue(state.formValid)
        state.calculateAndGoToResults()
        XCTAssertEqual(state.result?.figureId, 5)
        XCTAssertEqual(state.result?.stateOfContents, "")
        XCTAssertEqual(state.result?.fluidGroup, "")
        XCTAssertEqual(state.currentPage, 3)
    }

    @MainActor
    func testFormValidationRequiresSelections() {
        let state = AppState()
        state.equipmentType = "Pressure Vessels"
        state.designPressure = "100"
        state.volumeOrDiameter = "10"
        XCTAssertFalse(state.formValid) // missing state + fluid group
        state.stateOfContents = "Gas"
        state.fluidGroup = "Dangerous"
        XCTAssertTrue(state.formValid)
    }
}
