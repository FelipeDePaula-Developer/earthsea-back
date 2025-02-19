package com.earthsea.ia_dev.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpHeaders
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AIClient {

    private val apiKey = System.getenv("DEEP_SEEK_KEY")
        ?: throw IllegalStateException("Chave da API não encontrada! Verifique as variáveis de ambiente.")

    private val client = HttpClient()

    suspend fun askQuestion(question: String): String {
        val url = kotlin.text.buildString {
            append("https://openrouter.ai/api/v1/chat/completions")
        }

        val requestBody = ChatRequest(
            model = "deepseek/deepseek-r1:freeHttpResponse",
            messages = listOf(Message(role = "user", content = question))
        )

        val response: HttpResponse = client.post(url) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
                contentType(ContentType.Application.Json)
            }
            setBody(Json.encodeToString(requestBody)) // Correção para Ktor 2.x
        }

        return if (response.status == HttpStatusCode.OK) {
            val chatResponse: ChatResponse = response.body() // Substitui bodyAsText()
            chatResponse.choices.firstOrNull()?.message?.content ?: "No response from AI"
        } else {
            throw Exception("Failed to get response from AI: ${response.status}")
        }
    }

    @Serializable
    data class ChatRequest(
        val model: String,
        val messages: List<Message>
    )

    @Serializable
    data class Message(
        val role: String,
        val content: String
    )

    @Serializable
    data class ChatResponse(
        val choices: List<Choice>
    )

    @Serializable
    data class Choice(
        val message: Message
    )
}
