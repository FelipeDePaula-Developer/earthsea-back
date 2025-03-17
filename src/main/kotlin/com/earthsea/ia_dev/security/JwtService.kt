package com.earthsea.ia_dev.security

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*
import io.jsonwebtoken.security.Keys
import javax.crypto.SecretKey

@Service
class JwtService {
    private val secretKey: SecretKey = Keys.hmacShaKeyFor("superSecretKey".toByteArray(Charsets.UTF_8).copyOf(32))

    fun generateToken(userId: Int): String {
        return Jwts.builder()
            .setSubject(userId.toString())
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 3600000)) // 1 hora
            .signWith(secretKey, SignatureAlgorithm.HS256)
            .compact()
    }

}