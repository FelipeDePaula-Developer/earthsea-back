package com.earthsea.ia_dev.controllers

import com.earthsea.ia_dev.services.AIClientDeepSeek
import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import com.earthsea.ia_dev.forms.QuestionForm
import com.earthsea.ia_dev.services.AIClientGemini
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/chat")
class  ChatInteractionController {

    private val AIClientDeepSeek = AIClientDeepSeek()
    private val AIClientGemini = AIClientGemini()

    @PostMapping("/generate/deepseek")
    fun generateResponseDeepSeek(@RequestBody questionForm: QuestionForm): ResponseEntity<Any> {
        return try {

            val response = runBlocking {
                AIClientDeepSeek.askQuestion(questionForm.question)
            }

            ResponseEntity.ok(mapOf("response" to response))

        } catch (e: Exception) {

            ResponseEntity.status(500).body(mapOf("error" to "Erro ao processar resposta: ${e.message}"))

        }
    }

    @PostMapping("/generate/gemini")
    fun generateResponseGemini(@RequestBody questionForm: QuestionForm): ResponseEntity<Any> {
        return try {

            val response = runBlocking {
                AIClientGemini.askQuestion(questionForm.question)
            }

            ResponseEntity.ok(mapOf("response" to response))

        } catch (e: Exception) {

            ResponseEntity.status(500).body(mapOf("error" to "Erro ao processar resposta: ${e.message}"))

        }
    }

}
