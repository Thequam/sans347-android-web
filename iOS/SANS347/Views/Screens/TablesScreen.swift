import SwiftUI

/// Tables page, mirroring `TablesScreen.kt` — the dark-themed reference
/// Tables 1-3 from SANS 347:2024. Uses the narrow (horizontally scrollable)
/// Table 1 layout, appropriate for phone widths.
struct TablesScreen: View {
    var body: some View {
        ScrollView {
            VStack(spacing: 24) {
                headerCard
                table1Card
                table2Card
                table3Card
            }
            .maxContentWidth(896)
            .padding(.horizontal, 16)
            .padding(.top, 16)
            .padding(.bottom, 100)
        }
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(SansColors.darkBlueGray)
    }

    private var headerCard: some View {
        HStack(spacing: 12) {
            Image(systemName: "doc.text").foregroundColor(.white)
            VStack(alignment: .leading, spacing: 0) {
                Text("Reference Tables").font(.system(size: 20, weight: .bold)).foregroundColor(.white)
                Text("SANS 347 - 2024 3rd Edition").font(.system(size: 14)).foregroundColor(SansColors.gray400)
            }
            Spacer()
        }
        .padding(16)
        .frame(maxWidth: .infinity)
        .background(SansColors.gray700)
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(SansColors.gray600, lineWidth: 1))
    }

    // MARK: - Table 1

    private var table1Card: some View {
        darkCard(title: "Table 1 — Categorization Figures") {
            VStack(alignment: .leading, spacing: 0) {
                ScrollView(.horizontal, showsIndicators: true) {
                    table1Body
                }
                .padding(8)
                VStack(alignment: .leading, spacing: 4) {
                    note("NOTE:", "For two-phase flow, the equipment should be categorized to the higher risk.")
                    note("a", "Transportable gas container and their safety and pressure accessories shall be assessed using table 3.")
                    note("b", "No pockets of gas may form above the liquid in the equipment, including steam.")
                    note("c", "Fluid group 1 = dangerous; fluid group 2 = not dangerous.")
                }
                .padding(16)
            }
        }
    }

    private let cw: CGFloat = 34
    private let labelW: CGFloat = 100

    private var table1Body: some View {
        VStack(spacing: 0) {
            HStack(spacing: 0) {
                cell("Equipment Type", width: labelW, bold: true, header: true, fontSize: 9)
                cell("Pressure Vessels", width: cw * 4, bold: true, header: true, center: true, fontSize: 8)
                cell("Steam Gen.", width: cw, bold: true, header: true, center: true, fontSize: 8)
                cell("Piping", width: cw * 4, bold: true, header: true, center: true, fontSize: 8)
                cell("TGC", width: cw, bold: true, header: true, center: true, fontSize: 8)
            }
            HStack(spacing: 0) {
                cell("State of Contents", width: labelW, bold: true, fontSize: 9)
                cell("Gas", width: cw * 2, center: true)
                cell("Liquid", width: cw * 2, center: true)
                cell("—", width: cw, center: true)
                cell("Gas", width: cw * 2, center: true)
                cell("Liquid\u{1D47}", width: cw * 2, center: true)
                cell("Gas", width: cw, center: true)
            }
            HStack(spacing: 0) {
                cell("Fluid Group\u{1D9C}", width: labelW, bold: true, fontSize: 9)
                ForEach(Array(["1", "2", "1", "2", "—", "1", "2", "1", "2", "1"].enumerated()), id: \.offset) { _, value in
                    cell(value, width: cw, center: true)
                }
            }
            HStack(spacing: 0) {
                cell("Refer to Figure", width: labelW, bold: true, fontSize: 9)
                ForEach(1...9, id: \.self) { i in
                    cell("\(i)", width: cw, fig: true, center: true)
                }
                cell("a", width: cw, center: true)
            }
        }
        .background(SansColors.gray700)
    }

    private func cell(_ text: String, width: CGFloat, bold: Bool = false, header: Bool = false, fig: Bool = false, center: Bool = false, fontSize: CGFloat = 10) -> some View {
        let color: Color = header ? .white : (fig ? SansColors.categoryI : (bold ? SansColors.gray300 : SansColors.gray200))
        return Text(text)
            .font(.system(size: fontSize, weight: (bold || fig || header) ? .bold : .regular))
            .foregroundColor(color)
            .multilineTextAlignment(center ? .center : .leading)
            .frame(width: width, alignment: center ? .center : .leading)
            .padding(4)
            .overlay(Rectangle().stroke(SansColors.gray600, lineWidth: 0.5))
    }

    // MARK: - Table 2

    private var table2Card: some View {
        darkCard(title: "Table 2 — Conformity Assessment Modules for Each Category") {
            VStack(alignment: .leading, spacing: 0) {
                VStack(spacing: 0) {
                    HStack(spacing: 0) {
                        Text("Hazard Category").frame(maxWidth: .infinity, alignment: .leading).padding(12)
                            .font(.system(size: 13, weight: .semibold)).foregroundColor(SansColors.gray300)
                        Text("Manufacturer without Certified Quality System").frame(maxWidth: .infinity, alignment: .leading).padding(12)
                            .font(.system(size: 13, weight: .semibold)).foregroundColor(.white)
                        Text("Manufacturer with Certified Quality System").frame(maxWidth: .infinity, alignment: .leading).padding(12)
                            .font(.system(size: 13, weight: .semibold)).foregroundColor(.white)
                    }
                    .background(SansColors.gray600)
                    moduleRow("I", SansColors.categoryI, "A", "A")
                    moduleRow("II", SansColors.categoryII, "A2", "A2 or D1 or E1")
                    moduleRow("III", SansColors.categoryIII, "B (design type) + F or\nB (production type) + C2", "H or\nB (production type) + E or\nB (design type) + D")
                    moduleRow("IV", SansColors.categoryIV, "G or\nB (production type) + F", "H1 or\nB (production type) + D")
                }
                .padding(8)
                VStack(alignment: .leading, spacing: 4) {
                    Text("Module Definitions:").font(.system(size: 12, weight: .semibold)).foregroundColor(SansColors.gray300)
                    ForEach(Self.moduleDefinitions, id: \.0) { code, body in
                        def(code, body)
                    }
                    Spacer().frame(height: 8)
                    ForEach(Self.table2Notes, id: \.0) { label, body in
                        note(label, body)
                    }
                }
                .padding(16)
            }
        }
    }

    private func moduleRow(_ cat: String, _ color: Color, _ without: String, _ with: String) -> some View {
        HStack(alignment: .top, spacing: 0) {
            Text(cat).font(.system(size: 18, weight: .bold)).foregroundColor(color)
                .frame(width: 48, alignment: .leading).padding(12)
            Text(without).font(.system(size: 14)).foregroundColor(SansColors.gray200)
                .frame(maxWidth: .infinity, alignment: .leading).padding(12)
            Text(with).font(.system(size: 14)).foregroundColor(SansColors.gray200)
                .frame(maxWidth: .infinity, alignment: .leading).padding(12)
        }
        .overlay(Rectangle().stroke(SansColors.gray600, lineWidth: 1))
    }

    // MARK: - Table 3

    private var table3Card: some View {
        darkCard(title: "Table 3 — Conformity Assessment Modules for Transportable Gas Containers") {
            VStack(alignment: .leading, spacing: 0) {
                Text("National legislation requires that all pressure equipment, including transportable gas containers, shall be categorized in accordance with this standard. Transportable gas containers manufactured in accordance with a relevant health and safety standard shall be deemed to comply with the categorization requirements of this standard.")
                    .font(.system(size: 12)).foregroundColor(SansColors.gray300).padding(16)
                HStack(spacing: 0) {
                    Text("Hazard Category").frame(maxWidth: .infinity, alignment: .leading).padding(12)
                        .font(.system(size: 14, weight: .semibold)).foregroundColor(SansColors.gray300)
                    Text("Conformity Assessment Modules a and b").frame(maxWidth: .infinity, alignment: .leading).padding(12)
                        .font(.system(size: 13, weight: .semibold)).foregroundColor(.white)
                }
                .background(SansColors.gray600)
                HStack(spacing: 0) {
                    Text("III").font(.system(size: 18, weight: .bold)).foregroundColor(SansColors.categoryIII)
                        .frame(maxWidth: .infinity, alignment: .leading).padding(12)
                    Text("B + F").foregroundColor(SansColors.gray200)
                        .frame(maxWidth: .infinity, alignment: .leading).padding(12)
                }
                .overlay(Rectangle().stroke(SansColors.gray600, lineWidth: 1))
                VStack(alignment: .leading, spacing: 4) {
                    note("NOTE:", "Table 3 covers test pressures 0 kPa to 300 000 kPa and volume 0,5 L to 3 000 L (water capacity).")
                    def("B", "type examination — design type")
                    def("F", "conformity to type based on pressure equipment verification")
                    Spacer().frame(height: 8)
                    note("a", "Imported transportable gas containers from the European Union shall comply with the Transportable Pressure Equipment Directive (TPED) 2010/35/EU requirements.")
                    note("b", "Imported transportable gas containers from the United Kingdom shall bear the Rho (π) symbol and the UKCA mark in accordance with the UK Carriage of Dangerous Goods and Use of Transportable Pressure Equipment Regulations 2009.")
                }
                .padding(16)
            }
        }
    }

    // MARK: - Shared

    private func darkCard<Content: View>(title: String, @ViewBuilder content: () -> Content) -> some View {
        VStack(alignment: .leading, spacing: 0) {
            Text(title)
                .font(.system(size: 18, weight: .bold)).foregroundColor(.white)
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(16)
                .background(SansColors.gray600)
            content()
        }
        .frame(maxWidth: .infinity)
        .background(SansColors.gray700)
        .clipShape(RoundedRectangle(cornerRadius: 12))
        .overlay(RoundedRectangle(cornerRadius: 12).stroke(SansColors.gray600, lineWidth: 1))
    }

    private func note(_ label: String, _ body: String) -> some View {
        Text("\(label) \(body)")
            .font(.system(size: 12)).foregroundColor(SansColors.gray400)
            .frame(maxWidth: .infinity, alignment: .leading)
    }

    private func def(_ code: String, _ body: String) -> some View {
        Text("\(code) = \(body)")
            .font(.system(size: 12)).foregroundColor(SansColors.gray400)
            .frame(maxWidth: .infinity, alignment: .leading)
    }

    private static let moduleDefinitions: [(String, String)] = [
        ("A", "internal production control"),
        ("A2", "internal production control plus supervised pressure equipment checks at random intervals"),
        ("B", "type examination — production type or type examination — design type"),
        ("C2", "conformity to type based on internal production control plus supervised pressure equipment checks at random intervals"),
        ("D", "conformity to type based on quality assurance of the production process"),
        ("D1", "quality assurance of the production process"),
        ("E", "conformity to type based on pressure equipment quality assurance"),
        ("E1", "product quality assurance for final pressure equipment inspection and testing"),
        ("F", "conformity to type based on pressure equipment verification"),
        ("G", "conformity based on unit verification"),
        ("H", "conformity based on full quality assurance"),
        ("H1", "conformity based on full quality assurance plus design examination"),
    ]

    private static let table2Notes: [(String, String)] = [
        ("NOTE 1:", "For RSA/CI/OHSA marked pressure equipment intended for non-nuclear use, refer to annex B."),
        ("NOTE 2:", "For RSA/CI/OHSA marked pressure equipment intended for nuclear use, refer to annex C."),
        ("NOTE 3:", "For non-RSA/CI/OHSA marked pressure equipment intended for nuclear use, refer to annex C."),
    ]
}
