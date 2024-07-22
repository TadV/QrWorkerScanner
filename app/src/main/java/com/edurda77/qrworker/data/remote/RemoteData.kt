package com.edurda77.qrworker.data.remote


import com.google.gson.annotations.SerializedName

data class RemoteData(
    @SerializedName("id")
    val id: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("production_report")
    val productionReport: String,
    @SerializedName("tech_operation")
    val techOperation: String,
    @SerializedName("tech_operation_name")
    val techOperationName: String?,
    @SerializedName("tech_operation_number")
    val techOperationNumber: String?,

    @SerializedName("order_data")
    val orderData: String?,
    @SerializedName("order_number")
    val orderNumber: String?,

    @SerializedName("production_division")
    val productionDivision: String?,
    @SerializedName("production_product")
    val productionProduct: String?,
    @SerializedName("production_product_char")
    val productionProductChar: String?,

    @SerializedName("worker_user")
    val workerCode: String?,
    @SerializedName("worker_fio")
    val workerFIO: String?,

    @SerializedName("tech_operation_data")
    val techOperationData: String?,
    @SerializedName("quantity")
    val quantity: Int?,
    @SerializedName("unit")
    val unit: String?,
    @SerializedName("sum")
    val sum: Float?,
    @SerializedName("sum_confirmed")
    val sumConfirmed: Float?,
)