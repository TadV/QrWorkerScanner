package com.edurda77.qrworker.data.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH

interface ApiServer {

    @PATCH("users/login")
    suspend fun uploadCodes (@Body codes:List<CodeDto>):Response<String>
}