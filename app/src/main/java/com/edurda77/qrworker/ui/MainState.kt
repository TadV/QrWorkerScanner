package com.edurda77.qrworker.ui

data class MainState (
    val appState: AppState = AppState.Authorization(AuthorizationState.EnterState),
    val user:String = "",
    val code: String = "",
    val message:String = "",
)

