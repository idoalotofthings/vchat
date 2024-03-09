package io.github.idoalotofthings.vchat.server_scope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

/**
 * Implementation of [CoroutineScope] for handling asynchronous and potentially blocking operations on Main thread, by delegating heavy tasks to [Dispatchers.IO]
 */
class ServletCoroutineScope : CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext = job + Dispatchers.IO

    /**
     * Cancels all running operations
     */
    fun cancel() {
        coroutineContext.cancel()
    }

}