package io.github.idoalotofthings.vchat.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val queries: List<QueryResponse>,
    val status: ApiStatus
)

@Serializable
data class QueryResponse(
    val question: String,
    val answer: String,
    val nodeId: String
)

@Serializable
enum class ApiStatus {
    @SerialName("available")
    AVAILABLE,
    @SerialName("unavailable")
    UNAVAILABLE
}