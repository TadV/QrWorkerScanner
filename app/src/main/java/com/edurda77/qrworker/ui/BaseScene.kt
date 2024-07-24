package com.edurda77.qrworker.ui

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BaseScene(
    viewModel: MainViewModel = hiltViewModel()
) {
    val state = viewModel.state.collectAsState()
    val event = viewModel::onEvent
    Log.d("BaseScene", "state.value.appState")
    when (val result = state.value.appState) {
        is AppState.WorkScan -> {
            Log.d("appState", "AppState.WorkScan")
            WorkScreen(
                techOperations = state.value.techOperations,
                visibleTechOperations = state.value.visibleTechOperations,
                approvedTechOperations = state.value.approvedTechOperations,
                conflictTechOperations = state.value.conflictedTechOperations,

                message = state.value.message,
                workState = result.workState,

                user = state.value.user,
                userName = state.value.userName,

                isConflict = state.value.isConflict,
                filtersQueries = state.value.filtersQueries,
                query = state.value.query,
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