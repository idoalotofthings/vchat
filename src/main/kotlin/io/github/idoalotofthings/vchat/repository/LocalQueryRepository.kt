package io.github.idoalotofthings.vchat.repository

import io.github.idoalotofthings.vchat.model.QueryNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileReader

class LocalQueryRepository(
    private val filePath: String,
    override val cacheDir: String
) : QueryRepository {

    override fun loadQueries() = flow {
        val queries = FileReader(filePath).readText()
        val tree = Json.decodeFromString<QueryNode>(queries)
        emit(tree.withNodeId())
    }.flowOn(Dispatchers.IO)

    override suspend fun checkStatus(): Boolean {
        val file = File(filePath)
        return file.exists()
    }

}