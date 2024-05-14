package com.edurda77.qrworker.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {
    private var _state = MutableStateFlow(MainState())
    val state = _state.asStateFlow()


    fun onEvent(mainEvent: MainEvent) {
        when (mainEvent) {
            is MainEvent.TryAuthorization -> {
                if (mainEvent.userNumber.length==13) {
                    _state.value.copy(
                        appState = AppState.WorkScan,
                        user = mainEvent.userNumber
                    )
                        .updateStateUI()
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