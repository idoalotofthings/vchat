package io.github.idoalotofthings.vchat.network

import io.github.idoalotofthings.vchat.model.QueryNode
import io.github.idoalotofthings.vchat.model.Status
import retrofit2.http.GET

interface QueryNetworkApiService {

    @GET("queryTree")
    suspend fun getQueryTree(): QueryNode

    @GET("status")
    suspend fun checkStatus(): Status

}