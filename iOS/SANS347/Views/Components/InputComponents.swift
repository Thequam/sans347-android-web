import SwiftUI

/// Two-line toggle card used for Equipment Type / State / Fluid Group, mirroring
/// `SelectButton` in `HomeScreen.kt` (cyan when selected, dark gray otherwise).
struct SelectButton: View {
    let selected: Bool
    let title: String
    let subtitle: String
    let onTap: () -> Void

    var body: some View {
        let bg = selected ? SansColors.primaryCyan : SansColors.darkGray
        let subColor = selected ? SansColors.white : SansColors.primaryCyan
        Button(action: onTap) {
            VStack(spacing: 2) {
                Text(title)
                    .font(.system(size: 14, weight: .bold))
                    .foregroundColor(.white)
                Text(subtitle)
                    .font(.system(size: 12))
                    .foregroundColor(subColor)
            }
            .frame(maxWidth: .infinity)
            .frame(height: 72)
            .padding(8)
            .background(bg)
            .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .buttonStyle(.plain)
    }
}

/// Small "Steam" pill toggle, top-right of the equipment card.
struct SteamGenToggle: View {
    let selected: Bool
    let onTap: () -> Void

    var body: some View {
        let bg = selected ? SansColors.primaryCyan : SansColors.gray200
        let textColor = selected ? SansColors.white : SansColors.gray500
        Button(action: onTap) {
            HStack(spacing: 4) {
                Circle()
                    .fill(selected ? SansColors.white : SansColors.gray400)
                    .frame(width: 5, height: 5)
                Text("Steam")
                    .font(.system(size: 9, weight: .semibold))
                    .foregroundColor(textColor)
            }
            .padding(.horizontal, 6)
            .padding(.vertical, 3)
            .background(bg)
            .clipShape(RoundedRectangle(cornerRadius: 12))
        }
        .buttonStyle(.plain)
    }
}

/// Numeric field with a centered label/description and a trailing cyan unit
/// pill, mirroring `UnitOutlinedField` in `HomeScreen.kt`.
struct UnitField: View {
    let label: String
    let description: String
    @Binding var value: String
    let unit: String
    let placeholder: String

    @FocusState private var isFocused: Bool

    var body: some View {
        VStack(spacing: 0) {
            Text(label)
                .font(.system(size: 14, weight: .bold))
                .foregroundColor(.black)
                .multilineTextAlignment(.center)
            Text(description)
                .font(.system(size: 12))
                .foregroundColor(SansColors.gray500)
                .multilineTextAlignment(.center)
                .lineLimit(2)
                .frame(height: 34, alignment: .top)
                .padding(.top, 4)
                .padding(.bottom, 8)
            ZStack(alignment: .trailing) {
                TextField(placeholder, text: $value)
                    .keyboardType(.decimalPad)
                    .focused($isFocused)
                    .font(.system(size: 14))
                    .padding(.horizontal, 12)
                    .frame(height: 48)
                    .background(SansColors.white)
                    .clipShape(RoundedRectangle(cornerRadius: 12))
                    .overlay(
                        RoundedRectangle(cornerRadius: 12)
                            .stroke((isFocused || !value.isEmpty) ? SansColors.primaryCyan : SansColors.gray200, lineWidth: 1)
                    )
                    .toolbar {
                        ToolbarItemGroup(placement: .keyboard) {
                            Spacer()
                            Button("Done") { isFocused = false }
                                .foregroundColor(SansColors.primaryCyan)
                        }
                    }
                Text(unit)
                    .font(.system(size: 12, weight: .bold))
                    .foregroundColor(.white)
                    .padding(.horizontal, 8)
                    .padding(.vertical, 4)
                    .background(SansColors.primaryCyan)
                    .clipShape(Capsule())
                    .padding(.trailing, 12)
            }
        }
        .frame(maxWidth: .infinity)
    }
}
