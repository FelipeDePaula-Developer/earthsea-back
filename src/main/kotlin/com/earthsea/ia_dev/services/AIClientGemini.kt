package com.earthsea.ia_dev.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.*
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class AIClientGemini {

    private val apiKey = System.getenv("IA_KEY")
        ?: throw IllegalStateException("Chave da API não encontrada! Verifique as variáveis de ambiente.")

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun askQuestion(question: String): String {
        println("Iniciando Requisição ao OpenRouter")
        val url = "https://openrouter.ai/api/v1/chat/completions"

        val requestBody = OpenRouterRequest(
            model = "google/gemini-2.0-flash-thinking-exp-1219:free",
            messages = listOf(
                Message(
                    role = "user",
                    content = listOf(
                        Content(
                            type = "text",
                            text = question
                        )
                    )
                )
            )
        )

        val response: HttpResponse = client.post(url) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
                contentType(ContentType.Application.Json)
            }
            setBody(requestBody)
        }

        println(response.bodyAsText());
        return try {
            val chatResponse: OpenRouterResponse = response.body()
            println(chatResponse)
            chatResponse.choices.firstOrNull()?.message?.content ?: "Sem resposta da IA"
        } catch (e: Exception) {
            println("Erro ao desserializar resposta: ${e.message}")
            response.bodyAsText()
        }
    }

    @Serializable
    data class OpenRouterRequest(
        val model: String,
        val messages: List<Message>
    )

    @Serializable
    data class Message(
        val role: String,
        val content: List<Content>
    )

    @Serializable
    data class Content(
        val type: String,
        val text: String
    )

    @Serializable
    data class OpenRouterResponse(
        val id: String,
        val provider: String,
        val model: String,
        val `object`: String,
        val created: Long,
        val choices: List<Choice>,
        val usage: Usage
    )

    @Serializable
    data class Choice(
        val logprobs: String? = null,
        @SerialName("finish_reason") val finishReason: String,
        @SerialName("native_finish_reason") val nativeFinishReason: String,
        val index: Int,
        val message: MessageIA
    )

    @Serializable
    data class MessageIA(
        val role: String,
        val content: String
    )

    @Serializable
    data class Usage(
        @SerialName("prompt_tokens") val promptTokens: Int,
        @SerialName("completion_tokens") val completionTokens: Int,
        @SerialName("total_tokens") val totalTokens: Int
    )
}
