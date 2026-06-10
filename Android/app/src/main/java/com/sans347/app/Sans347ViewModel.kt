package com.sans347.app

import androidx.lifecycle.ViewModel
import com.sans347.app.data.ResultData
import com.sans347.app.data.determineCategory
import com.sans347.app.data.selectFigure
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class SansUiState(
    val currentPage: Int = 1,
    val showHeader: Boolean = true,
    val equipmentType: String? = null,
    val stateOfContents: String? = null,
    val fluidGroup: String? = null,
    val designPressure: String = "",
    val volumeOrDiameter: String = "",
    val currentGraphIndex: Int = 0,
    val result: ResultData? = null,
)

class Sans347ViewModel : ViewModel() {

    private val _state = MutableStateFlow(SansUiState())
    val state: StateFlow<SansUiState> = _state.asStateFlow()

    fun setCurrentPage(page: Int) {
        _state.update { it.copy(currentPage = page) }
    }

    fun toggleHeader() {
        _state.update { it.copy(showHeader = !it.showHeader) }
    }

    fun setEquipmentType(value: String?) {
        _state.update { it.copy(equipmentType = value) }
    }

    fun setStateOfContents(value: String?) {
        _state.update { it.copy(stateOfContents = value) }
    }

    fun setFluidGroup(value: String?) {
        _state.update { it.copy(fluidGroup = value) }
    }

    fun setDesignPressure(value: String) {
        _state.update { it.copy(designPressure = value) }
    }

    fun setVolumeOrDiameter(value: String) {
        _state.update { it.copy(volumeOrDiameter = value) }
    }

    fun setCurrentGraphIndex(index: Int) {
        _state.update { it.copy(currentGraphIndex = index.coerceIn(0, 8)) }
    }

    fun clearAll() {
        _state.update {
            SansUiState(
                currentPage = it.currentPage,
                showHeader = it.showHeader,
                currentGraphIndex = it.currentGraphIndex,
            )
        }
    }

    fun calculateAndGoToResults() {
        val s = _state.value
        val eq = s.equipmentType ?: return
        val ps = s.designPressure.toDoubleOrNull() ?: return
        val v = s.volumeOrDiameter.toDoubleOrNull() ?: return
        if (ps <= 0 || v <= 0) return

        val figureId: Int
        val st: String
        val fg: String
        if (eq == "Steam Generator") {
            figureId = 5
            st = ""
            fg = ""
        } else {
            st = s.stateOfContents ?: return
            fg = s.fluidGroup ?: return
            figureId = selectFigure(eq, st, fg)
        }

        val category = determineCategory(figureId, ps, v)
        val product = ps * v
        _state.update {
            it.copy(
                result = ResultData(
                    category = category,
                    figureId = figureId,
                    product = product,
                    ps = ps,
                    vOrDn = v,
                    equipmentType = eq,
                    stateOfContents = st,
                    fluidGroup = fg,
                ),
                currentPage = 3,
            )
        }
    }
}

fun SansUiState.formValid(): Boolean {
    val ps = designPressure.toDoubleOrNull()
    val v = volumeOrDiameter.toDoubleOrNull()
    if (equipmentType == "Steam Generator") {
        return ps != null && v != null && ps > 0 && v > 0
    }
    return equipmentType != null &&
        stateOfContents != null &&
        fluidGroup != null &&
        ps != null && v != null && ps > 0 && v > 0
}
