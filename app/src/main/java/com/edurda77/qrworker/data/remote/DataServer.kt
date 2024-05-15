package com.edurda77.qrworker.data.remote


import com.google.gson.annotations.SerializedName

data class DataServer(
    @SerializedName("code_user")
    val codeUser: String,
    @SerializedName("production_report")
    val productionReport: String,
    @SerializedName("tech_operation")
    val techOperation: String,
    @SerializedName("time_of_scan")
    val timeOfScan: String
)