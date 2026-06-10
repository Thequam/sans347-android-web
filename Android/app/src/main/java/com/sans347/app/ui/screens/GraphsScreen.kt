package com.sans347.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sans347.app.data.GraphConfig
import com.sans347.app.data.allGraphs
import com.sans347.app.ui.graph.AspectFitGraphFrame
import com.sans347.app.ui.graph.LandscapeFigureNavRow
import com.sans347.app.ui.graph.LandscapeGraphSidebar
import com.sans347.app.ui.graph.Sans347Graph
import com.sans347.app.ui.graph.ZoomableGraphBox
import com.sans347.app.ui.theme.SansColors

@Composable
fun GraphsScreen(
    currentIndex: Int,
    onIndexChange: (Int) -> Unit,
    immersiveGraph: Boolean,
    modifier: Modifier = Modifier,
) {
    val graph = allGraphs[currentIndex]

    if (immersiveGraph) {
        GraphsImmersiveLayout(
            graph = graph,
            currentIndex = currentIndex,
            onIndexChange = onIndexChange,
            modifier = modifier,
        )
        return
    }

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
            .background(SansColors.White),
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 896.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 100.dp),
        ) {
            GraphHeaderCard(graph, currentIndex, onIndexChange)

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SansColors.White, RoundedCornerShape(12.dp))
                    .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
                    .padding(8.dp),
            ) {
                ZoomableGraphBox(modifier = Modifier.fillMaxWidth()) {
                    Sans347Graph(config = graph, modifier = Modifier.fillMaxWidth())
                }
            }

            Spacer(Modifier.height(16.dp))

            Text(
                graph.applicationText,
                fontSize = 14.sp,
                color = SansColors.Gray700,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SansColors.White, RoundedCornerShape(12.dp))
                    .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
                    .padding(16.dp),
            )

            Spacer(Modifier.height(12.dp))

            Text(
                graph.footerText,
                fontSize = 14.sp,
                color = SansColors.Gray400,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                allGraphs.forEachIndexed { i, g ->
                    ThumbnailButton(
                        graph = g,
                        selected = i == currentIndex,
                        onClick = { onIndexChange(i) },
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            CategoryLegendCard()
        }
    }
}

@Composable
private fun GraphsImmersiveLayout(
    graph: GraphConfig,
    currentIndex: Int,
    onIndexChange: (Int) -> Unit,
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
            navContent = {
                LandscapeFigureNavRow(
                    currentIndex = currentIndex,
                    onIndexChange = onIndexChange,
                )
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
                    fitContainer = true,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun GraphHeaderCard(
    graph: GraphConfig,
    currentIndex: Int,
    onIndexChange: (Int) -> Unit,
    compact: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                if (compact) SansColors.White.copy(alpha = 0.92f) else SansColors.Gray50,
                RoundedCornerShape(12.dp),
            )
            .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
            .padding(if (compact) 8.dp else 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (!compact) {
            Text(
                "${currentIndex + 1} of 9",
                fontSize = 14.sp,
                color = SansColors.Gray400,
                modifier = Modifier
                    .align(Alignment.End)
                    .background(SansColors.Gray200, RoundedCornerShape(999.dp))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            )
        }
        Text(
            graph.title,
            fontWeight = FontWeight.Bold,
            fontSize = if (compact) 14.sp else 18.sp,
            color = Color.Black,
            textAlign = TextAlign.Center,
        )
        if (graph.subtitle.isNotEmpty()) {
            Text(
                graph.subtitle,
                color = SansColors.PrimaryCyan,
                fontWeight = FontWeight.Medium,
                fontSize = if (compact) 12.sp else 14.sp,
                textAlign = TextAlign.Center,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = if (compact) 4.dp else 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                "Figure ${graph.id}${if (compact) " · ${currentIndex + 1}/9" else ""}",
                color = SansColors.Gray500,
                fontSize = if (compact) 12.sp else 14.sp,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                val canPrev = currentIndex > 0
                val canNext = currentIndex < 8
                Button(
                    onClick = { onIndexChange(currentIndex - 1) },
                    enabled = canPrev,
                    modifier = Modifier.alpha(if (canPrev) 1f else 0.3f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SansColors.Gray700),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = canPrev),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null)
                    if (!compact) {
                        Text("Prev", fontSize = 12.sp)
                    }
                }
                Button(
                    onClick = { onIndexChange(currentIndex + 1) },
                    enabled = canNext,
                    modifier = Modifier.alpha(if (canNext) 1f else 0.3f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = SansColors.Gray700),
                    border = ButtonDefaults.outlinedButtonBorder(enabled = canNext),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                ) {
                    if (!compact) {
                        Text("Next", fontSize = 12.sp)
                    }
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null)
                }
            }
        }
    }
}

@Composable
private fun ThumbnailButton(
    graph: GraphConfig,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val abbr = when (graph.equipmentType) {
        "Pressure Vessels" -> "Vessels"
        "Steam Generator" -> "Steam Gen"
        else -> "Piping"
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .border(
                width = 2.dp,
                color = if (selected) SansColors.PrimaryCyan else SansColors.Gray200,
                shape = RoundedCornerShape(8.dp),
            )
            .background(if (selected) SansColors.ThumbSelectedBg else SansColors.White, RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 4.dp, vertical = 6.dp),
    ) {
        Text(
            "Fig ${graph.id}",
            fontWeight = FontWeight.Bold,
            fontSize = 9.sp,
            color = if (selected) SansColors.PrimaryCyan else SansColors.Gray700,
            textAlign = TextAlign.Center,
        )
        Text(
            abbr,
            fontSize = 7.sp,
            color = SansColors.Gray400,
            maxLines = 1,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CategoryLegendCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SansColors.White, RoundedCornerShape(12.dp))
            .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Category Legend", fontWeight = FontWeight.Bold, color = Color.Black, textAlign = TextAlign.Center, modifier = Modifier.padding(bottom = 12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf(
                Triple("SEP", SansColors.Success, "Sound Engineering Practice"),
                Triple("I", SansColors.CategoryI, "Category I"),
                Triple("II", SansColors.CategoryII, "Category II"),
                Triple("III", SansColors.CategoryIII, "Category III"),
                Triple("IV", SansColors.CategoryIV, "Category IV"),
            ).forEach { (label, color, desc) ->
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        label,
                        color = SansColors.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color, RoundedCornerShape(8.dp))
                            .padding(vertical = 8.dp),
                    )
                    Text(desc, fontSize = 10.sp, color = SansColors.Gray500, textAlign = TextAlign.Center, modifier = Modifier.padding(top = 4.dp))
                }
            }
        }
    }
}
