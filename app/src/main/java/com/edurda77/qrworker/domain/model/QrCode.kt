package com.edurda77.qrworker.domain.model


data class QrCode(
    val id: Int = 0,
    val codeUser: String,
    val timeOfScan: String,
    val codeQr: String,
)
