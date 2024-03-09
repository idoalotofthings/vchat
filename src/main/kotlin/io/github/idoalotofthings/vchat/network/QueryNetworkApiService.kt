package io.github.idoalotofthings.vchat.network

import io.github.idoalotofthings.vchat.model.QueryNode
import io.github.idoalotofthings.vchat.model.Status
import retrofit2.http.GET

/**
 * API Service Interface for [retrofit2.Retrofit] to instantiate
 */
interface QueryNetworkApiService {

    /**
     * GET request at /queryTree endpoint
     */
    @GET("queryTree")
    suspend fun getQueryTree(): QueryNode

    /**
     * GET request at /status endpoint
     */
    @GET("status")
    suspend fun checkStatus(): Status

}