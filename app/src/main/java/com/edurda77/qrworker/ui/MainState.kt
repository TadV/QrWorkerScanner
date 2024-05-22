package com.edurda77.qrworker.ui

import com.edurda77.qrworker.domain.model.TechOperation

data class MainState (
    val appState: AppState = AppState.Authorization(AuthorizationState.EnterState),
    val user:String = "",
    val opzs: String = "",
    val message:String = "",
    val techOperations: List<TechOperation> = emptyList(),
    val searchQuery: String = "",
    //val selectableTechOperations: List<TechOperation> = emptyList(),
)

