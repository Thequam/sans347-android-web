import SwiftUI
import UIKit

/// UIView that draws a figure via the shared `GraphRenderer`, so the on-screen
/// graph is identical to the PNG/PDF export.
final class GraphUIView: UIView {
    var config: GraphConfig? { didSet { setNeedsDisplay() } }
    var plotPoint: GraphPlotPoint? { didSet { setNeedsDisplay() } }

    override init(frame: CGRect) {
        super.init(frame: frame)
        backgroundColor = .white
        contentMode = .redraw
        isOpaque = true
    }

    required init?(coder: NSCoder) {
        super.init(coder: coder)
        backgroundColor = .white
        contentMode = .redraw
        isOpaque = true
    }

    override func draw(_ rect: CGRect) {
        guard let config, let ctx = UIGraphicsGetCurrentContext() else { return }
        GraphRenderer.draw(config: config, plotPoint: plotPoint, in: ctx, size: bounds.size)
    }
}

/// SwiftUI wrapper. Wrap with `.aspectRatio(GRAPH_ASPECT_RATIO, contentMode: .fit)`
/// for the portrait layout, or let it fill for the immersive landscape layout.
struct Sans347GraphView: UIViewRepresentable {
    let config: GraphConfig
    var plotPoint: GraphPlotPoint? = nil

    func makeUIView(context: Context) -> GraphUIView {
        let view = GraphUIView()
        view.config = config
        view.plotPoint = plotPoint
        view.setContentHuggingPriority(.defaultLow, for: .vertical)
        view.setContentHuggingPriority(.defaultLow, for: .horizontal)
        return view
    }

    func updateUIView(_ view: GraphUIView, context: Context) {
        view.config = config
        view.plotPoint = plotPoint
    }
}
