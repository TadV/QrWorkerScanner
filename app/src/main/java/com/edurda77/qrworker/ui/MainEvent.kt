package com.edurda77.qrworker.ui



sealed class MainEvent {
    class TryAuthorization(val userNumber:String):MainEvent()
    class ChangeAppState(val appState: AppState):MainEvent()
    class ScanOpzs(val code:String):MainEvent()
    class SelectTechOperation(val index:Int):MainEvent()
    data object UploadForce:MainEvent()
    data object UploadSelectedTechOperations:MainEvent()
}