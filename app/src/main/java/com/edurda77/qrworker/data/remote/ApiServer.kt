package com.edurda77.qrworker.data.remote

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiServer {

    @POST("mssql/insert")
    suspend fun uploadCodes (@Body codes:List<DataServer>):RemoteUnswerDto

    @GET("get-all-table_prod")
    suspend fun getRemoteCodes ():RemoteCodesDto

    @GET("/workers/{worker_code}")
    suspend fun getWorkerFio (@Path("worker_code") workerCode:String):RemoteWorkerDto

    @GET("tech-operations/{production_report}")
    suspend fun getTechOperations (@Path("production_report") productionReport:String):OperationDto

    @POST ("tech-operations")
    suspend fun updateTechOperations(@Body techOperations: List<UpdateTechOperationBody>): UnswerUpdateTechOperationDto
}