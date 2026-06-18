import SwiftUI
import UIKit

/// Wraps `UIActivityViewController` (the iOS equivalent of Android's
/// `Intent.ACTION_SEND` share sheet) for sharing exported files.
struct ShareSheet: UIViewControllerRepresentable {
    let items: [Any]

    func makeUIViewController(context: Context) -> UIActivityViewController {
        let controller = UIActivityViewController(activityItems: items, applicationActivities: nil)
        // On iPad a UIActivityViewController is presented as a popover and will
        // assert/crash without an anchor; anchor it to the presenting view.
        if let popover = controller.popoverPresentationController {
            popover.sourceView = controller.view
            popover.permittedArrowDirections = []
        }
        return controller
    }

    func updateUIViewController(_ controller: UIActivityViewController, context: Context) {
        if let popover = controller.popoverPresentationController, let view = controller.view {
            popover.sourceView = view
            popover.sourceRect = CGRect(x: view.bounds.midX, y: view.bounds.midY, width: 0, height: 0)
        }
    }
}
