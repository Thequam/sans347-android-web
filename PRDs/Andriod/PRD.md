# SANS 347 Pressure Equipment Categorization — Product Requirements Document (PRD)

**Target Platform:** Android (Native via Android Studio)
**Source Reference:** Next.js web app at `sans347-app/`
**Standard:** SANS 347:2024 Edition 3.1 — South African National Standard

---

## 1. Product Overview

A single-screen Android application that helps engineers categorize pressure equipment according to SANS 347:2024. Users select equipment parameters, calculate the hazard category (SEP, I, II, III, IV, or Not Regulated), view the result plotted on the applicable SANS 347 graph, and reference conformity assessment requirements.

The app has **4 pages** navigated via a fixed bottom navigation bar:

| Page Index | Label   | Purpose                                    |
|------------|---------|--------------------------------------------|
| 0          | Tables  | SANS 347 reference tables (dark theme)     |
| 1          | Home    | Input form for equipment parameters        |
| 2          | Graphs  | Interactive graph viewer (9 figures)        |
| 3          | Results | Calculation results with plotted graph      |

---

## 2. Navigation Architecture

### 2.1 Top Header Bar (Collapsible)
- Background: `#0F0F0F`
- Contains three tappable elements in a row:
  - **Left:** "Tables" button with `FileText` icon → navigates to Tables page
  - **Center:** App title "SANS 347" (white, bold) + "2024 3rd Edition" (cyan `#00C2FF`) → navigates to Home page
  - **Right:** "Graphs" button with `BarChart3` icon → navigates to Graphs page
- **Toggle chevron** below the header row: tapping collapses/expands the header (ChevronUp/ChevronDown icon, color `#6b7280`)
- When collapsed, only the chevron toggle button is visible

### 2.2 Bottom Navigation Bar (Fixed)
- Fixed at screen bottom, always visible
- Background: `#0F0F0F`, top border `#333333`
- Respects safe area insets (bottom notch/gesture bar)
- 4 items in a row, each with icon (20px) + label (10px):
  - **Home** (HomeIcon) → page 1
  - **Results** (ClipboardList) → page 3
  - **Graphs** (BarChart3) → page 2
  - **Tables** (FileText) → page 0
- Active item color: `#00C2FF`; Inactive: `#6b7280`

### 2.3 Page Switching
- No swipe gestures or animations — instant page switch
- Main content area fills space between header and bottom nav
- Each page scrolls independently within its container

---

## 3. Page 1: Home (Input Page)

### 3.1 Layout
- Background: `#F3F4F6` (light gray)
- Scrollable vertically, max-width constrained
- Bottom padding to clear the fixed nav bar

### 3.2 Title Block
- Rounded top corners, background `#353e43`
- Title: "Pressure Equipment Categorization" — white, bold, 24sp
- Subtitle: "Determine the appropriate category..." — `#00C2FF`, 14sp

### 3.3 Selection Sections (3 cards, stacked vertically)
Each card: white background, rounded corners (12dp), light border, shadow

**Card 1 — Equipment Type:**
- Heading: "Equipment Type" (bold, black, 18sp, centered)
- Subtext: "Select the type of pressure equipment" (gray, 14sp)
- 2-column grid of selection buttons:
  - "Pressure Vessels" / subtitle "Tanks, containers, boilers"
  - "Piping" / subtitle "Pipes, fittings, assemblies"

**Card 2 — State of Contents:**
- Heading: "State of Contents"
- Subtext: "What state will the fluid be in?"
- Buttons: "Gas" / "Gaseous state" | "Liquid" / "Liquid state"

**Card 3 — Fluid Group:**
- Heading: "Fluid Group"
- Subtext: "Classification based on hazard level"
- Buttons: "Dangerous" / "Group 1 - Higher risk" | "Non-Dangerous" / "Group 2 - Lower risk"

**Selection Button Behavior:**
- Height: 72dp, rounded 12dp
- **Selected state:** Background `#00C2FF`, white text, white subtitle
- **Unselected state:** Background `#4A5568`, white text, `#00C2FF` subtitle

### 3.4 Input Fields Card
- White card with 2-column grid
- **Design Pressure:**
  - Label: "Design Pressure" (bold, black)
  - Description: "Maximum allowable pressure"
  - Number input with "kPa" badge (cyan pill, right-aligned inside input)
  - Placeholder: "Enter pr..."
- **Volume/Diameter** (label changes based on Equipment Type):
  - If Vessels: Label "Volume", unit "L", desc "Internal volume capacity", placeholder "Enter vol..."
  - If Piping: Label "Diameter", unit "DN", desc "Nominal pipe diameter", placeholder "Enter diameter"
  - Same layout as Design Pressure with appropriate unit badge

**Input Field Style:**
- Height: 48dp (56dp on larger screens)
- Rounded 12dp, border `#E5E7EB`
- Focus: border `#00C2FF`, outer glow `#00C2FF` at 20% opacity
- No number spinner controls
- Unit badge: `#00C2FF` background, white bold text, pill-shaped, positioned inside input right

### 3.5 Action Buttons (full-width, stacked)

1. **Calculate Category** — Height 64dp, rounded 12dp
   - Enabled: `#00C2FF` bg, white text, bold 18sp, Calculator icon
   - Disabled: `#E5E7EB` bg, `#9CA3AF` text
   - On tap: runs calculation, navigates to Results page

2. **Clear All Fields** — Height 48dp, rounded 12dp
   - Background `#4A5568`, "Clear All" in `#00C2FF`, "Fields" in white
   - RefreshCw icon in `#00C2FF`
   - Resets all selections and inputs

3. **View Results** — Height 48dp, rounded 12dp
   - Enabled (has result): `#10B981` bg, white text, bold
   - Disabled: `#E5E7EB` bg, `#9CA3AF` text
   - Navigates to Results page

### 3.6 Footer Info Box
- `#F9FAFB` background, border `#E5E7EB`, rounded 12dp
- Line 1: "SANS 347:2024 Edition 3.1" (bold, black)
- Line 2: "Categorization and conformity assessment criteria..." (gray, 14sp)
- Line 3: "South African National Standard" (light gray, 12sp)

---

## 4. Page 0: Tables (Reference Tables)

### 4.1 Layout
- Background: `#111827` (very dark blue-gray)
- Custom scrollbar (6px, dark track/thumb)
- 3 table cards stacked vertically

### 4.2 Tables Header Card
- Background `#374151`, border bottom `#4B5563`
- FileText icon (white) + "Reference Tables" (white, bold, 20sp)
- Subtitle: "SANS 347 - 2024 3rd Edition" (gray)

### 4.3 Table 1 — Categorization Figures
Maps equipment type → state → fluid group → figure number (1-9)
- Table header bg: `#4B5563`
- Columns: Equipment Type, Pressure Vessels (Gas Group 1/2, Liquid Group 1/2), Steam Generator, Piping (same breakdown), Transportable Gas Containers
- Figure numbers displayed in blue (`#3B82F6`, bold)
- Footer notes: NOTE about two-phase flow, notes a/b/c

### 4.4 Table 2 — Conformity Assessment Modules
Maps hazard category (I-IV) → required assessment modules
- Columns: Hazard Category, Manufacturer without QMS, Manufacturer with QMS
- Category colors: I=`#3B82F6`, II=`#EAB308`, III=`#F97316`, IV=`#EF4444`
- Module definitions listed below (A, A2, B, C2, D, D1, E, E1, F, G, H, H1)
- 3 additional notes about RSA/CI/OHSA marking

### 4.5 Table 3 — Transportable Gas Containers
- Category III (orange `#F97316`) → Modules B + F
- Notes about TPED 2010/35/EU and UK CDGUTP Regulations 2009

---

## 5. Page 2: Graphs (Interactive Graph Viewer)

### 5.1 Graph Header Card
- Background `#F9FAFB`, border `#E5E7EB`, rounded 12dp
- Graph title (bold, 18sp, black) + subtitle (`#00C2FF`)
- Counter badge: "X of 9" (gray pill)
- "Figure N" label
- Prev/Next buttons: bordered, white bg, small text, ChevronLeft/Right icons
- Disabled at boundaries (opacity 30%)

### 5.2 Graph Canvas
- White card, border `#E5E7EB`, 8dp padding
- Renders via Canvas 2D (on Android: use Android Canvas or a custom View)
- **Log-log scale** on both axes
- Grid: Major lines (gray `#9CA3AF`, 0.5px), minor lines (`#C8CCD2`, 0.5px)
- Plot border: `#374151`, 1px
- Boundary lines: Red `#DC2626`, 1.5px
- Line labels: rotated to match line angle; red for diagonals, dark gray for horizontal/vertical
- Category zone labels: Bold gray `#6B7280`, centered in their zones
- Responsive: scales to container width maintaining 750:520 aspect ratio

### 5.3 Graph Data (9 Figures)
All graph configurations with exact line coordinates and category zone positions are defined in `graphData.ts`. The 9 figures are:

| Fig | Type              | Fluid              | X-Axis | X Range        | Y Range           |
|-----|-------------------|--------------------|--------|----------------|--------------------|
| 1   | Pressure Vessels  | Dangerous gas      | V (L)  | 0.1–10,000     | 0.1–400,000        |
| 2   | Pressure Vessels  | Nondangerous gas   | V (L)  | 0.1–10,000     | 0.1–500,000        |
| 3   | Pressure Vessels  | Dangerous liquids  | V (L)  | 0.1–10,000     | 0.1–500,000        |
| 4   | Pressure Vessels  | Nondangerous liq.  | V (L)  | 0.1–100,000    | 0.1–500,000        |
| 5   | Steam Generators  | (all)              | V (L)  | 0.1–10,000     | 0.1–100,000        |
| 6   | Piping            | Dangerous gas      | DN     | 1–2,000        | 1–100,000          |
| 7   | Piping            | Nondangerous gas   | DN     | 1–20,000       | 1–200,000          |
| 8   | Piping            | Dangerous liquids  | DN     | 1–10,000       | 1–200,000          |
| 9   | Piping            | Nondangerous liq.  | DN     | 1–10,000       | 1–200,000          |

Each figure includes:
- Boundary line segments with coordinates, labels, colors, and optional label offsets
- Category zone labels (SEP, I, II, III, IV, Not regulated) with x,y positions

**IMPORTANT:** Port the complete `graphData.ts` file verbatim — it contains all line coordinates, category zone positions, and boundary equations.

### 5.4 Application Text
- Below graph: text from `graph.applicationText` in a white card
- Footer: `graph.footerText` centered, gray, with top border

### 5.5 Thumbnail Grid
- 5 columns on mobile, 9 columns on tablet
- Each thumbnail: bordered button with "Fig N" + equipment type abbreviation
- Selected: `#00C2FF` border, `#ECFEFF` background, cyan text
- Unselected: `#E5E7EB` border, white background, dark text

### 5.6 Category Legend
- White card, 5-column grid
- Colored rectangles (rounded 8dp) with white bold text:
  - SEP: `#10B981` (green)
  - I: `#3B82F6` (blue)
  - II: `#EAB308` (yellow)
  - III: `#F97316` (orange)
  - IV: `#EF4444` (red)
- Description text below each

---

## 6. Page 3: Results

### 6.1 Results Header
- Gray card with CheckCircle2 icon (green `#10B981`) + "Calculation Results" (bold)
- "Back to Input" button with ArrowLeft icon

### 6.2 Category Display
- White card, centered layout
- Large circle (128dp diameter) filled with category color
- Category name inside circle (white, bold):
  - ≤3 chars: 48sp font
  - ≤5 chars: 24sp font
  - >5 chars: 14sp font
- Below circle: "Category X" in category color (20sp) + risk level description (gray)

### 6.3 Graph with Plot Point
- Same graph rendering as Graphs page, but with an overlaid plot point:
  - Dashed crosshair lines (4px dash pattern) in category color
  - Filled circle marker (8dp radius scaled) with white stroke
  - "Your Equipment" label in a rounded pill (category color bg, white text)
- **Floating result card** (top-right corner of graph):
  - Toggleable (tap to expand/collapse)
  - Expanded: shows color dot, "Your Result", product formula, mini category circle, category name
  - Collapsed: just a small dot + "Result" text
  - Border color: `#00C2FF`

### 6.4 Application Note
- Blue-tinted card: bg `#EFF6FF`, border `#BFDBFE`
- "Application:" label in `#2563EB` (bold) + application text in gray

### 6.5 Input Parameters Summary
- White card with 2-column grid
- Each row: label (gray) → value (right-aligned)
- Numeric values displayed in `#00C2FF`
- Fields: Equipment Type, Design Pressure (kPa), State Contents, Volume/Diameter, Fluid Group

### 6.6 Important Notes
- Yellow warning header: bg `#FEFCE8`, border `#FDE68A`, AlertCircle icon in `#D97706`
- 3 notes about: SANS 347:2024 basis, compliance requirements, professional review recommendation

### 6.7 Conformity Assessment Modules
- Only shown when category is I, II, III, or IV (hidden for SEP and Not Regulated)
- Two subsections:
  - "Manufacturer without Certified Quality System" → module text in gray card
  - "Manufacturer with Certified Quality System" → module text in gray card

### 6.8 Action Buttons (row layout, centered)
- "New Calculation" — white bg, gray border, ArrowLeft icon → goes to Home page
- "Export Results" — `#00C2FF` bg, white text, FileText icon → triggers print/share

---

## 7. Calculation Logic

### 7.1 Figure Selection (`selectFigure`)
```
Input: equipmentType (Vessels/Piping), stateOfContents (Gas/Liquid), fluidGroup (Dangerous/Non-Dangerous)
Output: Figure ID (1-9)

Vessels + Gas + Dangerous → Figure 1
Vessels + Gas + Non-Dangerous → Figure 2
Vessels + Liquid + Dangerous → Figure 3
Vessels + Liquid + Non-Dangerous → Figure 4
Steam Generator → Figure 5
Piping + Gas + Dangerous → Figure 6
Piping + Gas + Non-Dangerous → Figure 7
Piping + Liquid + Dangerous → Figure 8
Piping + Liquid + Non-Dangerous → Figure 9
```

### 7.2 Category Determination (`determineCategory`)
Each figure has unique boundary logic based on:
- PS (Design Pressure in kPa)
- V or DN (Volume in L or Nominal Diameter)
- Product = PS × V or PS × DN

**Port the complete `determineCategory` switch statement from `graphData.ts` verbatim.** It contains 9 cases with precise boundary conditions for each figure.

### 7.3 Conformity Modules (`getConformityModules`)
```
Category I  → A | A
Category II → A2 | A2 or D1 or E1
Category III → B(design)+F or B(prod)+C2 | H or B(prod)+E or B(design)+D
Category IV → G or B(prod)+F | H1 or B(prod)+D
```

### 7.4 Validation Rules
- All 3 selections required (Equipment Type, State, Fluid Group)
- Both numeric inputs required and must be > 0
- Calculate button disabled until form is valid

---

## 8. Data Model

### 8.1 State Variables
```
currentPage: Int (0-3)
showHeader: Boolean
equipmentType: String? ("Pressure Vessels" | "Piping" | null)
stateOfContents: String? ("Gas" | "Liquid" | null)
fluidGroup: String? ("Dangerous" | "Non-Dangerous" | null)
designPressure: String (numeric input as text)
volumeOrDiameter: String (numeric input as text)
currentGraphIndex: Int (0-8)
result: ResultData? {
  category: String
  figureId: Int
  product: Double
  ps: Double
  vOrDn: Double
}
```

### 8.2 Graph Configuration Data
```
GraphConfig {
  id: Int
  title: String
  subtitle: String
  equipmentType: String
  fluidType: String
  xAxisLabel: String
  yAxisLabel: String
  xMin, xMax, yMin, yMax: Double
  lines: List<LineSegment>
  categoryZones: List<CategoryZone>
  footerText: String
  applicationText: String
  xVariable: "V" | "DN"
}

LineSegment {
  x1, y1, x2, y2: Double
  label: String?
  labelPosition: String? ("start" | "middle" | "end")
  labelOffset: Double? (perpendicular pixel offset)
  labelX, labelY: Double? (custom label coordinates)
  color: String?
}

CategoryZone {
  label: String
  x, y: Double
}
```

---

## 9. Android-Specific Implementation Notes

### 9.1 Graph Rendering
- Use Android `Canvas` with a custom `View` or Jetpack Compose `Canvas`
- Implement log-log scale transformations: `logX(v)` and `logY(v)` mapping data coordinates to pixel coordinates
- Scale all fonts, margins, line widths relative to view width (base 750px)
- Support high-DPI rendering

### 9.2 Bottom Navigation
- Use `BottomNavigationView` (Material) or Compose `NavigationBar`
- Handle system navigation bar insets (edge-to-edge)

### 9.3 Number Input
- Use `InputType.TYPE_CLASS_NUMBER | TYPE_NUMBER_FLAG_DECIMAL` for numeric inputs
- Hide increment/decrement spinners

### 9.4 Export/Share
- Replace `window.print()` with Android share intent or PDF generation

### 9.5 Responsive Layout
- Support portrait and landscape orientations
- In landscape, constrain graph height to 70% of viewport

---

## 10. Feature Checklist

- [ ] Bottom navigation bar with 4 tabs (Home, Results, Graphs, Tables)
- [ ] Collapsible top header with title and quick-nav buttons
- [ ] Equipment Type selection (Pressure Vessels / Piping)
- [ ] State of Contents selection (Gas / Liquid)
- [ ] Fluid Group selection (Dangerous / Non-Dangerous)
- [ ] Design Pressure numeric input with kPa unit badge
- [ ] Volume/Diameter numeric input with dynamic label and unit
- [ ] Form validation (all fields required, values > 0)
- [ ] Calculate button with disabled state
- [ ] Clear All button to reset form
- [ ] View Results button (enabled after calculation)
- [ ] Category calculation using SANS 347 boundary logic
- [ ] Results page with large category circle display
- [ ] Graph rendering with log-log scale, gridlines, boundary lines, labels
- [ ] Plot point with crosshairs and "Your Equipment" label on results graph
- [ ] Floating toggleable result card on graph
- [ ] Input parameters summary on results page
- [ ] Conformity assessment modules display (Categories I-IV only)
- [ ] Important notes section
- [ ] New Calculation / Export Results buttons
- [ ] Graph viewer with 9 figures and Prev/Next navigation
- [ ] Thumbnail grid for quick graph selection
- [ ] Category color legend
- [ ] Application text per graph
- [ ] 3 reference tables with full SANS 347 data
- [ ] Table 1: Equipment type → figure mapping
- [ ] Table 2: Conformity assessment modules with module definitions
- [ ] Table 3: Transportable gas containers
- [ ] Dark theme for Tables page
- [ ] Safe area / notch handling
