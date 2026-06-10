package com.sans347.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.horizontalScroll
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sans347.app.ui.theme.SansColors

@Composable
fun TablesScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SansColors.DarkBlueGray),
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .widthIn(max = 896.dp)
                    .fillMaxWidth()
                    .align(Alignment.TopCenter)
                    .verticalScroll(scrollState)
                    .padding(start = 16.dp, end = 16.dp + 10.dp, top = 16.dp, bottom = 100.dp),
            ) {
                TablesContent()
            }
            ScrollbarTrack(
                scrollState = scrollState,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .width(6.dp)
                    .fillMaxHeight()
                    .padding(top = 16.dp, bottom = 100.dp, end = 4.dp),
            )
        }
    }
}

@Composable
private fun ScrollbarTrack(
    scrollState: androidx.compose.foundation.ScrollState,
    modifier: Modifier = Modifier,
) {
    val max = scrollState.maxValue
    Canvas(modifier = modifier) {
        val trackW = size.width
        val trackH = size.height
        drawRoundRect(
            color = androidx.compose.ui.graphics.Color(0xFF1F2937),
            topLeft = Offset.Zero,
            size = Size(trackW, trackH),
            cornerRadius = CornerRadius(trackW / 2, trackW / 2),
        )
        if (max > 0) {
            val thumbH = (trackH * trackH / (trackH + max)).coerceIn(24f, trackH * 0.9f)
            val travel = (trackH - thumbH).coerceAtLeast(0f)
            val progress = scrollState.value / max.toFloat()
            val thumbY = travel * progress
            drawRoundRect(
                color = androidx.compose.ui.graphics.Color(0xFF4B5563),
                topLeft = Offset(0f, thumbY),
                size = Size(trackW, thumbH),
                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx()),
            )
        }
    }
}

@Composable
private fun TablesContent() {
        Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SansColors.Gray700, RoundedCornerShape(12.dp))
                .border(1.dp, SansColors.Gray600, RoundedCornerShape(12.dp)),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SansColors.Gray600, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Description, null, tint = SansColors.White, modifier = Modifier.padding(end = 12.dp))
                Column {
                    Text("Reference Tables", color = SansColors.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("SANS 347 - 2024 3rd Edition", color = SansColors.Gray400, fontSize = 14.sp)
                }
            }
        }

        Table1Card()
        Table2Card()
        Table3Card()
    }
}

@Composable
private fun Table1Card() {
    DarkTableCard(title = "Table 1 — Categorization Figures") {
        Column {
            Table1Body()
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                NoteLine("NOTE:", "For two-phase flow, the equipment should be categorized to the higher risk.")
                NoteLine("a", "Transportable gas container and their safety and pressure accessories shall be assessed using table 3.")
                NoteLine("b", "No pockets of gas may form above the liquid in the equipment, including steam.")
                NoteLine("c", "Fluid group 1 = dangerous; fluid group 2 = not dangerous.")
            }
        }
    }
}

@Composable
private fun Table1Body() {
    BoxWithConstraints(Modifier.padding(8.dp)) {
        val isWide = maxWidth >= 500.dp
        if (isWide) {
            Table1Wide()
        } else {
            Table1Narrow()
        }
    }
}

@Composable
private fun Table1Narrow() {
    val hScroll = rememberScrollState()
    val c = 34.dp
    val labelW = 100.dp
    Row(Modifier.horizontalScroll(hScroll)) {
        Column(
            modifier = Modifier.background(SansColors.Gray700),
        ) {
            Row(Modifier.background(SansColors.Gray600)) {
                TableCell("Equipment Type", bold = true, header = true, width = labelW, smallText = true)
                TableCell("Pressure Vessels", bold = true, header = true, width = c * 4, center = true, smallHeader = true)
                TableCell("Steam Gen.", bold = true, header = true, width = c, center = true, smallHeader = true)
                TableCell("Piping", bold = true, header = true, width = c * 4, center = true, smallHeader = true)
                TableCell("TGC", bold = true, header = true, width = c, center = true, smallHeader = true)
            }
            Row {
                TableCell("State of Contents", bold = true, width = labelW, smallText = true)
                TableCell("Gas", width = c * 2, center = true)
                TableCell("Liquid", width = c * 2, center = true)
                TableCell("—", width = c, center = true)
                TableCell("Gas", width = c * 2, center = true)
                TableCell("Liquid\u1D47", width = c * 2, center = true)
                TableCell("Gas", width = c, center = true)
            }
            Row {
                TableCell("Fluid Group\u1D9C", bold = true, width = labelW, smallText = true)
                TableCell("1", width = c, center = true)
                TableCell("2", width = c, center = true)
                TableCell("1", width = c, center = true)
                TableCell("2", width = c, center = true)
                TableCell("—", width = c, center = true)
                TableCell("1", width = c, center = true)
                TableCell("2", width = c, center = true)
                TableCell("1", width = c, center = true)
                TableCell("2", width = c, center = true)
                TableCell("1", width = c, center = true)
            }
            Row {
                TableCell("Refer to Figure", bold = true, width = labelW, smallText = true)
                TableCell("1", fig = true, width = c, center = true)
                TableCell("2", fig = true, width = c, center = true)
                TableCell("3", fig = true, width = c, center = true)
                TableCell("4", fig = true, width = c, center = true)
                TableCell("5", fig = true, width = c, center = true)
                TableCell("6", fig = true, width = c, center = true)
                TableCell("7", fig = true, width = c, center = true)
                TableCell("8", fig = true, width = c, center = true)
                TableCell("9", fig = true, width = c, center = true)
                TableCell("a", width = c, center = true)
            }
        }
    }
}

@Composable
private fun Table1Wide() {
    val lw = 1.8f
    val cw = 1f

    Column(Modifier.fillMaxWidth().background(SansColors.Gray700)) {
        Row(Modifier.fillMaxWidth().background(SansColors.Gray600)) {
            WideCell("Equipment Type", Modifier.weight(lw), bold = true, header = true)
            WideCell("Pressure Vessels", Modifier.weight(cw * 4), bold = true, header = true, center = true)
            WideCell("Steam", Modifier.weight(cw), bold = true, header = true, center = true)
            WideCell("Piping", Modifier.weight(cw * 4), bold = true, header = true, center = true)
            WideCell("TGC", Modifier.weight(cw), bold = true, header = true, center = true)
        }
        Row(Modifier.fillMaxWidth()) {
            WideCell("State of Contents", Modifier.weight(lw), bold = true)
            WideCell("Gas", Modifier.weight(cw * 2), center = true)
            WideCell("Liquid", Modifier.weight(cw * 2), center = true)
            WideCell("—", Modifier.weight(cw), center = true)
            WideCell("Gas", Modifier.weight(cw * 2), center = true)
            WideCell("Liquid\u1D47", Modifier.weight(cw * 2), center = true)
            WideCell("Gas", Modifier.weight(cw), center = true)
        }
        Row(Modifier.fillMaxWidth()) {
            WideCell("Fluid Group\u1D9C", Modifier.weight(lw), bold = true)
            WideCell("1", Modifier.weight(cw), center = true)
            WideCell("2", Modifier.weight(cw), center = true)
            WideCell("1", Modifier.weight(cw), center = true)
            WideCell("2", Modifier.weight(cw), center = true)
            WideCell("—", Modifier.weight(cw), center = true)
            WideCell("1", Modifier.weight(cw), center = true)
            WideCell("2", Modifier.weight(cw), center = true)
            WideCell("1", Modifier.weight(cw), center = true)
            WideCell("2", Modifier.weight(cw), center = true)
            WideCell("1", Modifier.weight(cw), center = true)
        }
        Row(Modifier.fillMaxWidth()) {
            WideCell("Refer to Figure", Modifier.weight(lw), bold = true)
            WideCell("1", Modifier.weight(cw), fig = true, center = true)
            WideCell("2", Modifier.weight(cw), fig = true, center = true)
            WideCell("3", Modifier.weight(cw), fig = true, center = true)
            WideCell("4", Modifier.weight(cw), fig = true, center = true)
            WideCell("5", Modifier.weight(cw), fig = true, center = true)
            WideCell("6", Modifier.weight(cw), fig = true, center = true)
            WideCell("7", Modifier.weight(cw), fig = true, center = true)
            WideCell("8", Modifier.weight(cw), fig = true, center = true)
            WideCell("9", Modifier.weight(cw), fig = true, center = true)
            WideCell("a", Modifier.weight(cw), center = true)
        }
    }
}

@Composable
private fun RowScope.WideCell(
    text: String,
    modifier: Modifier = Modifier,
    bold: Boolean = false,
    header: Boolean = false,
    fig: Boolean = false,
    center: Boolean = false,
) {
    Text(
        text,
        modifier = modifier
            .border(0.5.dp, SansColors.Gray600)
            .padding(horizontal = 4.dp, vertical = 6.dp),
        color = when {
            header -> Color.White
            fig -> Color(0xFF3B82F6)
            bold -> SansColors.Gray300
            else -> SansColors.Gray200
        },
        fontWeight = if (bold || fig || header) FontWeight.Bold else FontWeight.Normal,
        fontSize = 11.sp,
        maxLines = 1,
        textAlign = if (center) TextAlign.Center else TextAlign.Start,
    )
}

@Composable
private fun RowScope.TableCell(
    text: String,
    bold: Boolean = false,
    header: Boolean = false,
    fig: Boolean = false,
    width: androidx.compose.ui.unit.Dp,
    center: Boolean = false,
    smallHeader: Boolean = false,
    smallText: Boolean = false,
) {
    Text(
        text,
        modifier = Modifier
            .width(width)
            .border(0.5.dp, SansColors.Gray600)
            .padding(4.dp),
        color = when {
            header -> Color.White
            fig -> Color(0xFF3B82F6)
            bold -> SansColors.Gray300
            else -> SansColors.Gray200
        },
        fontWeight = if (bold || fig || header) FontWeight.Bold else FontWeight.Normal,
        fontSize = when {
            smallHeader -> 8.sp
            smallText -> 9.sp
            else -> 10.sp
        },
        maxLines = if (smallHeader || smallText) 1 else 3,
        textAlign = if (center) TextAlign.Center else TextAlign.Start,
    )
}

@Composable
private fun Table2Card() {
    DarkTableCard(title = "Table 2 — Conformity Assessment Modules for Each Category") {
        Column {
            Column(Modifier.padding(8.dp)) {
                Row(Modifier.fillMaxWidth().background(SansColors.Gray600)) {
                    Text("Hazard Category", Modifier.weight(0.25f).padding(12.dp), color = SansColors.Gray300, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("Manufacturer without Certified Quality System", Modifier.weight(0.375f).padding(12.dp), color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                    Text("Manufacturer with Certified Quality System", Modifier.weight(0.375f).padding(12.dp), color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                }
                ModuleRow("I", Color(0xFF3B82F6), "A", "A")
                ModuleRow("II", Color(0xFFEAB308), "A2", "A2 or D1 or E1")
                ModuleRow("III", Color(0xFFF97316), "B (design type) + F or\nB (production type) + C2", "H or\nB (production type) + E or\nB (design type) + D")
                ModuleRow("IV", Color(0xFFEF4444), "G or\nB (production type) + F", "H1 or\nB (production type) + D")
            }
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Module Definitions:", color = SansColors.Gray300, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                DefLine("A", "internal production control")
                DefLine("A2", "internal production control plus supervised pressure equipment checks at random intervals")
                DefLine("B", "type examination — production type or type examination — design type")
                DefLine("C2", "conformity to type based on internal production control plus supervised pressure equipment checks at random intervals")
                DefLine("D", "conformity to type based on quality assurance of the production process")
                DefLine("D1", "quality assurance of the production process")
                DefLine("E", "conformity to type based on pressure equipment quality assurance")
                DefLine("E1", "product quality assurance for final pressure equipment inspection and testing")
                DefLine("F", "conformity to type based on pressure equipment verification")
                DefLine("G", "conformity based on unit verification")
                DefLine("H", "conformity based on full quality assurance")
                DefLine("H1", "conformity based on full quality assurance plus design examination")
                Spacer(Modifier.height(8.dp))
                NoteLine("NOTE 1:", "For RSA/CI/OHSA marked pressure equipment intended for non-nuclear use, refer to annex B.")
                NoteLine("NOTE 2:", "For RSA/CI/OHSA marked pressure equipment intended for nuclear use, refer to annex C.")
                NoteLine("NOTE 3:", "For non-RSA/CI/OHSA marked pressure equipment intended for nuclear use, refer to annex C.")
            }
        }
    }
}

@Composable
private fun ModuleRow(cat: String, color: Color, without: String, with: String) {
    Row(Modifier.fillMaxWidth().border(1.dp, SansColors.Gray600)) {
        Text(cat, Modifier.width(48.dp).padding(12.dp), color = color, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text(without, Modifier.weight(1f).padding(12.dp), color = SansColors.Gray200, fontSize = 14.sp)
        Text(with, Modifier.weight(1f).padding(12.dp), color = SansColors.Gray200, fontSize = 14.sp)
    }
}

@Composable
private fun Table3Card() {
    DarkTableCard(title = "Table 3 — Conformity Assessment Modules for Transportable Gas Containers") {
        Column {
            Text(
                "National legislation requires that all pressure equipment, including transportable gas containers, shall be categorized in accordance with this standard. Transportable gas containers manufactured in accordance with a relevant health and safety standard shall be deemed to comply with the categorization requirements of this standard.",
                color = SansColors.Gray300,
                fontSize = 12.sp,
                modifier = Modifier.padding(16.dp),
            )
            Row(Modifier.fillMaxWidth().background(SansColors.Gray600)) {
                Text("Hazard Category", Modifier.weight(0.4f).padding(12.dp), color = SansColors.Gray300, fontWeight = FontWeight.SemiBold)
                Text("Conformity Assessment Modules a and b", Modifier.weight(0.6f).padding(12.dp), color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            }
            Row(Modifier.fillMaxWidth().border(1.dp, SansColors.Gray600)) {
                Text("III", Modifier.weight(0.4f).padding(12.dp), color = Color(0xFFF97316), fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text("B + F", Modifier.weight(0.6f).padding(12.dp), color = SansColors.Gray200)
            }
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                NoteLine("NOTE:", "Table 3 covers test pressures 0 kPa to 300 000 kPa and volume 0,5 L to 3 000 L (water capacity).")
                DefLine("B", "type examination — design type")
                DefLine("F", "conformity to type based on pressure equipment verification")
                Spacer(Modifier.height(8.dp))
                NoteLine("a", "Imported transportable gas containers from the European Union shall comply with the Transportable Pressure Equipment Directive (TPED) 2010/35/EU requirements.")
                NoteLine("b", "Imported transportable gas containers from the United Kingdom shall bear the Rho (π) symbol and the UKCA mark in accordance with the UK Carriage of Dangerous Goods and Use of Transportable Pressure Equipment Regulations 2009.")
            }
        }
    }
}

@Composable
private fun DarkTableCard(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SansColors.Gray700, RoundedCornerShape(12.dp))
            .border(1.dp, SansColors.Gray600, RoundedCornerShape(12.dp)),
    ) {
        Text(
            title,
            modifier = Modifier
                .fillMaxWidth()
                .background(SansColors.Gray600)
                .padding(16.dp),
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
        )
        content()
    }
}

@Composable
private fun NoteLine(label: String, body: String) {
    Text(buildString {
        append(label)
        append(" ")
        append(body)
    }, fontSize = 12.sp, color = SansColors.Gray400)
}

@Composable
private fun DefLine(code: String, body: String) {
    Text(buildString {
        append(code)
        append(" = ")
        append(body)
    }, fontSize = 12.sp, color = SansColors.Gray400)
}
