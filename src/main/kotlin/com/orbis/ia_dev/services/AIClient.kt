package com.earthsea.ia_dev.services

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AIClient() {

    private val apiKey = System.getenv("DEEP_SEEK_KEY")
        ?: throw IllegalStateException("Chave da API não encontrada! Verifique as variáveis de ambiente.")

    private val client = HttpClient()

    suspend fun askQuestion(question: String): String {
        val url = "https://openrouter.ai/api/v1/chat/completions" // Exemplo de endpoint da OpenAI

        val requestBody = ChatRequest(
            model = "deepseek/deepseek-r1:freeHttpResponse", // Modelo da IA
            messages = listOf(Message(role = "user", content = question))
        )

        val response: HttpResponse = client.post(url) {
            header("Authorization", "Bearer $apiKey")
            header("Content-Type", "Content-Type: application/json")
            body = Json.encodeToString(requestBody)
        }

        return if (response.status == HttpStatusCode.OK) {
            val chatResponse = Json.decodeFromString<ChatResponse>(response.bodyAsText())
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