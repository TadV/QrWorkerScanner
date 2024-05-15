package com.edurda77.qrworker.ui

sealed class MainEvent {
    class TryAuthorization(val userNumber:String):MainEvent()
    class ChangeAppState(val appState: AppState):MainEvent()
    class AddNewQrCode(val code:String):MainEvent()
}