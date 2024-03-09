package io.github.idoalotofthings.vchat.repository

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.github.idoalotofthings.vchat.network.QueryNetworkApiService
import kotlinx.coroutines.flow.flow
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import retrofit2.Retrofit

/**
 * [QueryRepository] implementation for loading data from a remote data source
 * @param hostUrl to read data from
 */
class NetworkQueryRepository(
    hostUrl: String,
    override val cacheDir: String,
) : QueryRepository {

    /**
     * Instance of [QueryNetworkApiService] for making GET requests to the remote data source
     */
    private val networkApiService = Retrofit.Builder()
        .baseUrl(hostUrl)
        .addConverterFactory(Json.asConverterFactory(MediaType.get("application/json")))
        .build()
        .create(QueryNetworkApiService::class.java)

    override suspend fun checkStatus(): Boolean {
        return try {
            networkApiService.checkStatus().isOnline
        } catch (e: Exception) {
            false
        }
    }

    override fun loadQueries() = flow {
        emit(networkApiService.getQueryTree().withNodeId())
    }
}