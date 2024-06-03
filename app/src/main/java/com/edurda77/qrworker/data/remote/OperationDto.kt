package com.edurda77.qrworker.data.remote


import com.google.gson.annotations.SerializedName

data class OperationDto(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val remoteData: List<RemoteData>,
    @SerializedName("message")
    val message: String
)