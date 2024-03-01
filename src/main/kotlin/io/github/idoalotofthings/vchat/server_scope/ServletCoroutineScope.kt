package io.github.idoalotofthings.vchat.server_scope

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel

class ServletCoroutineScope : CoroutineScope {

    private val job: Job = Job()
    override val coroutineContext = job + Dispatchers.IO

    fun cancel() {
        coroutineContext.cancel()
    }

}