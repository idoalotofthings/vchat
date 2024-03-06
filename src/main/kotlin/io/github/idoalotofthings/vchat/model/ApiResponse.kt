package io.github.idoalotofthings.vchat.model

import kotlinx.serialization.Serializable

@Serializable
data class ApiResponse(
    val queries: List<QueryResponse>
)

@Serializable
data class QueryResponse(
    val question: String,
    val answer: String,
    val nodeId: String
)