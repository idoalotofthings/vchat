package io.github.idoalotofthings.vchat.model

import kotlinx.serialization.Serializable

/**
 * Status object for [io.github.idoalotofthings.vchat.network.QueryNetworkApiService]
 */
@Serializable
data class Status(
    val isOnline: Boolean
)
