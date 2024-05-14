package com.edurda77.qrworker.data.mapper

import com.edurda77.qrworker.data.local.CodeEntity
import com.edurda77.qrworker.data.remote.CodeDto
import com.edurda77.qrworker.domain.model.QrCode


fun List<CodeEntity>.convertToQrCodes(): List<QrCode> {
    return this.map { code->
        QrCode(
            id = code.id,
            codeQr = code.techOperation,
            codeUser = code.codeUser,
            timeOfScan = code.timeOfScan
        )
    }
}


fun List<CodeEntity>.convertToCodesDto(): List<CodeDto> {
    return this.map { code->
        CodeDto(
            qrCode = code.techOperation,
            codeUser = code.codeUser,
            timeScan = code.timeOfScan
        )
    }
}

fun CodeEntity?.convertToQrCode(): QrCode? {
    return if (this!=null) {
        QrCode(
            id = this.id,
            codeQr = this.techOperation,
            codeUser = this.codeUser,
            timeOfScan = this.timeOfScan
        )
    } else null
}