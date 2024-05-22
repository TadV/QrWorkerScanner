package com.edurda77.qrworker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.qrworker.domain.model.TechOperation
import com.edurda77.qrworker.domain.repository.WorkRepository
import com.edurda77.qrworker.domain.utils.Resource
import com.edurda77.qrworker.domain.utils.getCurrentDate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workRepository: WorkRepository
) : ViewModel() {
    private var _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()
    private val _shadowTechOperations = MutableStateFlow<List<TechOperation>>(emptyList())

    init {
        viewModelScope.launch {
            /*workRepository.updateTechOperations(
                currentUser = "123123123",
                techOperations = listOf(
                    TechOperation(
                        codeUser = "123123123",
                        createdAt = "2024-05-21T10:38:19.983Z",
                        id = 1,
                        isChecked = false,
                        isEnabled = true,
                        orderData = "2023-10-28T00:00:00Z",
                        orderNumber = "TRE00000908",
                        product = "Поролон",
                        productionDivision = "Раскройный Цех",
                        productionReport = "5b712337-7d6d-11ee-96f7-9440c9fe75e0",
                        techOperation = "102",
                        techOperationData = "2024-05-21T11:14:00Z",
                        techOperationName = "102 Транспортировка деталей в ячейку стеллажа для комплектования"
                    )
                )
            )*/
        }
    }

    fun onEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            is MainEvent.TryAuthorization -> {
                if (mainEvent.userNumber.length == 13) {
                    _state.value.copy(
                        appState = AppState.WorkScan(WorkState.ReadyScanState),
                        user = mainEvent.userNumber
                    )
                        .updateStateUI()
                }
            }

            is MainEvent.ChangeAppState -> {
                _state.value.copy(
                    appState = mainEvent.appState,
                )
                    .updateStateUI()
            }

            is MainEvent.ScanOpzs -> {
                viewModelScope.launch {
                    val result = workRepository.getTechOperations(
                        codeUser = _state.value.user,
                        numberOPZS = mainEvent.code
                    )
                    when (result) {
                        is Resource.Error -> {

                        }

                        is Resource.Success -> {
                            _shadowTechOperations.value = result.data ?: emptyList()
                            _state.value.copy(
                                techOperations = result.data ?: emptyList(),
                            )
                                .updateStateUI()
                        }
                    }
                }

            }

            MainEvent.UploadForce -> {
                viewModelScope.launch {
                    workRepository.uploadPerDayData()
                }
            }

            is MainEvent.SelectTechOperation -> {
                viewModelScope.launch {
                    when (val resultLoad =
                        workRepository.getCodeById(mainEvent.techOperation.id)) {
                        is Resource.Error -> {

                        }

                        is Resource.Success -> {
                            if (resultLoad.data != null) {
                                workRepository.deleteCodeById(mainEvent.techOperation.id)
                                //////////////
                            } else {
                                workRepository.insertQrCode(
                                    id = mainEvent.techOperation.id,
                                    techOperation = mainEvent.techOperation.techOperation,
                                    productionReport = mainEvent.techOperation.productionReport,
                                    timeScan = getCurrentDate(),
                                    user = _state.value.user
                                )
                                ///////
                            }
                        }
                    }
                    _shadowTechOperations.value = updateVisibleTechOperations(
                        techOperation = mainEvent.techOperation,
                        oldTechOperations = _shadowTechOperations.value
                    )
                    _state.value.copy(
                        techOperations = _shadowTechOperations.value,
                    )
                        .updateStateUI()
                }
            }

            MainEvent.UploadSelectedTechOperations -> {
                val selectedOperations =
                    _state.value.techOperations.filter { it.codeUser == _state.value.user }
                viewModelScope.launch {
                    workRepository.updateTechOperations(
                        techOperations = selectedOperations,
                        currentUser = _state.value.user
                    )
                }
            }

            is MainEvent.OnSearch -> {
                _state.value.copy(
                    searchQuery = mainEvent.query,
                    techOperations = _shadowTechOperations.value.filter {
                        it.techOperationName.contains(
                            mainEvent.query,
                            ignoreCase = true
                        )
                    }
                )
                    .updateStateUI()
            }
        }
    }

    private fun updateVisibleTechOperations(
        techOperation: TechOperation,
        oldTechOperations: List<TechOperation>
    ): List<TechOperation> {
        val index =
            oldTechOperations.indexOf(oldTechOperations.firstOrNull { it == techOperation })
        val oldTechOperation = _state.value.techOperations[index]
        val newTechOperation =
            if (oldTechOperation.codeUser.isBlank()) oldTechOperation.copy(codeUser = _state.value.user) else oldTechOperation.copy(
                codeUser = ""
            )
        val oldOperations = _state.value.techOperations.toMutableList()
        oldOperations.removeAt(index)
        oldOperations.add(index, newTechOperation)
        return oldOperations
    }

    private fun MainState.updateStateUI() {
        _state.update {
            this
        }
    }
}