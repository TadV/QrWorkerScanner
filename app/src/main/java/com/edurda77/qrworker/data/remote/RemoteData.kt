package com.edurda77.qrworker.data.remote


import com.google.gson.annotations.SerializedName

data class RemoteData(
    @SerializedName("code_user")
    val codeUser: String,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("order_data")
    val orderData: String,
    @SerializedName("order_number")
    val orderNumber: String,
    @SerializedName("product")
    val product: String,
    @SerializedName("production_division")
    val productionDivision: String,
    @SerializedName("production_report")
    val productionReport: String,
    @SerializedName("tech_operation")
    val techOperation: String,
    @SerializedName("tech_operation_data")
    val techOperationData: String?,
    @SerializedName("tech_operation_name")
    val techOperationName: String,
    @SerializedName("quantity")
    val quantity: Int,
    @SerializedName("unit")
    val unit: String,
)