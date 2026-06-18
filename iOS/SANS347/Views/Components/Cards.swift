import SwiftUI

/// Standard white card: rounded 12, 1pt gray-200 border. Matches the recurring
/// card style across the Android screens.
struct SansCard<Content: View>: View {
    var padding: CGFloat = 16
    var background: Color = SansColors.white
    @ViewBuilder var content: () -> Content

    var body: some View {
        content()
            .padding(padding)
            .frame(maxWidth: .infinity)
            .background(background)
            .clipShape(RoundedRectangle(cornerRadius: 12))
            .overlay(
                RoundedRectangle(cornerRadius: 12)
                    .stroke(SansColors.gray200, lineWidth: 1)
            )
    }
}

extension View {
    /// Caps content width and centers it, mirroring the Android `widthIn(max:)`
    /// + `align(TopCenter)` pattern.
    func maxContentWidth(_ width: CGFloat) -> some View {
        frame(maxWidth: width)
            .frame(maxWidth: .infinity, alignment: .center)
    }
}
