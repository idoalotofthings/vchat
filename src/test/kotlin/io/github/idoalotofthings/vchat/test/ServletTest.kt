package io.github.idoalotofthings.vchat.test

import io.github.idoalotofthings.vchat.VChatServlet
import io.github.idoalotofthings.vchat.model.ApiResponse
import io.github.idoalotofthings.vchat.model.ApiStatus
import io.github.idoalotofthings.vchat.model.QueryResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import java.io.PrintWriter

class ServletTest {

    private val servlet = VChatServlet()
    private lateinit var mockRequest: HttpServletRequest
    private lateinit var mockResponse: HttpServletResponse
    private lateinit var writer: PrintWriter

    @BeforeEach
    fun setup() {
        mockResponse = mock(HttpServletResponse::class.java)
        mockRequest = mock(HttpServletRequest::class.java)
        writer = mock(PrintWriter::class.java)
        `when`(mockRequest.getParameter("node_id")).thenReturn("0.0")
        `when`(mockResponse.writer).thenReturn(writer)
    }

    @Test
    fun `test unavailable response`() {
        val unavailableResponse = Json.encodeToString(
            ApiResponse(listOf(), ApiStatus.UNAVAILABLE)
        )
        servlet.doGet(mockRequest, mockResponse)
        verify(writer).write(unavailableResponse)
    }

    /**
     * Set a default tree before testing in [VChatServlet]
     */
    @Test
    fun `test servlet with mock response and request parameters`() {
        `when`(mockRequest.getParameter("node_id")).thenReturn("0.0")
        servlet.doGet(mockRequest, mockResponse)
        val expectedResponse = ApiResponse(
            queries = listOf(
                QueryResponse(
                    "What is it for?",
                    "Nothing",
                    "0.0.0"
                ),
                QueryResponse(
                    "What is it for?",
                    "Nothing",
                    "0.0.1"
                ),
                QueryResponse(
                    "What is it for?",
                    "Nothing",
                    "0.0.2"
                )
            ),
            ApiStatus.AVAILABLE
        )
        verify(writer).write(Json.encodeToString(expectedResponse))
    }

}