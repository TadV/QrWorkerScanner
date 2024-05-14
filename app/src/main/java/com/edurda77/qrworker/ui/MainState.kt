package com.edurda77.qrworker.ui

import com.edurda77.qrworker.domain.model.QrCode

data class MainState (
    val appState: AppState = AppState.Authorization(AuthorizationState.EnterState),
    val user:String = "",
    val code: String = "",
    val message:String = "",
    val qrCodes: List<QrCode> = emptyList()
)

