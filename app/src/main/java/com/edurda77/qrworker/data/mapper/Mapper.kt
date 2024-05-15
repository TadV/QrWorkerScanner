package com.edurda77.qrworker.data.mapper

import com.edurda77.qrworker.data.local.CodeEntity
import com.edurda77.qrworker.data.remote.DataServer
import com.edurda77.qrworker.domain.model.QrCode


fun List<CodeEntity>.convertToQrCodes(): List<QrCode> {
    return this.map { code->
        QrCode(
            id = code.id,
            codeQr = code.techOperation,
            codeUser = code.codeUser,
            timeOfScan = code.timeOfScan,
            isUpload = code.isUpload
        )
    }
}


fun List<CodeEntity>.convertToCodesDto(): List<DataServer> {
    return this.map { code->
        DataServer(
            techOperation = code.techOperation,
            codeUser = code.codeUser,
            timeOfScan = code.timeOfScan,
            productionReport = code.productionReport
        )
    }
}

fun CodeEntity?.convertToQrCode(): QrCode? {
    return if (this!=null) {
        QrCode(
            id = this.id,
            codeQr = this.techOperation,
            codeUser = this.codeUser,
            timeOfScan = this.timeOfScan,
            isUpload = this.isUpload
        )
    } else null
}