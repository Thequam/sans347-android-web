import SwiftUI
import UIKit

extension Color {
    /// Mirrors `colorFromHex` in `ui/theme/SansColors.kt`. Accepts "#RRGGBB",
    /// "RRGGBB", "#AARRGGBB" or "AARRGGBB".
    init(hex: String) {
        self.init(uiColor: UIColor(hex: hex))
    }
}

extension UIColor {
    convenience init(hex: String) {
        var h = hex
        if h.hasPrefix("#") { h.removeFirst() }
        let value = UInt64(h, radix: 16) ?? 0
        switch h.count {
        case 6:
            let r = Double((value & 0xFF0000) >> 16) / 255.0
            let g = Double((value & 0x00FF00) >> 8) / 255.0
            let b = Double(value & 0x0000FF) / 255.0
            self.init(red: r, green: g, blue: b, alpha: 1.0)
        case 8:
            let a = Double((value & 0xFF000000) >> 24) / 255.0
            let r = Double((value & 0x00FF0000) >> 16) / 255.0
            let g = Double((value & 0x0000FF00) >> 8) / 255.0
            let b = Double(value & 0x000000FF) / 255.0
            self.init(red: r, green: g, blue: b, alpha: a)
        default:
            self.init(red: 0, green: 0, blue: 0, alpha: 1.0)
        }
    }
}
