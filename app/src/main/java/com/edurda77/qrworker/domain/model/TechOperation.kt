package com.edurda77.qrworker.domain.model


data class TechOperation (
    val codeUser: String,
    val createdAt: String,
    val id: Int,
    val orderData: String,
    val orderNumber: String,
    val product: String,
    val productionDivision: String,
    val productionReport: String,
    val techOperation: String,
    val techOperationData: String,
    val techOperationName: String,
    val isUploadedThisUser: Boolean,
    val currentUser: String,
    val quantity: Int,
    val unit: String,
)