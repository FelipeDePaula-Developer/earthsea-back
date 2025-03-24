package com.earthsea.ia_dev.security

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController {
    @GetMapping("/validate")
    fun validateToken(@AuthenticationPrincipal userDetails: UserDetails?): ResponseEntity<*> {
        if (userDetails == null) {
            return ResponseEntity.status(401).body("Token inválido ou expirado.")
        }

        return ResponseEntity.ok().body(
            TokenValidationResponse(userDetails.username, "Token válido")
        )
    }

    @JvmRecord
    data class TokenValidationResponse(val username: String, val message: String)
}