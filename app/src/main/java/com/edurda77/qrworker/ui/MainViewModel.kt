package com.edurda77.qrworker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edurda77.qrworker.domain.repository.WorkRepository
import com.edurda77.qrworker.domain.utils.DOUBLICAT
import com.edurda77.qrworker.domain.utils.Resource
import com.edurda77.qrworker.domain.utils.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val workRepository: WorkRepository
) : ViewModel() {
    private var _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()

    init {
        loadQrCodes()
    }

    private fun loadQrCodes() {
        viewModelScope.launch {
           // workRepository.clearQrCodes()
            workRepository.getAllQrCodes().collect { resultLoad ->
                when (resultLoad) {
                    is Resource.Error -> {
                        resultLoad.message?.let {
                            _state.value.copy(
                                message = it,
                            )
                                .updateStateUI()
                        }
                    }

                    is Resource.Success -> {
                        _state.value.copy(
                            qrCodes = resultLoad.data ?: emptyList(),
                        )
                            .updateStateUI()
                    }
                }
            }
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

            is MainEvent.AddNewQrCode -> {
                viewModelScope.launch {
                    when (val resultLoad = workRepository.getQrCode(mainEvent.code)) {
                        is Resource.Error -> {
                            resultLoad.message?.let {
                                _state.value.copy(
                                    message = it,
                                )
                                    .updateStateUI()
                            }
                        }

                        is Resource.Success -> {
                            if (resultLoad.data == null) {
                                _state.value.copy(
                                    message = mainEvent.code,
                                    appState = AppState.WorkScan(WorkState.ReadyScanState)
                                )
                                    .updateStateUI()
                                workRepository.insertQrCode(
                                    user = _state.value.user,
                                    timeScan = getCurrentTime(),
                                    qrCode = mainEvent.code
                                )
                            } else {
                                _state.value.copy(
                                    appState = AppState.WorkScan(WorkState.ReadyScanState),
                                    message = DOUBLICAT,
                                )
                                    .updateStateUI()
                               /* delay(300)
                                _state.value.copy(
                                    message = ""
                                )
                                    .updateStateUI()*/
                            }
                        }
                    }
                }
            }
        }
    }

    private fun MainState.updateStateUI() {
        _state.update {
            this
        }
    }
}