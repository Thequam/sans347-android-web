# SANS 347 — iOS (SwiftUI)

A native iPhone app for categorizing pressure equipment per **SANS 347:2024 (3rd Edition)**. This is a faithful, full-parity port of the Android (`../Android`) and web (`../sans347-app`) apps: identical safety-critical logic, the same four screens and dark-cyan design system, the custom log-log categorization graph with pinch/zoom/pan, and PNG + PDF export.

## Requirements

- A **Mac** with **Xcode 16 or newer** (native Swift/SwiftUI cannot be compiled on Windows or Linux).
- Deployment target: **iOS 16.0+**. Runs on iPhone and iPad (the immersive landscape graph view mirrors Android).

## Build & run

1. Open `SANS347.xcodeproj` in Xcode.
2. Select the **SANS347** target → **Signing & Capabilities** → choose your Apple ID **Team** (a free personal team is fine for running on your own device).
3. Pick a destination (a Simulator such as *iPhone 16*, or your plugged-in iPhone).
4. Press **Run** (⌘R).

From the command line on a Mac:

```bash
xcodebuild -project SANS347.xcodeproj -scheme SANS347 \
  -destination 'platform=iOS Simulator,name=iPhone 16' build
```

## Tests (parity suite)

`SANS347Tests/CategorizationTests.swift` asserts the categorization output against the standard — the `selectFigure` Table-1 matrix, the `PS < 50` "Not regulated" floor, per-figure boundary vectors, the Steam Generator → figure 5 bypass, and form validation.

Run with **⌘U** in Xcode, or:

```bash
xcodebuild -project SANS347.xcodeproj -scheme SANS347 \
  -destination 'platform=iOS Simulator,name=iPhone 16' test
```

## Project structure

The Xcode project uses **synchronized folder groups** (Xcode 16+): every `.swift` file under `SANS347/` is automatically part of the app target, and everything under `SANS347Tests/` is part of the test target. You do **not** need to edit `project.pbxproj` when adding files — just create them in the right folder.

```
iOS/
  SANS347.xcodeproj/
  SANS347/
    App/          SANS347App.swift (entry point), AppState.swift (ObservableObject)
    Models/       GraphModels.swift
    Data/         GraphData.swift (figures 1–9), Categorization.swift (logic)
    Theme/        SansColors.swift, Typography.swift, Color+Hex.swift
    Graph/        GraphGeometry, GraphRenderer (Core Graphics), Sans347GraphView, ZoomableGraphBox
    Views/        RootView, Chrome/AppChrome, Screens/*, Components/*
    Export/       GraphImageExporter (PNG), ReportPDFExporter (PDF), ShareSheet
    Resources/    Assets.xcassets (AccentColor = #00C2FF cyan, AppIcon placeholder)
  SANS347Tests/   CategorizationTests.swift
```

### Notes

- **Single render pipeline.** `Graph/GraphRenderer.swift` draws every figure (background → grid → border → boundary lines + rotated labels → category zone labels → ticks → axis titles → plotted point with crosshairs and the "Your Equipment" pill). It is used by the on-screen `Sans347GraphView` and reused by both exporters, so the PNG/PDF match the screen exactly.
- **App icon.** A cyan `AccentColor` is provided; the `AppIcon` set is an empty placeholder — drop a 1024×1024 PNG into `Resources/Assets.xcassets/AppIcon.appiconset` when you have artwork.
- **Keeping parity.** This app must stay in lock-step with the Android and web ports. If you change figure data or `selectFigure`/`determineCategory`, make the mirror change on the other platforms and keep the test suite green.
- **Fallback if the project won't open.** If your Xcode is older than 16 (no synchronized groups), create a new iOS App project named `SANS347` (SwiftUI, bundle id `com.sans347.app`), delete its starter files, drag the `SANS347/` folder in (choose *Create groups*), add a Unit Testing Bundle target and drag in `SANS347Tests/`, then build.
```
