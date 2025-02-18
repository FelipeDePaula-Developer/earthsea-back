package com.earthsea.ia_dev.controllers

import com.earthsea.ia_dev.services.AIClient
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/chat")
class ChatInteractionController {

    private val aiClient = AIClient()

    @PostMapping("/generate")
    fun generateResponse(@RequestParam question: String): ResponseEntity<Any> {
        return try {
            val response = runBlocking { aiClient.askQuestion(question) }
            ResponseEntity.ok(mapOf("response" to response))
        } catch (e: Exception) {
            ResponseEntity.status(500).body(mapOf("error" to "Erro ao processar resposta: ${e.message}"))
        }
    }
}
