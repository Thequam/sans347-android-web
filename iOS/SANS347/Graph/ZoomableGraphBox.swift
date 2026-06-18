import SwiftUI

/// Pinch-to-zoom / pan / double-tap-to-reset wrapper, mirroring
/// `ui/graph/ZoomableGraphBox.kt` (scale clamp 1...5, symmetric pan clamp).
struct ZoomableGraphBox<Content: View>: View {
    @ViewBuilder var content: () -> Content

    @State private var scale: CGFloat = 1
    @State private var lastScale: CGFloat = 1
    @State private var offset: CGSize = .zero
    @State private var lastOffset: CGSize = .zero
    @State private var contentSize: CGSize = .zero

    private func clampOffset(_ s: CGFloat, _ o: CGSize) -> CGSize {
        if s <= 1 { return .zero }
        let maxX = contentSize.width * (s - 1) / 2
        let maxY = contentSize.height * (s - 1) / 2
        return CGSize(
            width: min(max(o.width, -maxX), maxX),
            height: min(max(o.height, -maxY), maxY)
        )
    }

    var body: some View {
        let magnify = MagnificationGesture()
            .onChanged { value in
                let newScale = min(max(lastScale * value, 1), 5)
                scale = newScale
                offset = clampOffset(newScale, lastOffset)
            }
            .onEnded { _ in
                lastScale = scale
                lastOffset = offset
            }

        let pan = DragGesture()
            .onChanged { value in
                guard scale > 1 else { return }
                offset = clampOffset(scale, CGSize(
                    width: lastOffset.width + value.translation.width,
                    height: lastOffset.height + value.translation.height
                ))
            }
            .onEnded { _ in
                lastOffset = offset
            }

        return content()
            .background(
                GeometryReader { geo in
                    Color.clear
                        .onAppear { contentSize = geo.size }
                        .onChange(of: geo.size) { newValue in contentSize = newValue }
                }
            )
            .scaleEffect(scale, anchor: .center)
            .offset(offset)
            .clipped()
            .contentShape(Rectangle())
            .gesture(magnify)
            .simultaneousGesture(pan)
            .onTapGesture(count: 2) {
                withAnimation(.easeInOut(duration: 0.2)) {
                    scale = 1
                    lastScale = 1
                    offset = .zero
                    lastOffset = .zero
                }
            }
    }
}
