package io.github.idoalotofthings.vchat.model

import kotlinx.serialization.Serializable

/**
 * Internal representation of a query
 * @param question the question at the node
 * @param answer the answer at the node
 */
@Serializable
data class Query(
    val question: String,
    val answer: String
)
