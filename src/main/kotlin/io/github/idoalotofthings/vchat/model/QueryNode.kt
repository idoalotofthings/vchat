package io.github.idoalotofthings.vchat.model

import kotlinx.serialization.Serializable

@Serializable
data class QueryNode(
    val isRoot: Boolean = false,
    val query: Query,
    val depth: Int = 0,
    val childQueryNodes: MutableList<QueryNode> = mutableListOf(),
    val nodeId: String = ""
) {

    fun getNodeAtDepthString(depthFmt: String, verticalDepth: Int): QueryNode {
        return if(verticalDepth != depth && depthFmt.length != 1) {
            val horizontalIndex = depthFmt[2].digitToInt()
            childQueryNodes[horizontalIndex].getNodeAtDepthString(depthFmt.substring(2), verticalDepth+1)
        } else {
            this
        }
    }

    override fun toString(): String {
        return "QueryNode(val = ${query.question}, depth = $depth, id = $nodeId, children = $childQueryNodes)"
    }

    fun withNodeId(nodeId: String = "0", horizontalDepth: Int = 0, verticalDepth: Int = 0): QueryNode {
        val currentId = if(isRoot) "0" else "$nodeId.$horizontalDepth"
        val newNodes = mutableListOf<QueryNode>()
        for (node in childQueryNodes.indices) {
            newNodes.add(childQueryNodes[node].withNodeId(currentId, node, verticalDepth+1))
        }
        return copy(childQueryNodes = newNodes, nodeId = currentId, depth = verticalDepth)
    }
}