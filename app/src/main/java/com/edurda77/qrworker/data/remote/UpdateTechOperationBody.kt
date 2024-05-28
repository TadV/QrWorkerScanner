package com.edurda77.qrworker.data.remote

import com.google.gson.annotations.SerializedName

data class UpdateTechOperationBody(
    @SerializedName("current_user")
    val currentUser: String,
    @SerializedName("production_report")
    val productionReport: String,
    @SerializedName("tech_operation")
    val techOperation: String,
    @SerializedName("code_user")
    val codeUser: String,
    @SerializedName("tech_operation_data")
    val techOperationData: String,
)
