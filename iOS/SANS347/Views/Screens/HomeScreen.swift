import SwiftUI
import UIKit

/// Home / Input page, mirroring `HomeScreen.kt`.
struct HomeScreen: View {
    @EnvironmentObject private var state: AppState

    private var isSteamGen: Bool { state.equipmentType == "Steam Generator" }
    private var isPiping: Bool { state.equipmentType == "Piping" }
    private var volumeLabel: String { isPiping ? "Diameter" : "Volume" }
    private var volumeUnit: String { isPiping ? "DN" : "L" }
    private var volumePlaceholder: String { isPiping ? "Enter diameter" : "Enter vol..." }
    private var volumeDesc: String { isPiping ? "Nominal pipe diameter" : "Internal volume capacity" }

    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                heroBlock
                equipmentCard
                if !isSteamGen {
                    stateCard
                    fluidCard
                }
                inputCard
                calculateButton
                clearButton
                viewResultsButton
                footerCard
            }
            .maxContentWidth(672)
            .padding(.horizontal, 16)
            .padding(.top, 12)
            .padding(.bottom, 100)
        }
        .scrollDismissesKeyboard(.interactively)
        .frame(maxWidth: .infinity, maxHeight: .infinity)
        .background(SansColors.pageBackground)
    }

    // MARK: - Sections

    private var heroBlock: some View {
        VStack(spacing: 4) {
            Text("Pressure Equipment\nCategorization")
                .font(.system(size: 24, weight: .bold))
                .foregroundColor(.white)
                .multilineTextAlignment(.center)
            Text("Determine the appropriate category and conformity assessment requirements for your pressure equipment according to SANS 347:2024")
                .font(.system(size: 14))
                .foregroundColor(SansColors.primaryCyan)
                .multilineTextAlignment(.center)
        }
        .frame(maxWidth: .infinity)
        .padding(.horizontal, 12)
        .padding(.vertical, 16)
        .background(SansColors.mediumGray)
        .clipShape(RoundedCorners(radius: 12, corners: [.topLeft, .topRight]))
    }

    private var equipmentCard: some View {
        SansCard {
            VStack(spacing: 0) {
                ZStack(alignment: .topTrailing) {
                    VStack(spacing: 0) {
                        Text("Equipment Type")
                            .font(.system(size: 18, weight: .bold))
                            .foregroundColor(.black)
                        Text("Select the type of pressure equipment")
                            .font(.system(size: 14))
                            .foregroundColor(SansColors.gray500)
                            .padding(.bottom, 12)
                    }
                    .frame(maxWidth: .infinity)
                    SteamGenToggle(selected: isSteamGen) {
                        state.equipmentType = isSteamGen ? nil : "Steam Generator"
                    }
                }
                HStack(spacing: 16) {
                    SelectButton(selected: state.equipmentType == "Pressure Vessels",
                                 title: "Pressure Vessels",
                                 subtitle: "Tanks, containers, boilers") {
                        state.equipmentType = "Pressure Vessels"
                    }
                    SelectButton(selected: state.equipmentType == "Piping",
                                 title: "Piping",
                                 subtitle: "Pipes, fittings, assemblies") {
                        state.equipmentType = "Piping"
                    }
                }
            }
        }
    }

    private var stateCard: some View {
        selectionCard(title: "State of Contents", subtitle: "What state will the fluid be in?") {
            HStack(spacing: 16) {
                SelectButton(selected: state.stateOfContents == "Gas", title: "Gas", subtitle: "Gaseous state") {
                    state.stateOfContents = "Gas"
                }
                SelectButton(selected: state.stateOfContents == "Liquid", title: "Liquid", subtitle: "Liquid state") {
                    state.stateOfContents = "Liquid"
                }
            }
        }
    }

    private var fluidCard: some View {
        selectionCard(title: "Fluid Group", subtitle: "Classification based on hazard level") {
            HStack(spacing: 16) {
                SelectButton(selected: state.fluidGroup == "Dangerous", title: "Dangerous", subtitle: "Group 1 - Higher risk") {
                    state.fluidGroup = "Dangerous"
                }
                SelectButton(selected: state.fluidGroup == "Non-Dangerous", title: "Non-Dangerous", subtitle: "Group 2 - Lower risk") {
                    state.fluidGroup = "Non-Dangerous"
                }
            }
        }
    }

    private var inputCard: some View {
        SansCard {
            HStack(alignment: .top, spacing: 16) {
                UnitField(label: "Design Pressure",
                          description: "Maximum allowable pressure",
                          value: $state.designPressure,
                          unit: "kPa",
                          placeholder: "Enter pr...")
                UnitField(label: volumeLabel,
                          description: volumeDesc,
                          value: $state.volumeOrDiameter,
                          unit: volumeUnit,
                          placeholder: volumePlaceholder)
            }
        }
    }

    private var calculateButton: some View {
        Button {
            state.calculateAndGoToResults()
        } label: {
            HStack(spacing: 8) {
                Image(systemName: "function")
                Text("Calculate Category").font(.system(size: 18, weight: .bold))
            }
            .frame(maxWidth: .infinity)
            .frame(height: 64)
            .foregroundColor(state.formValid ? .white : SansColors.gray400)
            .background(state.formValid ? SansColors.primaryCyan : SansColors.gray200)
            .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .buttonStyle(.plain)
        .disabled(!state.formValid)
    }

    private var clearButton: some View {
        Button {
            state.clearAll()
        } label: {
            HStack(spacing: 0) {
                Image(systemName: "arrow.clockwise")
                    .foregroundColor(SansColors.primaryCyan)
                    .padding(.trailing, 8)
                Text("Clear All").foregroundColor(SansColors.primaryCyan).fontWeight(.medium)
                Text(" Fields").foregroundColor(.white).fontWeight(.medium)
            }
            .frame(maxWidth: .infinity)
            .frame(height: 48)
            .background(SansColors.darkGray)
            .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .buttonStyle(.plain)
    }

    private var viewResultsButton: some View {
        Button {
            state.setCurrentPage(3)
        } label: {
            HStack(spacing: 8) {
                Image(systemName: "list.bullet.rectangle")
                Text("View Results").fontWeight(.bold)
            }
            .frame(maxWidth: .infinity)
            .frame(height: 48)
            .foregroundColor(state.result != nil ? .white : SansColors.gray400)
            .background(state.result != nil ? SansColors.success : SansColors.gray200)
            .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .buttonStyle(.plain)
        .disabled(state.result == nil)
    }

    private var footerCard: some View {
        SansCard(background: SansColors.gray50) {
            VStack(spacing: 4) {
                Text("SANS 347:2024 Edition 3.1")
                    .font(.system(size: 16, weight: .bold))
                    .foregroundColor(.black)
                    .multilineTextAlignment(.center)
                Text("Categorization and conformity assessment criteria for all pressure equipment")
                    .font(.system(size: 14))
                    .foregroundColor(SansColors.gray500)
                    .multilineTextAlignment(.center)
                Text("South African National Standard")
                    .font(.system(size: 12))
                    .foregroundColor(SansColors.gray400)
                    .multilineTextAlignment(.center)
            }
            .frame(maxWidth: .infinity)
        }
    }

    private func selectionCard<C: View>(title: String, subtitle: String, @ViewBuilder content: () -> C) -> some View {
        SansCard {
            VStack(spacing: 0) {
                Text(title).font(.system(size: 18, weight: .bold)).foregroundColor(.black)
                Text(subtitle).font(.system(size: 14)).foregroundColor(SansColors.gray500).padding(.bottom, 12)
                content()
            }
        }
    }
}

/// Shape that rounds only the specified corners (used for the hero block).
struct RoundedCorners: Shape {
    var radius: CGFloat
    var corners: UIRectCorner

    func path(in rect: CGRect) -> Path {
        let path = UIBezierPath(
            roundedRect: rect,
            byRoundingCorners: corners,
            cornerRadii: CGSize(width: radius, height: radius)
        )
        return Path(path.cgPath)
    }
}
