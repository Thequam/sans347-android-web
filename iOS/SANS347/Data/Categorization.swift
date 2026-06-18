import Foundation

// Safety-critical categorization logic, ported verbatim from
// `Android/.../data/GraphData.kt` (lines 306-464). The Kotlin and TS
// implementations are logically identical. Evaluation is FIRST-MATCH-WINS:
// the order of the conditions matters and must be preserved exactly.

/// Table 1 mapping of inputs to a figure id. NOTE: Steam Generator is handled
/// before this is ever called (it always maps to figure 5) — see `AppState`.
func selectFigure(
    equipmentType: String,
    stateOfContents: String,
    fluidGroup: String
) -> Int {
    let dangerous = fluidGroup == "Dangerous"
    if equipmentType == "Pressure Vessels" {
        if stateOfContents == "Gas" {
            return dangerous ? 1 : 2
        } else {
            return dangerous ? 3 : 4
        }
    } else {
        if stateOfContents == "Gas" {
            return dangerous ? 6 : 7
        } else {
            return dangerous ? 8 : 9
        }
    }
}

/// Hand-coded threshold rules per figure. `product = ps * vOrDn`.
func determineCategory(
    figureId: Int,
    ps: Double,
    vOrDn: Double
) -> String {
    let product = ps * vOrDn
    switch figureId {
    case 1:
        if ps < 50 { return "Not regulated" }
        if vOrDn < 1 && ps < 20000 { return "SEP" }
        if vOrDn < 1 && ps >= 20000 && ps < 100000 { return "SEP" }
        if vOrDn < 1 && ps >= 100000 { return "IV" }
        if product <= 2000 && ps >= 50 { return "SEP" }
        if product > 2000 && product <= 5000 && ps >= 50 { return "I" }
        if product > 5000 && product <= 20000 && ps >= 50 { return "II" }
        if product > 20000 && product <= 100000 && ps >= 50 { return "III" }
        if product > 100000 { return "IV" }
        return "SEP"
    case 2:
        if ps < 50 { return "Not regulated" }
        if vOrDn < 1 && ps < 100000 { return "SEP" }
        if vOrDn < 1 && ps >= 100000 && ps < 300000 { return "SEP" }
        if vOrDn < 1 && ps >= 300000 { return "IV" }
        if product <= 5000 && ps >= 50 && ps < 400 { return "SEP" }
        if product <= 5000 && ps >= 400 { return "SEP" }
        if product > 5000 && product <= 20000 && ps >= 50 { return "I" }
        if product > 20000 && product <= 100000 && ps >= 50 { return "II" }
        if product > 100000 && product <= 300000 && ps >= 50 { return "III" }
        if product > 300000 || (ps > 400 && product > 300000) { return "IV" }
        return "SEP"
    case 3:
        if ps < 50 { return "Not regulated" }
        if vOrDn < 1 { return "SEP" }
        if ps >= 50 && ps < 1000 && product <= 20000 { return "SEP" }
        if product <= 20000 && ps >= 50 && ps < 1000 { return "SEP" }
        if ps >= 50 && ps < 1000 && product > 20000 { return "I" }
        if ps >= 1000 && ps < 50000 { return "II" }
        if ps >= 50000 { return "III" }
        if product > 20000 && ps < 1000 { return "I" }
        return "SEP"
    case 4:
        if ps < 50 { return "Not regulated" }
        if vOrDn < 10 { return "SEP" }
        if ps >= 50 && ps < 1000 && product <= 1000000 { return "SEP" }
        if product > 1000000 && ps < 1000 { return "I" }
        if ps >= 1000 && ps < 50000 { return "II" }
        if ps >= 50000 { return "III" }
        return "SEP"
    case 5:
        if ps < 50 { return "Not regulated" }
        if vOrDn < 2 { return "SEP" }
        if product <= 5000 && ps >= 50 && ps < 3200 { return "SEP" }
        if product > 5000 && product <= 20000 && ps >= 50 { return "I" }
        if product > 20000 && ps >= 50 && ps <= 3200 { return "II" }
        if (product > 20000 && ps > 3200) || product > 300000 { return "III" }
        if vOrDn > 1000 && ps > 300 { return "IV" }
        if product > 300000 { return "IV" }
        return "II"
    case 6:
        if ps < 50 { return "Not regulated" }
        if vOrDn < 25 { return "SEP" }
        if vOrDn >= 25 && vOrDn < 100 && ps >= 50 && product <= 100000 { return "I" }
        if vOrDn >= 25 && vOrDn < 100 && product > 100000 { return "II" }
        if vOrDn >= 100 && vOrDn < 350 && ps < 3500 && product <= 350000 { return "II" }
        if vOrDn >= 100 && ps >= 3500 { return "III" }
        if vOrDn >= 350 || product > 350000 { return "III" }
        return "I"
    case 7:
        if ps < 50 { return "Not regulated" }
        if vOrDn < 32 { return "SEP" }
        if vOrDn >= 32 && vOrDn < 100 && product <= 100000 { return "SEP" }
        if vOrDn >= 32 && product > 100000 && product <= 350000 { return "I" }
        if vOrDn >= 100 && vOrDn < 250 && product > 350000 { return "II" }
        if vOrDn >= 250 || product > 500000 { return "III" }
        if product > 350000 && vOrDn >= 100 { return "II" }
        return "I"
    case 8:
        if ps < 50 { return "Not regulated" }
        if vOrDn < 25 { return "SEP" }
        if ps >= 50 && ps < 100 && vOrDn >= 25 { return "SEP" }
        if ps >= 100 && ps < 1000 && product <= 200000 { return "I" }
        if ps >= 1000 && ps < 50000 { return "II" }
        if ps >= 50000 { return "III" }
        if product > 200000 { return "II" }
        return "I"
    case 9:
        if ps < 50 { return "Not regulated" }
        if vOrDn < 200 { return "SEP" }
        if ps >= 50 && ps < 1000 && product <= 500000 { return "SEP" }
        if product > 500000 && ps < 1000 { return "I" }
        if ps >= 1000 && ps < 50000 { return "II" }
        if ps >= 50000 { return "III" }
        return "I"
    default:
        return "Not regulated"
    }
}

func getConformityModules(_ category: String) -> ConformityModules {
    switch category {
    case "I":
        return ConformityModules(withoutQuality: "A", withQuality: "A")
    case "II":
        return ConformityModules(withoutQuality: "A2", withQuality: "A2 or D1 or E1")
    case "III":
        return ConformityModules(
            withoutQuality: "B (design type) + F or B (production type) + C2",
            withQuality: "H or B (production type) + E or B (design type) + D"
        )
    case "IV":
        return ConformityModules(
            withoutQuality: "G or B (production type) + F",
            withQuality: "H1 or B (production type) + D"
        )
    default:
        return ConformityModules(withoutQuality: "N/A", withQuality: "N/A")
    }
}

func getCategoryColorHex(_ category: String) -> String {
    switch category {
    case "SEP": return "#10b981"
    case "I": return "#3b82f6"
    case "II": return "#eab308"
    case "III": return "#f97316"
    case "IV": return "#ef4444"
    default: return "#6b7280"
    }
}

func getCategoryRisk(_ category: String) -> String {
    switch category {
    case "SEP": return "Sound Engineering Practice"
    case "Not regulated": return "Below regulation threshold"
    case "I": return "Low Risk"
    case "II": return "Medium Risk"
    case "III": return "High Risk"
    case "IV": return "Very High Risk"
    default: return ""
    }
}

/// Display formatter used in the results header / PDF (mirrors
/// `formatCategoryDisplayLabel` in `export/GraphGeometry.kt`).
func formatCategoryDisplayLabel(_ category: String) -> String {
    switch category {
    case "Not regulated": return "Not Regulated"
    case "SEP": return "Sound Engineering Practice"
    default: return "Category \(category)"
    }
}

func requiresPrEng(_ category: String) -> Bool {
    ["II", "III", "IV"].contains(category)
}
