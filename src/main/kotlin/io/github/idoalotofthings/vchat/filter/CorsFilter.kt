package io.github.idoalotofthings.vchat.filter

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class CorsFilter : Filter {
    override fun doFilter(p0: ServletRequest?, p1: ServletResponse?, p2: FilterChain?) {
        val httpRequest = p0 as HttpServletRequest
        val httpResponse = p1 as HttpServletResponse

        httpResponse.apply {
            setHeader("Access-Control-Allow-Origin", "*")
            setHeader("Access-Control-Allow-Methods", "GET, OPTIONS")
            setHeader("Access-Control-Allow-Headers","Content-Type, Authorization")
            setHeader("Access-Control-Allow-Credentials", "true")
            setHeader("Access-Control-Max-Age","3600")
        }

        p2?.doFilter(httpRequest, httpResponse)
    }

}