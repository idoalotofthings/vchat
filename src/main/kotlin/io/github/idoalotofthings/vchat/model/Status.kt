package io.github.idoalotofthings.vchat.model

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val isOnline: Boolean
)
