package com.edurda77.qrworker.domain.model


data class TechOperation (
    val id: Int,
    val createdAt: String,

    val productionReport: String,
    val techOperation: String,
    val techOperationName: String?,
    val techOperationNumber: String?,

    val orderNumber: String?,
    val orderData: String?,

    val productionDivision: String?,
    val productionProduct: String?,
    val productionProductChar: String?,

    val currentUser: String?,
    val currentUserFIO: String?,

    val workerCode: String?,
    val workerFIO: String?,

    val techOperationData: String?,
    val isUploadedThisUser: Boolean,

    val quantity: Int?,
    val unit: String?,
    val sum: Float?,
    val sumConfirmed: Float?,
)