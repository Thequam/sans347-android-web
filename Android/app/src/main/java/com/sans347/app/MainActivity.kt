package com.sans347.app

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sans347.app.ui.components.BottomNavBar
import com.sans347.app.ui.components.CollapsibleHeader
import com.sans347.app.ui.screens.GraphsScreen
import com.sans347.app.ui.screens.HomeScreen
import com.sans347.app.ui.screens.ResultsScreen
import com.sans347.app.ui.screens.TablesScreen
import com.sans347.app.ui.theme.Sans347Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Sans347Theme {
                val vm: Sans347ViewModel = viewModel()
                val state by vm.state.collectAsState()
                val config = LocalConfiguration.current
                val landscape = config.orientation == Configuration.ORIENTATION_LANDSCAPE
                val graphImmersive = landscape && state.currentPage in listOf(2, 3)
                val inputHeight = if (landscape) 56.dp else 48.dp

                Column(Modifier.fillMaxSize()) {
                    if (!graphImmersive) {
                        CollapsibleHeader(
                            showHeader = state.showHeader,
                            onToggleHeader = { vm.toggleHeader() },
                            onTables = { vm.setCurrentPage(0) },
                            onHomeTitle = { vm.setCurrentPage(1) },
                            onGraphs = { vm.setCurrentPage(2) },
                        )
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                    ) {
                        when (state.currentPage) {
                            0 -> TablesScreen(Modifier.fillMaxSize())
                            1 -> HomeScreen(
                                state = state,
                                onEquipmentType = vm::setEquipmentType,
                                onStateOfContents = vm::setStateOfContents,
                                onFluidGroup = vm::setFluidGroup,
                                onDesignPressure = vm::setDesignPressure,
                                onVolumeOrDiameter = vm::setVolumeOrDiameter,
                                onCalculate = { vm.calculateAndGoToResults() },
                                onClear = { vm.clearAll() },
                                onViewResults = { vm.setCurrentPage(3) },
                                inputFieldHeight = inputHeight,
                                modifier = Modifier.fillMaxSize(),
                            )
                            2 -> GraphsScreen(
                                currentIndex = state.currentGraphIndex,
                                onIndexChange = vm::setCurrentGraphIndex,
                                immersiveGraph = graphImmersive,
                                modifier = Modifier.fillMaxSize(),
                            )
                            3 -> ResultsScreen(
                                result = state.result,
                                onBackToInput = { vm.setCurrentPage(1) },
                                immersiveGraph = graphImmersive,
                                modifier = Modifier.fillMaxSize(),
                            )
                        }
                    }
                    if (!graphImmersive) {
                        BottomNavBar(
                            currentPage = state.currentPage,
                            onSelect = { vm.setCurrentPage(it) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .navigationBarsPadding(),
                        )
                    }
                }
            }
        }
    }
}
