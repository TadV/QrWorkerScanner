package com.edurda77.qrworker.ui

sealed interface AppState {
    class Authorization(val authorizationState: AuthorizationState):AppState
    data object WorkScan:AppState
}

sealed interface AuthorizationState{
    data object EnterState:AuthorizationState
    data object ScannerState:AuthorizationState
}