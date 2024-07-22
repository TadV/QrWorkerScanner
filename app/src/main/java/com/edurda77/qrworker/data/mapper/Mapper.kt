package com.edurda77.qrworker.data.mapper

import com.edurda77.qrworker.data.local.TechOperationEntity
import com.edurda77.qrworker.data.remote.DataServer
import com.edurda77.qrworker.data.remote.OperationDto
import com.edurda77.qrworker.data.remote.UpdateTechOperationBody
import com.edurda77.qrworker.domain.model.LocalTechOperation
import com.edurda77.qrworker.domain.model.TechOperation
import com.edurda77.qrworker.domain.utils.getCurrentDateTime


fun List<TechOperationEntity>.convertToCodesDto(): List<DataServer> {
    return this.map { code ->
        DataServer(
            techOperation = code.techOperation,
            codeUser = code.codeUser,
            timeOfScan = code.timeOfChoose,
            productionReport = code.productionReport
        )
    }
}


fun OperationDto.convertToTechOperations(code: String): List<TechOperation> {
    return this.remoteData.map {
        TechOperation(
            id = it.id,
            createdAt = it.createdAt,

            productionReport = it.productionReport,
            techOperation = it.techOperation,
            techOperationName = it.techOperationName?: "",
            techOperationNumber = it.techOperationNumber?: "",

            orderNumber = it.orderNumber?: "",
            orderData = it.orderData?: "",

            productionDivision = it.productionDivision?: "",
            productionProduct = it.productionProduct?: "",
            productionProductChar = it.productionProductChar?: "",

            currentUser = it.workerCode ?: "",
            currentUserFIO = it.workerFIO ?: "",

            workerCode = it.workerCode?: "",
            workerFIO = it.workerFIO?: "",

            techOperationData = it.techOperationData ?: "",
            isUploadedThisUser = code == (it.workerCode ?: ""),

            quantity = it.quantity,
            unit = it.unit,
            sum = it.sum?: 0f,
            sumConfirmed = it.sumConfirmed?: 0f,

        )
    }
}

fun List<TechOperation>.convertToUpdateTechOperationBody(): List<UpdateTechOperationBody> {
    return this.map {
        UpdateTechOperationBody(
            codeUser = it.workerCode!!,
            currentUser = it.workerFIO!!,
            productionReport = it.productionReport,
            techOperation = it.techOperation,
            techOperationData = getCurrentDateTime()
        )
    }
}

fun TechOperationEntity?.convertToLocalTechOperation(): LocalTechOperation? {
    return if (this != null) LocalTechOperation(
        codeUser = this.codeUser,
        id = this.id,
        productionReport = this.productionReport,
        techOperation = this.techOperation,
        timeOfChoose = this.timeOfChoose
    ) else null
}

fun List<TechOperationEntity>.convertToLocalTechOperations(): List<LocalTechOperation> {
    return this.map {
        LocalTechOperation(
            codeUser = it.codeUser,
            id = it.id,
            productionReport = it.productionReport,
            techOperation = it.techOperation,
            timeOfChoose = it.timeOfChoose
        )
    }
}