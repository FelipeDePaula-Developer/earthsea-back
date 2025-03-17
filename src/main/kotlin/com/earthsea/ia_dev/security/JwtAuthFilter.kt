package com.earthsea.ia_dev.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.*
import javax.crypto.SecretKey

@Component
class JwtAuthFilter: OncePerRequestFilter(){

    private val secretKey: SecretKey = Keys.hmacShaKeyFor("superSecretKey".toByteArray(Charsets.UTF_8).copyOf(32))

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val jwt = getTokenFromCookies(request)
        if (jwt != null && SecurityContextHolder.getContext().authentication == null) {
            val claims = parseToken(jwt)

            if (claims != null) {
                val userDetails: UserDetails = User(claims.subject, "", listOf())

                val authToken = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

                SecurityContextHolder.getContext().authentication = authToken
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun getTokenFromCookies(request: HttpServletRequest): String? {
        return request.cookies?.firstOrNull { it.name == "jwt" }?.value
    }

    private fun parseToken(token: String): Claims? {
        return try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token).body
        } catch (e: Exception) {
            null
        }
    }

}