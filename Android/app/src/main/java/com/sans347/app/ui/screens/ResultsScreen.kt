package com.sans347.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sans347.app.data.ConformityModules
import com.sans347.app.data.GraphConfig
import com.sans347.app.data.ResultData
import com.sans347.app.data.getCategoryColorHex
import com.sans347.app.data.getCategoryRisk
import com.sans347.app.data.getConformityModules
import com.sans347.app.data.graphById
import com.sans347.app.export.ExportColors
import com.sans347.app.export.ExportPlotPoint
import com.sans347.app.export.ExportShare
import com.sans347.app.export.GraphPngExporter
import com.sans347.app.export.ReportPdfExporter
import com.sans347.app.export.formatCategoryDisplayLabel
import com.sans347.app.ui.graph.AspectFitGraphFrame
import com.sans347.app.ui.graph.LandscapeGraphSidebar
import com.sans347.app.ui.graph.PlotPointData
import com.sans347.app.ui.graph.Sans347Graph
import com.sans347.app.ui.graph.ZoomableGraphBox
import com.sans347.app.ui.theme.SansColors
import com.sans347.app.ui.theme.colorFromHex
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

fun fmtNum(v: Double): String = String.format(Locale.US, "%,.0f", v).replace(',', ' ')

@Composable
fun ResultsScreen(
    result: ResultData?,
    onBackToInput: () -> Unit,
    immersiveGraph: Boolean,
    modifier: Modifier = Modifier,
) {
    val ctx = LocalContext.current
    if (result == null) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(SansColors.White)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text("No calculation yet.", fontSize = 16.sp, color = SansColors.Gray500)
            Spacer(Modifier.height(16.dp))
            Button(onClick = onBackToInput, colors = ButtonDefaults.buttonColors(containerColor = SansColors.PrimaryCyan)) {
                Text("Go to Home")
            }
        }
        return
    }

    val graph = graphById(result.figureId) ?: return
    val catColor = colorFromHex(getCategoryColorHex(result.category))
    val catRisk = getCategoryRisk(result.category)
    val conformity = getConformityModules(result.category)
    val isPiping = graph.xVariable == "DN"
    val productLabel = if (isPiping) {
        "PS×DN = ${fmtNum(result.ps)} × ${fmtNum(result.vOrDn)} = ${fmtNum(result.product)} kPa·DN"
    } else {
        "PS×V = ${fmtNum(result.ps)} × ${fmtNum(result.vOrDn)} = ${fmtNum(result.product)} kPa·L"
    }

    var showResultCard by remember { mutableStateOf(true) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val plotPoint = ExportPlotPoint(
        x = result.vOrDn,
        y = result.ps,
        colorHex = ExportColors.composeToHex(catColor),
    )

    val onExportPng: () -> Unit = {
        scope.launch {
            try {
                val file = withContext(Dispatchers.IO) {
                    GraphPngExporter.exportToCache(ctx, graph, plotPoint)
                }
                ExportShare.shareFile(ctx, file, "image/png", "Export graph (PNG)")
                snackbarHostState.showSnackbar("Graph exported as PNG")
            } catch (_: Exception) {
                snackbarHostState.showSnackbar("PNG export failed")
            }
        }
    }

    val onExportPdf: () -> Unit = {
        scope.launch {
            try {
                val file = withContext(Dispatchers.IO) {
                    ReportPdfExporter.exportToCache(ctx, result, graph, catRisk, conformity, plotPoint)
                }
                ExportShare.shareFile(ctx, file, "application/pdf", "Export report (PDF)")
                snackbarHostState.showSnackbar("Report exported as PDF")
            } catch (_: Exception) {
                snackbarHostState.showSnackbar("PDF export failed")
            }
        }
    }

    if (immersiveGraph) {
        Box(modifier = modifier.fillMaxSize()) {
            ResultsImmersiveLayout(
                graph = graph,
                result = result,
                catColor = catColor,
                productLabel = productLabel,
                showResultCard = showResultCard,
                onToggleCard = { showResultCard = !showResultCard },
                onBackToInput = onBackToInput,
                onExportPng = onExportPng,
                onExportPdf = onExportPdf,
                modifier = Modifier.fillMaxSize(),
            )
            SnackbarHost(
                hostState = snackbarHostState,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
            )
        }
        return
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SansColors.White),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 100.dp)
                .widthIn(max = 896.dp)
                .align(Alignment.TopCenter),
        ) {
            Column(Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SansColors.Gray50, RoundedCornerShape(12.dp))
                        .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, null, tint = SansColors.Success, modifier = Modifier.size(24.dp))
                        Spacer(Modifier.size(8.dp))
                        Text("Calculation Results", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                    Button(
                        onClick = onBackToInput,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SansColors.Gray700),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(14.dp))
                        Spacer(Modifier.size(4.dp))
                        Text("Back to Input", fontSize = 14.sp)
                    }
                }

                Spacer(Modifier.height(16.dp))

                CategoryResultCard(result, catColor, catRisk)

                Spacer(Modifier.height(16.dp))

                GraphResultSection(
                    graph = graph,
                    result = result,
                    catColor = catColor,
                    productLabel = productLabel,
                    showResultCard = showResultCard,
                    onToggleCard = { showResultCard = !showResultCard },
                )

                Spacer(Modifier.height(16.dp))

                SummaryCard(result, graph, isPiping)

                Spacer(Modifier.height(16.dp))

                ImportantNotesCard(category = result.category)

                if (result.category != "SEP" && result.category != "Not regulated") {
                    Spacer(Modifier.height(16.dp))
                    ConformityCard(conformity)
                }

                Spacer(Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        onClick = onBackToInput,
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = SansColors.Gray700),
                        border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(16.dp))
                        Spacer(Modifier.size(8.dp))
                        Text("New Calculation")
                    }
                }

                Spacer(Modifier.height(12.dp))

                ResultsExportButtons(
                    onExportPng = onExportPng,
                    onExportPdf = onExportPdf,
                    compact = false,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp),
        )
    }
}

@Composable
private fun CategoryResultCard(result: ResultData, catColor: Color, catRisk: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SansColors.White, RoundedCornerShape(12.dp))
            .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Your Equipment Category", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.Black)
        Spacer(Modifier.height(16.dp))
        val circleTextSize = when {
            result.category.length <= 3 -> 48.sp
            result.category.length <= 5 -> 24.sp
            else -> 14.sp
        }
        Box(
            modifier = Modifier
                .size(128.dp)
                .background(catColor, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                result.category,
                color = SansColors.White,
                fontWeight = FontWeight.Bold,
                fontSize = circleTextSize,
                modifier = Modifier.padding(4.dp),
            )
        }
        Spacer(Modifier.height(12.dp))
        Text(
            if (result.category == "Not regulated") "Not Regulated" else "Category ${result.category}",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = catColor,
        )
        Text(catRisk, color = SansColors.Gray500, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun ResultsImmersiveLayout(
    graph: GraphConfig,
    result: ResultData,
    catColor: Color,
    productLabel: String,
    showResultCard: Boolean,
    onToggleCard: () -> Unit,
    onBackToInput: () -> Unit,
    onExportPng: () -> Unit,
    onExportPdf: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxSize()
            .background(SansColors.White)
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        LandscapeGraphSidebar(
            graph = graph,
            headerContent = {
                Button(
                    onClick = onBackToInput,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SansColors.Gray700),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = true),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null, modifier = Modifier.size(14.dp))
                    Spacer(Modifier.size(6.dp))
                    Text("Back", fontSize = 12.sp)
                }
            },
            footerContent = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    ResultsSidebarResultSection(
                        result = result,
                        catColor = catColor,
                        productLabel = productLabel,
                        showResultCard = showResultCard,
                        onToggleCard = onToggleCard,
                    )
                    Spacer(Modifier.height(12.dp))
                    ResultsExportButtons(
                        onExportPng = onExportPng,
                        onExportPdf = onExportPdf,
                        compact = true,
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            },
        )

        AspectFitGraphFrame(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .padding(start = 8.dp),
        ) {
            ZoomableGraphBox(modifier = Modifier.fillMaxSize()) {
                Sans347Graph(
                    config = graph,
                    plotPoint = PlotPointData(result.vOrDn, result.ps, catColor),
                    fitContainer = true,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun ResultsSidebarResultSection(
    result: ResultData,
    catColor: Color,
    productLabel: String,
    showResultCard: Boolean,
    onToggleCard: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onToggleCard),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            "Your result",
            fontSize = 10.sp,
            color = SansColors.Gray400,
            fontWeight = FontWeight.Medium,
        )
        Spacer(Modifier.height(8.dp))
        if (showResultCard) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .background(catColor, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        result.category,
                        color = SansColors.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = if (result.category.length <= 3) 11.sp else 7.sp,
                    )
                }
                Spacer(Modifier.size(8.dp))
                Text(
                    formatCategoryDisplayLabel(result.category),
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = catColor,
                )
            }
            Spacer(Modifier.height(8.dp))
            Text(
                productLabel,
                fontSize = 10.sp,
                color = SansColors.Gray700,
                lineHeight = 14.sp,
            )
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(catColor, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        result.category,
                        color = SansColors.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 7.sp,
                    )
                }
                Spacer(Modifier.size(8.dp))
                Text("Tap to expand", fontSize = 10.sp, color = SansColors.Gray500)
            }
        }
    }
}

@Composable
private fun ResultsExportButtons(
    onExportPng: () -> Unit,
    onExportPdf: () -> Unit,
    compact: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Button(
            onClick = onExportPng,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = SansColors.PrimaryCyan),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = if (compact) 6.dp else 10.dp),
        ) {
            Icon(Icons.Default.BarChart, null, modifier = Modifier.size(if (compact) 14.dp else 16.dp))
            Spacer(Modifier.size(if (compact) 4.dp else 8.dp))
            Text(
                if (compact) "PNG" else "Export graph (PNG)",
                color = SansColors.White,
                fontSize = if (compact) 11.sp else 14.sp,
            )
        }
        Button(
            onClick = onExportPdf,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = SansColors.Gray700),
            border = ButtonDefaults.outlinedButtonBorder(enabled = true),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = if (compact) 6.dp else 10.dp),
        ) {
            Icon(Icons.Default.Description, null, modifier = Modifier.size(if (compact) 14.dp else 16.dp))
            Spacer(Modifier.size(if (compact) 4.dp else 8.dp))
            Text(
                if (compact) "PDF" else "Export report (PDF)",
                fontSize = if (compact) 11.sp else 14.sp,
            )
        }
    }
}

@Composable
private fun GraphResultOverlay(
    result: ResultData,
    catColor: Color,
    productLabel: String,
    showResultCard: Boolean,
    onToggleCard: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .clickable(onClick = onToggleCard)
            .background(SansColors.White, RoundedCornerShape(8.dp))
            .border(1.dp, SansColors.PrimaryCyan, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 6.dp),
    ) {
        if (showResultCard) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(catColor, CircleShape),
                    )
                    Spacer(Modifier.size(4.dp))
                    Text("Your Result", fontWeight = FontWeight.Bold, fontSize = 10.sp)
                }
                Text(productLabel, fontSize = 9.sp, color = SansColors.Gray700, lineHeight = 12.sp)
            }
        } else {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(14.dp)
                        .background(catColor, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        result.category,
                        color = SansColors.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 6.sp,
                    )
                }
                Spacer(Modifier.size(4.dp))
                Text("Result", fontWeight = FontWeight.Bold, fontSize = 9.sp, color = SansColors.Gray600)
            }
        }
    }
}

@Composable
private fun GraphResultSection(
    graph: GraphConfig,
    result: ResultData,
    catColor: Color,
    productLabel: String,
    showResultCard: Boolean,
    onToggleCard: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SansColors.White, RoundedCornerShape(12.dp))
            .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 12.dp)) {
            Icon(Icons.Default.BarChart, null, tint = SansColors.Gray500, modifier = Modifier.size(18.dp))
            Spacer(Modifier.size(8.dp))
            Text("Applicable Categorization Graph", fontWeight = FontWeight.Bold, color = Color.Black)
        }
        HorizontalDivider(color = SansColors.Gray200)
        Spacer(Modifier.height(12.dp))
        Text(
            "${graph.title} — ${graph.subtitle}",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.fillMaxWidth(),
        )
        Text("Figure ${graph.id}", color = SansColors.PrimaryCyan, fontSize = 14.sp)

        Spacer(Modifier.height(8.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            ZoomableGraphBox(modifier = Modifier.fillMaxWidth()) {
                Sans347Graph(
                    config = graph,
                    plotPoint = PlotPointData(result.vOrDn, result.ps, catColor),
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            GraphResultOverlay(
                result = result,
                catColor = catColor,
                productLabel = productLabel,
                showResultCard = showResultCard,
                onToggleCard = onToggleCard,
                modifier = Modifier.align(Alignment.TopEnd),
            )
        }

        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SansColors.ApplicationBg, RoundedCornerShape(8.dp))
                .border(1.dp, SansColors.ApplicationBorder, RoundedCornerShape(8.dp))
                .padding(12.dp),
        ) {
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = SansColors.ApplicationLabel)) {
                        append("Application:")
                    }
                    append(" ")
                    withStyle(SpanStyle(color = SansColors.Gray700)) {
                        append(graph.applicationText)
                    }
                },
                fontSize = 14.sp,
            )
        }
    }
}

@Composable
private fun SummaryCard(result: ResultData, graph: GraphConfig, isPiping: Boolean) {
    val equipLabel = if (graph.equipmentType == "Pressure Vessels") "Vessels" else graph.equipmentType
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SansColors.White, RoundedCornerShape(12.dp))
            .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Text("Input Parameters", fontWeight = FontWeight.Bold, color = Color.Black, modifier = Modifier.padding(bottom = 12.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Equipment Type:", color = SansColors.Gray500, fontSize = 14.sp)
            Text(equipLabel, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp), color = SansColors.Gray200)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Design Pressure:", color = SansColors.Gray500, fontSize = 14.sp)
            Text("${fmtNum(result.ps)} kPa", color = SansColors.PrimaryCyan, fontWeight = FontWeight.Medium, fontSize = 14.sp)
        }
        val isSteamGen = result.equipmentType == "Steam Generator"
        if (!isSteamGen) {
            HorizontalDivider(Modifier.padding(vertical = 8.dp), color = SansColors.Gray200)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("State Contents:", color = SansColors.Gray500, fontSize = 14.sp)
                Text(result.stateOfContents, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }
        }
        HorizontalDivider(Modifier.padding(vertical = 8.dp), color = SansColors.Gray200)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(if (isPiping) "Diameter:" else "Volume:", color = SansColors.Gray500, fontSize = 14.sp)
            Text(
                "${fmtNum(result.vOrDn)} ${if (isPiping) "DN" else "L"}",
                color = SansColors.PrimaryCyan,
                fontWeight = FontWeight.Medium,
                fontSize = 14.sp,
            )
        }
        if (!isSteamGen) {
            HorizontalDivider(Modifier.padding(vertical = 8.dp), color = SansColors.Gray200)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Fluid Group:", color = SansColors.Gray500, fontSize = 14.sp)
                Text(result.fluidGroup, fontWeight = FontWeight.Medium, fontSize = 14.sp)
            }
        }
    }
}

private fun requiresPrEng(category: String): Boolean =
    category in listOf("II", "III", "IV")

@Composable
private fun ImportantNotesCard(category: String) {
    val prEngRequired = requiresPrEng(category)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SansColors.White, RoundedCornerShape(12.dp))
            .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp)),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(SansColors.WarningBg)
                .border(1.dp, SansColors.WarningBorder, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(Icons.Default.Warning, null, tint = SansColors.WarningIcon, modifier = Modifier.size(18.dp))
            Spacer(Modifier.size(8.dp))
            Text("Important Notes", fontWeight = FontWeight.Bold)
        }
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) { append("Note:") }
                    append(" This categorization is based on SANS 347:2024 Edition 3.1 standards.")
                },
                fontSize = 14.sp,
                color = SansColors.Gray700,
            )
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) { append("Compliance:") }
                    append(" All pressure equipment must comply with the applicable conformity assessment procedures for the determined category.")
                },
                fontSize = 14.sp,
                color = SansColors.Gray700,
            )
            Text(
                buildAnnotatedString {
                    withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) { append("Professional Review:") }
                    if (prEngRequired) {
                        append(" ")
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = SansColors.PrimaryCyan)) {
                            append("Pr Eng sign-off required.")
                        }
                        append(" Category $category pressure equipment must be signed off by a Professional Registered Engineer (Pr Eng) in terms of the Engineering Profession Act.")
                    } else {
                        append(" No Pr Eng sign-off required for this category. It is still recommended to have these results reviewed by a qualified pressure equipment engineer.")
                    }
                },
                fontSize = 14.sp,
                color = SansColors.Gray700,
            )
        }
    }
}

@Composable
private fun ConformityCard(conformity: ConformityModules) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SansColors.White, RoundedCornerShape(12.dp))
            .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Text("Required Conformity Assessment Modules", fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 12.dp))
        Text("Manufacturer without Certified Quality System", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = SansColors.Gray700)
        Text(
            conformity.withoutQuality,
            fontSize = 14.sp,
            color = SansColors.Gray700,
            modifier = Modifier
                .fillMaxWidth()
                .background(SansColors.Gray50, RoundedCornerShape(8.dp))
                .border(1.dp, SansColors.Gray200, RoundedCornerShape(8.dp))
                .padding(12.dp),
        )
        Spacer(Modifier.height(12.dp))
        Text("Manufacturer with Certified Quality System", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = SansColors.Gray700)
        Text(
            conformity.withQuality,
            fontSize = 14.sp,
            color = SansColors.Gray700,
            modifier = Modifier
                .fillMaxWidth()
                .background(SansColors.Gray50, RoundedCornerShape(8.dp))
                .border(1.dp, SansColors.Gray200, RoundedCornerShape(8.dp))
                .padding(12.dp),
        )
    }
}
