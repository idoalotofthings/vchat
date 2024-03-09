package io.github.idoalotofthings.vchat.repository

import io.github.idoalotofthings.vchat.model.QueryNode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileReader
import java.io.IOException

/**
 * Interface for defining the working of source-specific [QueryRepository] implementations. Defines cache related methods itself.
 */
interface QueryRepository {

    /**
     * Full path for storing cache
     */
    val cacheDir: String

    /**
     * Asynchronously loads queries from a data source, emits in a [Flow]
     */
    fun loadQueries(): Flow<QueryNode>

    /**
     * Asynchronously checks status of the data source
     */
    suspend fun checkStatus(): Boolean

    /**
     * Loads cache from the default file, emits in a [Flow]
     */
    fun readCache() = flow {
        try {
            val cache = FileReader("$cacheDir/vchat_cache.json")
            val tree = Json.decodeFromString<QueryNode>(cache.readText())
            emit(tree)
        }  catch (ioe: IOException) {
            loadQueries().collect {
                writeToCache(it)
                emit(it)
            }
        }
    }.flowOn(Dispatchers.IO)

    /**
     * Asynchronously writes current tree state to cache file
     */
    suspend fun writeToCache(tree: QueryNode) {
        withContext(Dispatchers.IO) {
            val file = File("$cacheDir/vchat_cache.json")
            if(!file.exists()) {
                file.createNewFile()
            }
            val json = Json.encodeToString(tree)
            file.writeText(json)
        }
    }
}