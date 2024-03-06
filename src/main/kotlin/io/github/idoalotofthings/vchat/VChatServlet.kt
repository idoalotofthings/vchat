package io.github.idoalotofthings.vchat

import io.github.idoalotofthings.vchat.ext.digitCount
import io.github.idoalotofthings.vchat.model.ApiResponse
import io.github.idoalotofthings.vchat.model.QueryNode
import io.github.idoalotofthings.vchat.model.QueryResponse
import io.github.idoalotofthings.vchat.repository.LocalQueryRepository
import io.github.idoalotofthings.vchat.repository.NetworkQueryRepository
import io.github.idoalotofthings.vchat.repository.QueryRepository
import io.github.idoalotofthings.vchat.server_scope.ServletCoroutineScope
import jakarta.servlet.annotation.WebServlet
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@WebServlet(name="VChatServlet", value = ["/chat"])
class VChatServlet : HttpServlet() {

    private val servletScope = ServletCoroutineScope()

    // TODO: Ensure system variable exists
    private val vchatQueryPath = System.getenv("VCHAT_QUERY_PATH")
    private val cacheDir = servletContext.getRealPath("/WEB-INF/temp")

    private lateinit var tree: QueryNode

    private var repository: QueryRepository = NetworkQueryRepository(
        vchatQueryPath,
        cacheDir
    )
    init {
        servletScope.launch {
            if(!repository.checkStatus()) {
                repository = LocalQueryRepository(
                    vchatQueryPath,
                    cacheDir
                )
            }

            repository.loadQueries().collect {
                tree = it
            }
        }
    }

    override fun doGet(req: HttpServletRequest?, resp: HttpServletResponse?) {
        resp?.apply {
            contentType = "application/json"
            characterEncoding = "UTF-8"
        }

        val nodeId = req?.getParameter("node_id") ?: "0"
        val queryResponses = tree.getNodeAtDepthString(nodeId, nodeId.digitCount()).childQueryNodes.map {
            QueryResponse(
                it.query.question,
                it.query.answer,
                it.nodeId
            )
        }
        val response = Json.encodeToString(ApiResponse(queryResponses))
        resp?.writer?.write(response)
    }
}