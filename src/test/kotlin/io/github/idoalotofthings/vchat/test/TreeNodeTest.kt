package io.github.idoalotofthings.vchat.test

import io.github.idoalotofthings.vchat.model.Query
import io.github.idoalotofthings.vchat.model.QueryNode
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TreeNodeTest {

    private fun generateTree(
        verticalDepth: Int,
        horizontalDepth: Int,
        currentVerticalDepth: Int = 0,
        currentTreeNode: QueryNode = QueryNode(query=Query("0","0")),
        fmt: String = "0",
    ) : QueryNode {

        for (i in (0 until horizontalDepth)) {
            if(verticalDepth==currentVerticalDepth+2) {
               currentTreeNode.childQueryNodes.add(
                   QueryNode(query = Query("$fmt.$i", "$fmt.$i"))
               )
            } else {
                val childNode = QueryNode(query = Query("$fmt.$i", "$fmt.$i"))
                currentTreeNode.childQueryNodes.add(
                    generateTree(verticalDepth, horizontalDepth, currentVerticalDepth+1, childNode,"$fmt.$i")
                )
            }
        }
        return currentTreeNode
    }

    @Test
    fun `test tree generation`() {
        val tree = generateTree(3,2)
        val expectedTree = QueryNode(
            query = Query("0","0"),
            childQueryNodes = mutableListOf(
                QueryNode(
                    query = Query("0.0","0.0"),
                    childQueryNodes = mutableListOf(
                        QueryNode(query = Query("0.0.0","0.0.0")),
                        QueryNode(query = Query("0.0.1","0.0.1"))
                    )
                ),
                QueryNode(
                    query = Query("0.1","0.1"),
                    childQueryNodes = mutableListOf(
                        QueryNode(query = Query("0.1.0","0.1.0")),
                        QueryNode(query = Query("0.1.1","0.1.1"))
                    )
                )
            )
        )
        assertEquals(expectedTree, tree)
    }

    @Test
    fun `test tree traversal`() {
        val tree = generateTree(5,5)
        val query = tree.getNodeAtDepthString("0.4.2.4", 5).query.answer
        assertEquals("0.4.2.4", query)
    }

    @Test
    fun `test id gen`() {
        val expectedTree = QueryNode(
            query = Query("0","0"),
            depth = 0,
            nodeId = "0",
            childQueryNodes = mutableListOf(
                QueryNode(
                    query = Query("0.0","0.0"),
                    depth = 1,
                    nodeId = "0.0",
                    childQueryNodes = mutableListOf(
                        QueryNode(
                            query = Query("0.0.0","0.0.0"),
                            depth = 2,
                            nodeId = "0.0.0"
                        ),
                        QueryNode(
                            query = Query("0.0.1","0.0.1"),
                            depth = 2,
                            nodeId = "0.0.1"
                        )
                    )
                ),
                QueryNode(
                    query = Query("0.1","0.1"),
                    depth = 1,
                    nodeId = "0.1",
                    childQueryNodes = mutableListOf(
                        QueryNode(
                            query = Query("0.1.0","0.1.0"),
                            depth = 2,
                            nodeId = "0.1.0"
                        ),
                        QueryNode(
                            query = Query("0.1.1","0.1.1"),
                            depth = 2,
                            nodeId = "0.1.1"
                        )
                    )
                )
            )
        )

        val regularTree = QueryNode(
            query = Query("0","0"),
            childQueryNodes = mutableListOf(
                QueryNode(
                    query = Query("0.0","0.0"),
                    childQueryNodes = mutableListOf(
                        QueryNode(query = Query("0.0.0","0.0.0")),
                        QueryNode(query = Query("0.0.1","0.0.1"))
                    )
                ),
                QueryNode(
                    query = Query("0.1","0.1"),
                    childQueryNodes = mutableListOf(
                        QueryNode(query = Query("0.1.0","0.1.0")),
                        QueryNode(query = Query("0.1.1","0.1.1"))
                    )
                )
            )
        )
        assertEquals(expectedTree, regularTree.withNodeId())
    }

}