package com.sans347.app.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sans347.app.ui.theme.SansColors

@Composable
fun CollapsibleHeader(
    showHeader: Boolean,
    onToggleHeader: () -> Unit,
    onTables: () -> Unit,
    onHomeTitle: () -> Unit,
    onGraphs: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SansColors.DarkBackground)
            .statusBarsPadding()
            .animateContentSize(),
    ) {
        if (showHeader) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Row(
                    modifier = Modifier
                        .clickable(onClick = onTables)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(Icons.Default.Description, null, tint = SansColors.Gray300, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("Tables", color = SansColors.Gray300, fontSize = 14.sp)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(onClick = onHomeTitle)
                        .padding(horizontal = 8.dp),
                ) {
                    Text("SANS 347", color = SansColors.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("2024 3rd Edition", color = SansColors.PrimaryCyan, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
                Row(
                    modifier = Modifier
                        .clickable(onClick = onGraphs)
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text("Graphs", color = SansColors.Gray300, fontSize = 14.sp)
                    Spacer(Modifier.width(4.dp))
                    Icon(Icons.Default.BarChart, null, tint = SansColors.Gray300, modifier = Modifier.size(16.dp))
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = onToggleHeader)
                .padding(vertical = 2.dp),
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = if (showHeader) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                contentDescription = null,
                tint = SansColors.Gray500,
                modifier = Modifier.size(14.dp),
            )
        }
    }
}

@Composable
fun BottomNavBar(
    currentPage: Int,
    onSelect: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth().background(SansColors.DarkBackground)) {
        HorizontalDivider(color = SansColors.NavBorder, thickness = 1.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp, bottom = 6.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            NavItem(1, currentPage, "Home", Icons.Default.Home, onSelect)
            NavItem(3, currentPage, "Results", Icons.AutoMirrored.Filled.ListAlt, onSelect)
            NavItem(2, currentPage, "Graphs", Icons.Default.BarChart, onSelect)
            NavItem(0, currentPage, "Tables", Icons.Default.Description, onSelect)
        }
    }
}

@Composable
private fun NavItem(
    page: Int,
    currentPage: Int,
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    onSelect: (Int) -> Unit,
) {
    val active = currentPage == page
    val color = if (active) SansColors.PrimaryCyan else SansColors.Gray500
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .widthIn(min = 60.dp)
            .clickable { onSelect(page) }
            .padding(horizontal = 8.dp, vertical = 4.dp),
    ) {
        Icon(icon, null, tint = color, modifier = Modifier.size(20.dp))
        Text(label, color = color, fontSize = 10.sp, fontWeight = FontWeight.Medium)
    }
}
