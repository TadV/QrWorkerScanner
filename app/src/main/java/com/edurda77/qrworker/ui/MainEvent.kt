package com.edurda77.qrworker.ui

import com.edurda77.qrworker.domain.model.TechOperation


sealed class MainEvent {
    class TryAuthorization(val userNumber:String):MainEvent()
    class ChangeAppState(val appState: AppState):MainEvent()
    class ScanOpzs(val code:String):MainEvent()
    class SelectTechOperation(val techOperation: TechOperation):MainEvent()
    data object UploadForce:MainEvent()
    data object UploadSelectedTechOperations:MainEvent()
    data object OnSearch :MainEvent()

    class UpdateFilteredList(val index: Int, val query:String):MainEvent()
    data object AddItemInFilteredList : MainEvent()
    class DeleteItemFromFilterList(val index: Int):MainEvent()
    data object GetApprovedTechOperationPrevDay:MainEvent()
    data object GetApprovedTechOperationCurrentMonth:MainEvent()
}