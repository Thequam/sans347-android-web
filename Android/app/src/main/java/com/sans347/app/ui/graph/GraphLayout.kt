package com.sans347.app.ui.graph

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import com.sans347.app.ui.theme.SansColors

const val GRAPH_ASPECT_RATIO = 750f / 520f

private val landscapeSidebarWidth = 140.dp

@Composable
fun LandscapeGraphSidebar(
    graph: GraphConfig,
    modifier: Modifier = Modifier,
    headerContent: @Composable (() -> Unit)? = null,
    navContent: @Composable (() -> Unit)? = null,
    footerContent: @Composable (() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .width(landscapeSidebarWidth)
            .fillMaxHeight()
            .background(SansColors.Gray50, RoundedCornerShape(12.dp))
            .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        headerContent?.invoke()
        if (headerContent != null) {
            Spacer(Modifier.height(12.dp))
        }

        Text(
            graph.title,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Color.Black,
            lineHeight = 19.sp,
        )
        if (graph.subtitle.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(
                graph.subtitle,
                color = SansColors.PrimaryCyan,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                lineHeight = 16.sp,
            )
        }
        Spacer(Modifier.height(10.dp))
        Text(
            "Figure ${graph.id}",
            color = SansColors.Gray500,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
        )

        if (navContent != null) {
            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = SansColors.Gray200)
            Spacer(Modifier.height(12.dp))
            navContent()
        }

        Spacer(Modifier.weight(1f))

        if (footerContent != null) {
            HorizontalDivider(color = SansColors.Gray200)
            Spacer(Modifier.height(12.dp))
            footerContent()
        }
    }
}

@Composable
fun LandscapeFigureNavRow(
    currentIndex: Int,
    onIndexChange: (Int) -> Unit,
) {
    val canPrev = currentIndex > 0
    val canNext = currentIndex < 8

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            "Browse figures",
            fontSize = 10.sp,
            color = SansColors.Gray400,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
        )
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = { onIndexChange(currentIndex - 1) },
                enabled = canPrev,
                modifier = Modifier
                    .alpha(if (canPrev) 1f else 0.35f)
                    .weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SansColors.Gray700),
                border = ButtonDefaults.outlinedButtonBorder(enabled = canPrev),
                contentPadding = PaddingValues(vertical = 6.dp),
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, contentDescription = "Previous figure")
            }
            Text(
                "${currentIndex + 1} / 9",
                fontSize = 12.sp,
                color = SansColors.Gray700,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 8.dp),
                textAlign = TextAlign.Center,
            )
            Button(
                onClick = { onIndexChange(currentIndex + 1) },
                enabled = canNext,
                modifier = Modifier
                    .alpha(if (canNext) 1f else 0.35f)
                    .weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = SansColors.Gray700),
                border = ButtonDefaults.outlinedButtonBorder(enabled = canNext),
                contentPadding = PaddingValues(vertical = 6.dp),
            ) {
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = "Next figure")
            }
        }
    }
}

@Composable
fun AspectFitGraphFrame(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    BoxWithConstraints(modifier = modifier) {
        val fitWidth = minOf(maxWidth, maxHeight * GRAPH_ASPECT_RATIO)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(fitWidth)
                .aspectRatio(GRAPH_ASPECT_RATIO),
        ) {
            content()
        }
    }
}
