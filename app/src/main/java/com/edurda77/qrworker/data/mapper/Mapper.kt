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
            codeUser = it.codeUser,
            createdAt = it.createdAt,
            id = it.id,
            orderData = it.orderData,
            orderNumber = it.orderNumber,
            product = it.product,
            productionReport = it.productionReport,
            productionDivision = it.productionDivision,
            techOperation = it.techOperation,
            techOperationData = it.techOperationData ?: "",
            techOperationName = it.techOperationName,
            isUploadedThisUser = code == it.codeUser,
            currentUser = it.codeUser,
            quantity = it.quantity,
            unit = it.unit
        )
    }
}

fun List<TechOperation>.convertToUpdateTechOperationBody(): List<UpdateTechOperationBody> {
    return this.map {
        UpdateTechOperationBody(
            codeUser = it.codeUser,
            currentUser = it.currentUser,
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