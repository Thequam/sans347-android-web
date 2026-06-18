import SwiftUI

// The Android app uses the platform sans-serif (Roboto) and the web uses
// Arial/Helvetica. On iOS we use the system font (SF Pro), which is the
// closest faithful equivalent. Sizes are taken 1:1 from the Android `sp`
// values (1sp ~= 1pt at default scale). Helpers here keep call sites terse.

extension Font {
    static func sans(_ size: CGFloat, weight: Font.Weight = .regular) -> Font {
        .system(size: size, weight: weight)
    }
}

extension View {
    /// Convenience for the recurring "bold black heading" style.
    func sansHeading(_ size: CGFloat) -> some View {
        font(.system(size: size, weight: .bold)).foregroundColor(.black)
    }
}
