package com.edurda77.qrworker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BaseScene(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val event = viewModel::onEvent

    when (val result = state.value.appState) {
        is AppState.WorkScan -> {
            WorkScreen(
                qrCodes = state.value.qrCodes,
                message = state.value.message,
                workState = result.workState,
                event = event
            )
        }

        is AppState.Authorization -> {
            AuthorisationScreenZxing(
                authorizationState = result.authorizationState,
                event = event
            )
        }
    }
}