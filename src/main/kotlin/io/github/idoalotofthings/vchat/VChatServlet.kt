package io.github.idoalotofthings.vchat

import io.github.idoalotofthings.vchat.ext.digitCount
import io.github.idoalotofthings.vchat.model.ApiResponse
import io.github.idoalotofthings.vchat.model.ApiStatus
import io.github.idoalotofthings.vchat.model.QueryNode
import io.github.idoalotofthings.vchat.model.QueryResponse
import io.github.idoalotofthings.vchat.repository.LocalQueryRepository
import io.github.idoalotofthings.vchat.repository.NetworkQueryRepository
import io.github.idoalotofthings.vchat.repository.QueryRepository
import io.github.idoalotofthings.vchat.server_scope.ServletCoroutineScope
import javax.servlet.ServletConfig
import javax.servlet.annotation.WebServlet
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.util.logging.Logger
import kotlin.concurrent.fixedRateTimer

/**
 * Servlet class for handling get requests with a single parameter, node_id of type [String]
 */
@WebServlet
class VChatServlet : HttpServlet() {

    private val logger = Logger.getLogger(VChatServlet::class.java.name)

    private val servletScope = ServletCoroutineScope()

    // The URL for remote query source
    private val vchatQueryPath = System.getenv("VCHAT_QUERY_PATH") ?: "https://vivekanandschool.in"

    private lateinit var cacheDir: String

    private lateinit var tree: QueryNode

    private var isAvailable = false

    private lateinit var repository: QueryRepository

    /**
     * Initialises the proper [QueryRepository]. Performs the following operations in order, moving to next if one fails:
     * 1. Checks availability of data on remote data source. On success, [NetworkQueryRepository] is retained
     * 2. Checks availability of cache from previous instances of [NetworkQueryRepository]
     * 3. Checks availability of local queries from local data source. On success, [LocalQueryRepository] is retained
     * 4. Checks availability of local cache from previous instances of [LocalQueryRepository]
     * 5. Marks API as unavailable
     */
    private fun initRepository() {
        repository = NetworkQueryRepository(
            vchatQueryPath,
            cacheDir
        )
        servletScope.launch {
            if(!repository.checkStatus()) {
                repository = LocalQueryRepository(
                    servletContext.getRealPath("WEB-INF/")+"queries.json",
                    cacheDir
                )

                try {
                    repository.loadQueries().collect {
                        isAvailable = true
                        tree = it
                    }
                } catch(e: Exception) {
                    logger.severe(e.message)
                    try {
                        repository.readCache().collect {
                            isAvailable = true
                            tree = it
                        }
                    } catch (e:Exception) {
                        isAvailable = false
                    }
                }
            } else {
                isAvailable = true
            }
        }
    }

    // Don't override constructor, perform initial operations in this method to stay in line with Servlet Lifecycle
    override fun init(config: ServletConfig?) {
        super.init(config)
        cacheDir = servletContext.getRealPath("/WEB-INF/temp")
        fixedRateTimer(
            name = "Data Refresh",
            daemon = true,
            period = 3600000L
        ) {
            if(::repository.isInitialized && ::tree.isInitialized) {
                servletScope.launch {
                    repository.writeToCache(tree)
                }
            }
            initRepository()
        }
    }

    public override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {

        if(!isAvailable) { initRepository() }

        resp?.apply {
            contentType = "application/json"
            characterEncoding = "UTF-8"
        }

        if(isAvailable) {
            val nodeId = req?.getParameter("node_id") ?: "0"
            val queryResponses = tree.getNodeAtDepthString(nodeId, nodeId.digitCount()).childQueryNodes.map {
                QueryResponse(
                    it.query.question,
                    it.query.answer,
                    it.nodeId
                )
            }
            val response = Json.encodeToString(ApiResponse(queryResponses, ApiStatus.AVAILABLE))
            resp?.writer?.write(response)
        } else {
            val errResponse = Json.encodeToString(ApiResponse(listOf(), ApiStatus.UNAVAILABLE))
            resp?.writer?.write(errResponse)
        }
    }
}
