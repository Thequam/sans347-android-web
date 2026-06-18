import UIKit

/// Renders a figure to a 1500x1040 PNG (exactly 2x the 750x520 base, matching
/// `export/GraphBitmapRenderer.kt` / `GraphPngExporter.kt`) and writes it to a
/// temporary file for sharing.
enum GraphImageExporter {
    static let exportSize = CGSize(width: 1500, height: 1040)

    static func renderImage(config: GraphConfig, plotPoint: GraphPlotPoint?) -> UIImage {
        let format = UIGraphicsImageRendererFormat()
        format.scale = 1
        format.opaque = true
        let renderer = UIGraphicsImageRenderer(size: exportSize, format: format)
        return renderer.image { rendererContext in
            GraphRenderer.draw(config: config, plotPoint: plotPoint, in: rendererContext.cgContext, size: exportSize)
        }
    }

    /// Writes the PNG to a temp file and returns the URL, or nil on failure.
    static func exportToTempFile(config: GraphConfig, plotPoint: GraphPlotPoint?) -> URL? {
        let image = renderImage(config: config, plotPoint: plotPoint)
        guard let data = image.pngData() else { return nil }
        let url = FileManager.default.temporaryDirectory
            .appendingPathComponent("sans347-figure-\(config.id)-graph.png")
        do {
            try data.write(to: url, options: .atomic)
            return url
        } catch {
            return nil
        }
    }
}
