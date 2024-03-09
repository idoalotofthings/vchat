package io.github.idoalotofthings.vchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Serializable class for standardized responses to the client
 * @param queries the list of [QueryResponse] to be sent
 * @param status the current [ApiStatus]
 */
@Serializable
data class ApiResponse(
    val queries: List<QueryResponse>,
    val status: ApiStatus
)

/**
 * Serializable response with necessary query parameters
 * @param question the question at the current node
 * @param answer the answer at the current node
 * @param nodeId the ID of the current node
 */
@Serializable
data class QueryResponse(
    val question: String,
    val answer: String,
    val nodeId: String
)

/**
 * Serializable status of the API
 * @property AVAILABLE the API is up and working
 * @property UNAVAILABLE the API has no data and can't provide responses
 */
@Serializable
enum class ApiStatus {
    @SerialName("available")
    AVAILABLE,
    @SerialName("unavailable")
    UNAVAILABLE
}