package com.edurda77.qrworker.data.remote


import com.google.gson.annotations.SerializedName

data class RemoteCodesDto(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val dataServer: List<DataServer>?,
    @SerializedName("message")
    val message: String
)


data class RemoteUnswerDto(
    @SerializedName("code")
    val code: Int,
    @SerializedName("data")
    val result: String?,
    @SerializedName("message")
    val message: String
)