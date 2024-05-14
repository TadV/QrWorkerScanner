package com.edurda77.qrworker.ui

sealed interface AppState {
    data object Autorization:AppState
    data object WorkScan:AppState
}