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
        AppState.WorkScan -> {
            WorkScreen()
        }

        is AppState.Authorization -> {
            AuthorisationScreenZxing(
                authorizationState = result.authorizationState,
                event = event
            )
        }
    }
}