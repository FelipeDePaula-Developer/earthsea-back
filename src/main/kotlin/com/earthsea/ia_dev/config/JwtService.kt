package com.earthsea.ia_dev.config

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*
import io.jsonwebtoken.security.Keys
import java.util.Base64
import javax.crypto.SecretKey

@Service
class JwtService {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor(Base64.getEncoder().encode("superSecretKey".toByteArray()))

    fun generateToken(userId: Int): String {
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 3600000)) // 1 hora
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

//    fun validateToken(token: String): Boolean {
//        return try {
//            Jwts.parserBuilder().setSigningKey(secretKey.toByteArray()).build().parseClaimsJws(token)
//            true
//        } catch (e: Exception) {
//            false
//        }
//    }
}