package com.edurda77.qrworker.ui

import com.edurda77.qrworker.domain.model.LocalTechOperation
import com.edurda77.qrworker.domain.model.TechOperation

data class MainState (
    val appState: AppState = AppState.Authorization(AuthorizationState.EnterState),
    val user:String = "",
    val opzs: String = "",
    val message:String = "",
    val techOperations: List<TechOperation> = emptyList(),
    val conflictedTechOperations: List<TechOperation> = emptyList(),
    val localTechOperations: List<LocalTechOperation> = emptyList(),
    val approvedTechOperations: List<TechOperation> = emptyList(),
    val searchQuery: String = "",
    val isConflict: Boolean = false,
    //val selectableTechOperations: List<TechOperation> = emptyList(),
)

