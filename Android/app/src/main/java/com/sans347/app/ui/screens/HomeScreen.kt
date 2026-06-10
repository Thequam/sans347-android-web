package com.sans347.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.sans347.app.SansUiState
import com.sans347.app.formValid
import com.sans347.app.ui.theme.SansColors

@Composable
fun HomeScreen(
    state: SansUiState,
    onEquipmentType: (String?) -> Unit,
    onStateOfContents: (String?) -> Unit,
    onFluidGroup: (String?) -> Unit,
    onDesignPressure: (String) -> Unit,
    onVolumeOrDiameter: (String) -> Unit,
    onCalculate: () -> Unit,
    onClear: () -> Unit,
    onViewResults: () -> Unit,
    inputFieldHeight: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    val isSteamGen = state.equipmentType == "Steam Generator"
    val isPiping = state.equipmentType == "Piping"
    val volumeLabel = if (isPiping) "Diameter" else "Volume"
    val volumeUnit = if (isPiping) "DN" else "L"
    val volumePlaceholder = if (isPiping) "Enter diameter" else "Enter vol..."
    val volumeDesc = if (isPiping) "Nominal pipe diameter" else "Internal volume capacity"
    val formValid = state.formValid()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(SansColors.PageBackground),
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 672.dp)
                .align(Alignment.TopCenter)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = 12.dp, bottom = 100.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SansColors.MediumGray, RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .padding(horizontal = 12.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    "Pressure Equipment\nCategorization",
                    color = SansColors.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 32.sp,
                )
                Text(
                    "Determine the appropriate category and conformity assessment requirements for your pressure equipment according to SANS 347:2024",
                    color = SansColors.PrimaryCyan,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SansColors.White, RoundedCornerShape(12.dp))
                    .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
                    .padding(16.dp),
            ) {
                Box(Modifier.fillMaxWidth()) {
                    Column(
                        Modifier.align(Alignment.Center).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text("Equipment Type", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(
                            "Select the type of pressure equipment",
                            color = SansColors.Gray500,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(bottom = 12.dp),
                        )
                    }
                    SteamGenToggle(
                        selected = isSteamGen,
                        onClick = {
                            if (isSteamGen) onEquipmentType(null) else onEquipmentType("Steam Generator")
                        },
                        modifier = Modifier.align(Alignment.TopEnd),
                    )
                }
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    SelectButton(
                        selected = state.equipmentType == "Pressure Vessels",
                        onClick = { onEquipmentType("Pressure Vessels") },
                        title = "Pressure Vessels",
                        subtitle = "Tanks, containers, boilers",
                        modifier = Modifier.weight(1f),
                    )
                    SelectButton(
                        selected = state.equipmentType == "Piping",
                        onClick = { onEquipmentType("Piping") },
                        title = "Piping",
                        subtitle = "Pipes, fittings, assemblies",
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            if (!isSteamGen) {
                Spacer(Modifier.height(16.dp))

                SelectionCard(
                    title = "State of Contents",
                    subtitle = "What state will the fluid be in?",
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SelectButton(
                            selected = state.stateOfContents == "Gas",
                            onClick = { onStateOfContents("Gas") },
                            title = "Gas",
                            subtitle = "Gaseous state",
                            modifier = Modifier.weight(1f),
                        )
                        SelectButton(
                            selected = state.stateOfContents == "Liquid",
                            onClick = { onStateOfContents("Liquid") },
                            title = "Liquid",
                            subtitle = "Liquid state",
                            modifier = Modifier.weight(1f),
                        )
                    }
                }

                Spacer(Modifier.height(16.dp))

                SelectionCard(
                    title = "Fluid Group",
                    subtitle = "Classification based on hazard level",
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        SelectButton(
                            selected = state.fluidGroup == "Dangerous",
                            onClick = { onFluidGroup("Dangerous") },
                            title = "Dangerous",
                            subtitle = "Group 1 - Higher risk",
                            modifier = Modifier.weight(1f),
                        )
                        SelectButton(
                            selected = state.fluidGroup == "Non-Dangerous",
                            onClick = { onFluidGroup("Non-Dangerous") },
                            title = "Non-Dangerous",
                            subtitle = "Group 2 - Lower risk",
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SansColors.White, RoundedCornerShape(12.dp))
                    .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
                    .padding(16.dp),
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    UnitOutlinedField(
                        label = "Design Pressure",
                        description = "Maximum allowable pressure",
                        value = state.designPressure,
                        onValueChange = onDesignPressure,
                        unit = "kPa",
                        placeholder = "Enter pr...",
                        height = inputFieldHeight,
                        modifier = Modifier.weight(1f),
                    )
                    UnitOutlinedField(
                        label = volumeLabel,
                        description = volumeDesc,
                        value = state.volumeOrDiameter,
                        onValueChange = onVolumeOrDiameter,
                        unit = volumeUnit,
                        placeholder = volumePlaceholder,
                        height = inputFieldHeight,
                        modifier = Modifier.weight(1f),
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onCalculate,
                enabled = formValid,
                modifier = Modifier.fillMaxWidth().height(64.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SansColors.PrimaryCyan,
                    disabledContainerColor = SansColors.Gray200,
                    contentColor = SansColors.White,
                    disabledContentColor = SansColors.Gray400,
                ),
            ) {
                Icon(Icons.Default.Calculate, null, modifier = Modifier.padding(end = 8.dp))
                Text("Calculate Category", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onClear,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SansColors.DarkGray),
            ) {
                Icon(Icons.Default.Refresh, null, tint = SansColors.PrimaryCyan, modifier = Modifier.padding(end = 8.dp))
                Text("Clear All", color = SansColors.PrimaryCyan, fontWeight = FontWeight.Medium)
                Text(" Fields", color = SansColors.White, fontWeight = FontWeight.Medium)
            }

            Spacer(Modifier.height(12.dp))

            Button(
                onClick = onViewResults,
                enabled = state.result != null,
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SansColors.Success,
                    disabledContainerColor = SansColors.Gray200,
                    contentColor = SansColors.White,
                    disabledContentColor = SansColors.Gray400,
                ),
            ) {
                Icon(Icons.AutoMirrored.Filled.ListAlt, null, modifier = Modifier.padding(end = 8.dp))
                Text("View Results", fontWeight = FontWeight.Bold)
            }

            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SansColors.Gray50, RoundedCornerShape(12.dp))
                    .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("SANS 347:2024 Edition 3.1", fontWeight = FontWeight.Bold, color = Color.Black, textAlign = TextAlign.Center)
                Text(
                    "Categorization and conformity assessment criteria for all pressure equipment",
                    color = SansColors.Gray500,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp),
                )
                Text(
                    "South African National Standard",
                    color = SansColors.Gray400,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun SelectionCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(SansColors.White, RoundedCornerShape(12.dp))
            .border(1.dp, SansColors.Gray200, RoundedCornerShape(12.dp))
            .padding(16.dp),
    ) {
        Text(title, fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.align(Alignment.CenterHorizontally))
        Text(
            subtitle,
            color = SansColors.Gray500,
            fontSize = 14.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally).padding(bottom = 12.dp),
        )
        content()
    }
}

@Composable
private fun SelectButton(
    selected: Boolean,
    onClick: () -> Unit,
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
) {
    val bg = if (selected) SansColors.PrimaryCyan else SansColors.DarkGray
    val subColor = if (selected) SansColors.White else SansColors.PrimaryCyan
    Column(
        modifier = modifier
            .height(72.dp)
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(12.dp))
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(title, color = SansColors.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
        Text(subtitle, color = subColor, fontSize = 12.sp, modifier = Modifier.padding(top = 2.dp))
    }
}

@Composable
private fun UnitOutlinedField(
    label: String,
    description: String,
    value: String,
    onValueChange: (String) -> Unit,
    unit: String,
    placeholder: String,
    height: androidx.compose.ui.unit.Dp,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color.Black, textAlign = TextAlign.Center)
        Text(
            description,
            color = SansColors.Gray500,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 4.dp, bottom = 8.dp),
            minLines = 2,
            maxLines = 2,
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.fillMaxWidth().height(height),
                placeholder = { Text(placeholder, color = SansColors.Gray400, fontSize = 14.sp) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = SansColors.PrimaryCyan,
                    unfocusedBorderColor = SansColors.Gray200,
                    focusedContainerColor = SansColors.White,
                    unfocusedContainerColor = SansColors.White,
                ),
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 12.dp)
                    .background(SansColors.PrimaryCyan, RoundedCornerShape(50))
                    .padding(horizontal = 8.dp, vertical = 4.dp),
            ) {
                Text(unit, color = SansColors.White, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun SteamGenToggle(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val bg = if (selected) SansColors.PrimaryCyan else SansColors.Gray200
    val textColor = if (selected) SansColors.White else SansColors.Gray500
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(bg)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick,
            )
            .padding(horizontal = 6.dp, vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Box(
            modifier = Modifier
                .size(5.dp)
                .background(
                    if (selected) SansColors.White else SansColors.Gray400,
                    CircleShape,
                ),
        )
        Text(
            "Steam",
            color = textColor,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
