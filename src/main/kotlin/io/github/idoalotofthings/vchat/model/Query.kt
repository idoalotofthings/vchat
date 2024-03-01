package io.github.idoalotofthings.vchat.model

import kotlinx.serialization.Serializable

@Serializable
data class Query(
    val question: String,
    val answer: String
)
