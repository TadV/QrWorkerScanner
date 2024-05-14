package com.edurda77.qrworker.data.remote

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CodeDto (
    @SerializedName("code_user")
    val codeUser: String,
    @SerializedName("time_of_scan")
    val timeScan: String,
    @SerializedName("code_work")
    val qrCode: String,
): Serializable