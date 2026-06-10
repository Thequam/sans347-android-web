# SANS 347 App — Design Specification

**Purpose:** Complete visual design reference to recreate the app's exact appearance on Android.

---

## 1. Color Palette

### 1.1 Primary Colors
| Name              | Hex       | Usage                                                      |
|-------------------|-----------|-------------------------------------------------------------|
| Primary Cyan      | `#00C2FF` | Accent color, active nav, selected buttons, unit badges, links |
| Dark Background   | `#0F0F0F` | Header bar, bottom nav bar                                  |
| Page Background   | `#F3F4F6` | Home page background, body default                          |
| White             | `#FFFFFF` | Cards, content backgrounds, text on dark                    |

### 1.2 Gray Scale
| Name            | Hex       | Usage                                         |
|-----------------|-----------|------------------------------------------------|
| Medium Gray     | `#353E43` | Title block background, input page header      |
| Dark Gray       | `#4A5568` | Unselected buttons, clear button bg             |
| Gray 700        | `#374151` | Table card backgrounds, plot border, axis text  |
| Gray 600        | `#4B5563` | Table headers, table borders                   |
| Gray 500        | `#6B7280` | Inactive nav icons, header toggle, zone labels  |
| Gray 400        | `#9CA3AF` | Disabled text, secondary labels, grid lines    |
| Gray 300        | `#D1D5DB` | Header button text                             |
| Gray 200        | `#E5E7EB` | Card borders, input borders, disabled bg        |
| Gray 100        | `#F3F4F6` | Page background                                |
| Gray 50         | `#F9FAFB` | Graph header card bg, footer info bg            |
| Dark Blue-Gray  | `#111827` | Tables page background                         |
| Near Black      | `#1C1C1E` | Alternative dark bg (unused currently)          |

### 1.3 Category Colors
| Category       | Hex       | Name    | Risk Level                  |
|----------------|-----------|---------|-----------------------------|
| SEP            | `#10B981` | Green   | Sound Engineering Practice  |
| Category I     | `#3B82F6` | Blue    | Low Risk                    |
| Category II    | `#EAB308` | Yellow  | Medium Risk                 |
| Category III   | `#F97316` | Orange  | High Risk                   |
| Category IV    | `#EF4444` | Red     | Very High Risk              |
| Not Regulated  | `#6B7280` | Gray    | Below regulation threshold  |

### 1.4 Accent/Functional Colors
| Purpose             | Hex       |
|---------------------|-----------|
| Boundary lines      | `#DC2626` |
| Application box bg  | `#EFF6FF` |
| Application border  | `#BFDBFE` |
| Application label   | `#2563EB` |
| Warning header bg   | `#FEFCE8` |
| Warning border      | `#FDE68A` |
| Warning icon        | `#D97706` |
| Success icon        | `#10B981` |
| Table figure nums   | `#3B82F6` |
| Minor gridlines     | `#C8CCD2` |
| Major gridlines     | `#9CA3AF` |
| Selected thumb bg   | `#ECFEFF` |

---

## 2. Typography

### 2.1 Font Family
- **Primary:** Arial, Helvetica, sans-serif
- **Fallback:** System default sans-serif
- **Android equivalent:** Use system default sans-serif (`Typeface.DEFAULT`) or explicitly set Roboto/Arial

### 2.2 Font Sizes (sp for Android)
| Element                        | Size    | Weight    | Color      |
|--------------------------------|---------|-----------|------------|
| App title "SANS 347"           | 18sp    | Bold (700)| `#FFFFFF`  |
| App subtitle "2024 3rd Ed"     | 12sp    | Medium 500| `#00C2FF`  |
| Page heading (h1)              | 24sp    | Bold      | `#FFFFFF` or `#000000` |
| Section heading (h2)           | 18sp    | Bold      | `#000000`  |
| Card sub-heading (h3)          | 14-16sp | Bold      | `#000000`  |
| Body text                      | 14sp    | Regular   | `#4B5563`  |
| Small text / descriptions      | 12sp    | Regular   | `#6B7280`  |
| Extra small text               | 10sp    | Medium    | `#9CA3AF`  |
| Button text (primary)          | 18sp    | Bold      | `#FFFFFF`  |
| Button text (secondary)        | 14sp    | Medium    | varies     |
| Selection button title         | 14sp    | Bold      | `#FFFFFF`  |
| Selection button subtitle      | 12sp    | Regular   | `#00C2FF` or `#FFFFFF` |
| Input text                     | 14sp    | Regular   | `#000000`  |
| Input placeholder              | 14sp    | Regular   | `#9CA3AF`  |
| Unit badge text                | 12sp    | Bold      | `#FFFFFF`  |
| Bottom nav label               | 10sp    | Medium    | varies     |
| Table cell text                | 14sp    | Regular   | `#E5E7EB`  |
| Table header text              | 14sp    | SemiBold  | `#FFFFFF`  |
| Graph axis labels              | 11sp*   | Regular   | `#374151`  |
| Graph axis titles              | 12sp*   | Bold      | `#1F2937`  |
| Graph zone labels              | 14sp*   | Bold      | `#6B7280`  |
| Graph line labels              | 10sp*   | Regular   | `#DC2626` or `#374151` |

*Graph text sizes scale proportionally with graph width (base: 750px)

---

## 3. Spacing & Sizing

### 3.1 Standard Spacing (dp for Android)
| Token  | Value | Usage                           |
|--------|-------|---------------------------------|
| xs     | 2dp   | Tiny gaps (icon-text)           |
| sm     | 4dp   | Small gaps, inner padding       |
| md     | 8dp   | Card inner padding, grid gaps   |
| lg     | 16dp  | Section padding, card padding   |
| xl     | 24dp  | Large section spacing           |
| 2xl    | 32dp  | Page padding bottom for nav     |

### 3.2 Component Sizes
| Component                | Size              |
|--------------------------|-------------------|
| Bottom nav bar height    | ~56dp + safe area |
| Bottom nav icon          | 20dp              |
| Header bar height        | ~44dp (when open) |
| Header toggle height     | ~16dp             |
| Selection button height  | 72dp              |
| Input field height       | 48dp (56dp large) |
| Calculate button height  | 64dp              |
| Clear/Results btn height | 48dp              |
| Category circle (result) | 128dp diameter    |
| Unit badge               | pill ~28x20dp     |
| Graph thumbnail button   | ~60x48dp          |
| Legend color block        | ~full-width x 36dp |
| Nav item min-width       | 60dp              |

### 3.3 Border Radius
| Element              | Radius |
|----------------------|--------|
| Cards                | 12dp   |
| Buttons              | 12dp   |
| Input fields         | 12dp   |
| Unit badges (pill)   | 9999dp (full round) |
| Category circle      | 64dp (half of 128)  |
| Thumbnail buttons    | 8dp    |
| Legend color blocks   | 8dp    |
| Nav counter badge    | 9999dp |
| Plot point label pill| 4dp (scaled) |

---

## 4. Component Designs

### 4.1 Selection Button
```
┌─────────────────────────┐
│       Title (bold)       │  Height: 72dp
│    Subtitle (small)      │  Border-radius: 12dp
└─────────────────────────┘

Selected:   bg=#00C2FF  title=white  subtitle=white
Unselected: bg=#4A5568  title=white  subtitle=#00C2FF
```

### 4.2 Input Field
```
┌──────────────────────────────────────────┐
│  Enter pr...                      [kPa]  │  Height: 48-56dp
└──────────────────────────────────────────┘
                                    ↑ pill badge

Border: 1dp #E5E7EB
Focus border: 1dp #00C2FF + 2dp glow ring (#00C2FF at 20% opacity)
Badge: bg=#00C2FF, text=white bold 12sp, pill-shaped, inside input right
```

### 4.3 Bottom Navigation Bar
```
┌──────────────────────────────────────────────────────┐
│  🏠 Home    📋 Results    📊 Graphs    📄 Tables    │
└──────────────────────────────────────────────────────┘

Background: #0F0F0F
Border-top: 1dp #333333
Icon size: 20dp
Label: 10sp, medium weight
Active: #00C2FF
Inactive: #6B7280
Padding-bottom: max(6dp, safe-area-inset)
```

### 4.4 Top Header Bar
```
┌──────────────────────────────────────────────────────┐
│  📄 Tables     SANS 347            Graphs 📊        │
│                2024 3rd Edition                       │
├──────────────────────────────────────────────────────┤
│                    ∧ (chevron)                        │
└──────────────────────────────────────────────────────┘

Background: #0F0F0F
Left/Right buttons: text=#D1D5DB, hover-bg=#374151, rounded 8dp
Center: "SANS 347" white bold 18sp, "2024 3rd Edition" #00C2FF 12sp
Chevron: #6B7280, 14dp icon
```

### 4.5 Card (Standard)
```
Background: #FFFFFF
Border: 1dp #E5E7EB
Border-radius: 12dp
Shadow: small elevation (2dp)
Padding: 16dp
```

### 4.6 Graph Container Card
```
Background: #FFFFFF
Border: 1dp #E5E7EB
Border-radius: 12dp
Padding: 8dp
Canvas fills width, aspect ratio 750:520
```

### 4.7 Category Result Circle
```
         ┌──────────┐
        /            \
       │     III      │   128dp diameter
        \            /    bg: category color
         └──────────┘     text: white, bold

Font size adapts:
  ≤3 chars → 48sp
  ≤5 chars → 24sp
  >5 chars → 14sp (for "Not regulated")
```

### 4.8 Table Card (Dark Theme)
```
┌──────────────────────────────────────────┐ bg=#4B5563
│  Table Title (white, bold, 18sp)          │
├──────────────────────────────────────────┤ bg=#374151
│  Header row      │  Col 1  │  Col 2     │
├──────────────────┼─────────┼────────────┤ border=#4B5563
│  Data row        │  val    │  val       │
├──────────────────┼─────────┼────────────┤
│  Data row        │  val    │  val       │
└──────────────────────────────────────────┘
│  Footer notes (gray-400, 12sp)           │
└──────────────────────────────────────────┘

Card bg: #374151
Card border: 1dp #4B5563
Header bg: #4B5563
Row borders: #4B5563
Header text: white, semibold
Data text: #E5E7EB
Label text: #D1D5DB
Notes: #9CA3AF, bold labels in #D1D5DB
```

---

## 5. Graph Rendering Specification

### 5.1 Canvas Dimensions
- Base size: 750 x 520 (logical pixels)
- Scales to container width, preserving aspect ratio
- All measurements below are relative to base 750px width

### 5.2 Margins (scaled by width/750)
```
Top:    20px
Right:  30px
Bottom: 60px
Left:   80px
```

### 5.3 Grid Lines
- **Major gridlines** (at powers of 10): color `#9CA3AF`, width 0.5px
- **Minor gridlines** (at 2x-9x each power): color `#C8CCD2`, width 0.5px
- Both X and Y axes

### 5.4 Plot Area
- Border: `#374151`, 1px width
- Background: `#FFFFFF` (white)

### 5.5 Boundary Lines
- Color: `#DC2626` (red), width 1.5px
- Labels positioned at midpoint (or custom coordinates if `labelX`/`labelY` set)
- Diagonal labels: rotated to match line angle, red text
- Horizontal/Vertical labels: not rotated (horizontal text), dark gray `#374151` text
- Label font: 10sp scaled (min 8sp)
- Label offset: -6px perpendicular from line (customizable via `labelOffset`)

### 5.6 Category Zone Labels
- Font: Bold, 14sp scaled (min 10sp)
- Color: `#6B7280`
- Text-align: center
- Positioned at specific (x, y) data coordinates converted to pixel coordinates via log scale

### 5.7 Axis Labels
- Y-axis labels: right-aligned, left of plot area, 11sp scaled (min 9sp), `#374151`
- X-axis labels: center-aligned, below plot area, same styling
- Number formatting: thousands separated by spaces (e.g., "100 000"), decimals with comma (e.g., "0,1")

### 5.8 Axis Titles
- Font: Bold, 12sp scaled (min 10sp), `#1F2937`
- X-axis title: centered below X labels
- Y-axis title: rotated -90 degrees, centered left of Y labels

### 5.9 Plot Point (Results only)
- **Crosshairs:** Dashed lines (4px on, 4px off), category color, 1.5px width, full plot area extent
- **Marker:** Filled circle, category color, radius 8px scaled (min 5px), white 2px stroke
- **Label pill:** "Your Equipment" text in white, category color background, rounded rectangle (4px radius), positioned 12px right and 12px above the marker

### 5.10 Log Scale Functions
```
logX(value) = margin.left + ((log10(value) - log10(xMin)) / (log10(xMax) - log10(xMin))) * plotWidth
logY(value) = margin.top + plotHeight - ((log10(value) - log10(yMin)) / (log10(yMax) - log10(yMin))) * plotHeight
```

---

## 6. Page Layouts

### 6.1 Home Page (Input)
```
┌──────────────────────────────────────┐ bg=#F3F4F6
│  ┌──────────────────────────────────┐│
│  │  Title Block (dark gray)         ││ bg=#353E43
│  │  "Pressure Equipment..."         ││ rounded-top
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │  Equipment Type                  ││ white card
│  │  [Vessels]  [Piping]             ││
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │  State of Contents               ││ white card
│  │  [Gas]      [Liquid]             ││
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │  Fluid Group                     ││ white card
│  │  [Dangerous] [Non-Dangerous]     ││
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │  [Pressure ▪kPa] [Volume ▪L]    ││ white card, 2-col
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │  🔢 Calculate Category           ││ cyan or gray
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │  🔄 Clear All Fields             ││ dark gray
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │  📋 View Results                  ││ green or gray
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │  SANS 347:2024 Edition 3.1      ││ footer info
│  └──────────────────────────────────┘│
└──────────────────────────────────────┘
```

### 6.2 Tables Page
```
┌──────────────────────────────────────┐ bg=#111827
│  ┌──────────────────────────────────┐│
│  │ 📄 Reference Tables              ││ dark card header
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │ Table 1 — Categorization Figures ││ dark card
│  │ ┌────────────────────────────┐   ││
│  │ │ scrollable table           │   ││
│  │ └────────────────────────────┘   ││
│  │ footnotes                        ││
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │ Table 2 — Conformity Assessment  ││ dark card
│  │ ...                              ││
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │ Table 3 — Transportable Gas      ││ dark card
│  │ ...                              ││
│  └──────────────────────────────────┘│
└──────────────────────────────────────┘
```

### 6.3 Graphs Page
```
┌──────────────────────────────────────┐ bg=#FFFFFF
│  ┌──────────────────────────────────┐│
│  │ Title + Subtitle    [3 of 9]    ││ gray header card
│  │ Figure 3       [< Prev] [Next >]││
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │                                  ││
│  │      [GRAPH CANVAS]              ││ white card
│  │                                  ││
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │ Application text...              ││ white card
│  └──────────────────────────────────┘│
│  ─────── footer text ───────         │
│  ┌──┬──┬──┬──┬──┬──┬──┬──┬──┐      │
│  │F1│F2│F3│F4│F5│F6│F7│F8│F9│      │ thumbnail grid
│  └──┴──┴──┴──┴──┴──┴──┴──┴──┘      │
│  ┌──────────────────────────────────┐│
│  │ [SEP] [I] [II] [III] [IV]       ││ legend card
│  └──────────────────────────────────┘│
└──────────────────────────────────────┘
```

### 6.4 Results Page
```
┌──────────────────────────────────────┐ bg=#FFFFFF
│  ┌──────────────────────────────────┐│
│  │ ✅ Calculation Results [← Back]  ││ header card
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │   Your Equipment Category        ││
│  │        ╭───────╮                 ││
│  │        │  III  │  ← 128dp circle ││ white card
│  │        ╰───────╯                 ││
│  │     Category III                 ││
│  │       High Risk                  ││
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │ Graph Title — Subtitle           ││
│  │ Figure N                         ││
│  │ ┌────────────────────[Result]─┐  ││
│  │ │                             │  ││
│  │ │    [GRAPH + PLOT POINT]     │  ││ graph card
│  │ │                             │  ││
│  │ └─────────────────────────────┘  ││
│  │ Application: ...                 ││ blue box
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │ Input Parameters (2-col grid)    ││ white card
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │ ⚠ Important Notes                ││ warning card
│  │ ...                              ││
│  └──────────────────────────────────┘│
│  ┌──────────────────────────────────┐│
│  │ Conformity Assessment Modules    ││ white card
│  │ (only for Cat I-IV)              ││
│  └──────────────────────────────────┘│
│  [← New Calculation] [Export Results]│ action buttons
└──────────────────────────────────────┘
```

---

## 7. Icons

The app uses **Lucide** icons (equivalent to Lucide Android or Material Icons):

| Icon Name       | Where Used                              | Size  |
|-----------------|-----------------------------------------|-------|
| FileText        | Tables nav, header, export button       | 16-22dp |
| BarChart3       | Graphs nav, header, graph section header| 16-20dp |
| Calculator      | Calculate button                        | 22dp  |
| RefreshCw       | Clear button                            | 16dp  |
| ChevronLeft     | Prev button, graph nav                  | 12dp  |
| ChevronRight    | Next button, graph nav                  | 12dp  |
| ChevronUp       | Header collapse toggle                  | 14dp  |
| ChevronDown     | Header expand toggle                    | 14dp  |
| CheckCircle2    | Results header                          | 24dp  |
| AlertCircle     | Important notes header                  | 18dp  |
| ArrowLeft       | Back button, New Calculation            | 14-16dp |
| Home            | Home nav tab                            | 20dp  |
| ClipboardList   | Results nav tab, View Results button    | 18-20dp |

**Android equivalent:** Use Material Icons or Lucide Android library.

---

## 8. Transitions & Animations

- **Page switching:** Instant (no animation). Pages swap directly.
- **Header collapse/expand:** `300ms ease-out` height transition
- **Button hover/press:** Color transition `150ms` (use ripple effect on Android)
- **No swipe gestures** between pages

---

## 9. Responsive Behavior

### 9.1 Portrait (Primary)
- Single column layout, full-width cards
- Graph thumbnail grid: 5 columns
- Input fields: 2-column grid within card

### 9.2 Landscape
- Graph container: max-height 70% of viewport
- Graph thumbnail grid: 9 columns (if width allows)
- Selection buttons and inputs scale slightly larger (height 56dp)

### 9.3 Tablet
- Max content width constrained (max-width ~672dp for inputs, ~896dp for graphs/tables)
- Center content horizontally with padding

---

## 10. Scrollbar Styling

### Home/Graphs/Results Pages
- Hidden scrollbar (still scrollable)

### Tables Page
- Custom thin scrollbar:
  - Width: 6dp
  - Track: `#1F2937`
  - Thumb: `#4B5563`, rounded 3dp
  - Thumb hover: `#6B7280`

---

## 11. Shadow & Elevation

| Element           | Elevation | Notes                    |
|-------------------|-----------|--------------------------|
| Cards (standard)  | 2dp       | Subtle shadow            |
| Table cards       | 8dp       | More prominent (shadow-2xl) |
| Floating result   | 4dp       | White bg with border     |
| Bottom nav bar    | 8dp       | Sits above content       |

---

## 12. Safe Area / Edge-to-Edge

- Bottom nav respects `safe-area-inset-bottom` (for devices with gesture bars)
- Content area has bottom padding of 56dp (nav bar height) to avoid overlap
- Viewport fits cover mode for notched devices
