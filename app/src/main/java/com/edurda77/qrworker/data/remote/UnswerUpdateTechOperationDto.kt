package com.edurda77.qrworker.data.remote


import com.google.gson.annotations.SerializedName

data class UnswerUpdateTechOperationDto(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val remoteData: Boolean,
    @SerializedName("message")
    val message: String
)