package com.edurda77.qrworker.ui

sealed class MainEvent {
    class TryAuthorization(val userNumber:String):MainEvent()
}