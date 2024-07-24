package com.edurda77.qrworker.data.mapper

import androidx.annotation.Nullable
import com.edurda77.qrworker.data.local.CurrentUserEntity
import com.edurda77.qrworker.data.local.TechOperationEntity
import com.edurda77.qrworker.data.remote.DataServer
import com.edurda77.qrworker.data.remote.OperationDto
import com.edurda77.qrworker.data.remote.UpdateTechOperationBody
import com.edurda77.qrworker.domain.model.LocalTechOperation
import com.edurda77.qrworker.domain.model.LocalUser
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


fun OperationDto.convertToTechOperations(currentUserCode: String, currentUserName: String): List<TechOperation> {
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
            orderProduct = it.orderProduct?: "",
            orderProductChar = it.orderProductChar?: "",

            productionDivision = it.productionDivision?: "",
            productionProduct = it.productionProduct?: "",
            productionProductChar = it.productionProductChar?: "",

            currentUser = currentUserCode,
            currentUserFIO = currentUserName,

            workerCode = it.workerCode?: "",
            workerFIO = it.workerFIO?: "",

            techOperationData = it.techOperationData ?: "",
            isUploadedThisUser = currentUserCode == (it.workerCode ?: ""),

            quantity = it.quantity,
            unit = it.unit,
            sum = it.sum?: 0f,
            sumConfirmed = it.sumConfirmed?: 0f,

        )
    }
}

fun List<TechOperation>.convertToUpdateTechOperationBody(): List<UpdateTechOperationBody> {
    return this.filter {
        it.workerCode!! == "" || (it.workerCode == it.currentUser)
    }.map {
        UpdateTechOperationBody(
                codeUser = it.workerCode!!,
                currentUser = it.currentUser!!,
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



fun CurrentUserEntity?.convertToLocalUser(): LocalUser? {
    return if (this != null) LocalUser(
        userCode,
        userName,
    ) else null
}