package io.github.idoalotofthings.vchat.model

import io.github.idoalotofthings.vchat.ext.digitCount
import kotlinx.serialization.Serializable

@Serializable
data class QueryNode(
    val isLeafNode: Boolean = false,
    val isRoot: Boolean = false,
    val query: Query,
    val depth: Int = 0,
    val childQueryNodes: MutableList<QueryNode> = mutableListOf()
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
        return "QueryNode(val = ${query.question}, children = $childQueryNodes)"
    }
}