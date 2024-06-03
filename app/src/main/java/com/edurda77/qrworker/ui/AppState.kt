package com.edurda77.qrworker.ui

sealed interface AppState {
    class Authorization(val authorizationState: AuthorizationState):AppState
    class WorkScan(val workState: WorkState):AppState
}

sealed interface AuthorizationState{
    data object EnterState:AuthorizationState
    data object ScannerState:AuthorizationState
}

sealed interface WorkState{
    data object ReadyScanState:WorkState
    data object ProcessScannerState:WorkState
}