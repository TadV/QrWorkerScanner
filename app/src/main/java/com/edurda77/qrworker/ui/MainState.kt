package com.edurda77.qrworker.ui

data class MainState (
    val appState: AppState = AppState.Autorization,
    val user:String = "",
    val code: String = "",
    val message:String = "",
)

