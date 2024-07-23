package com.edurda77.qrworker.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel
import com.edurda77.qrworker.ui.uikit.FilterContent

@Composable
fun BaseScene(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val event = viewModel::onEvent
    when (val result = state.value.appState) {
        is AppState.WorkScan -> {
            WorkScreen(
                techOperations = state.value.techOperations,
                conflictTechOperations = state.value.conflictedTechOperations,
                message = state.value.message,
                workState = result.workState,
                user = state.value.user,
                userName = state.value.userName,
                isConflict = state.value.isConflict,
                filtersQueries = state.value.filtersQueries,
                approvedTechOperations = state.value.approvedTechOperations,
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