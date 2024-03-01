package io.github.idoalotofthings.vchat.model

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonNames

@Serializable
@OptIn(ExperimentalSerializationApi::class)
enum class RequestType {
    @JsonNames("ASK")
    QUESTIONS,

    @JsonNames("QUERY")
    QUERY
}