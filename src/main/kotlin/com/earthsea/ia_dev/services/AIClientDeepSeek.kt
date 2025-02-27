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
import kotlinx.serialization.json.Json
import org.springframework.stereotype.Service

@Service
class AIClientDeepSeek {

    private val apiKey = System.getenv("IA_KEY")
        ?: throw IllegalStateException("Chave da API não encontrada! Verifique as variáveis de ambiente.")

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    suspend fun askQuestion(question: String): String {
        println("Iniciando Requisiçao")
        val url = "https://openrouter.ai/api/v1/chat/completions"

        val requestBody = ChatRequest(
            model = "deepseek/deepseek-r1:free",
            messages = listOf(Message(role = "user", content = question)),
            temperature = 0.7,
            max_tokens = 150
        )

        println(requestBody)
        val response: HttpResponse = client.post(url) {
            headers {
                append(HttpHeaders.Authorization, "Bearer $apiKey")
                contentType(ContentType.Application.Json)
            }
            setBody(requestBody)
        }
        val responseBody = response.bodyAsText()
        println(response.bodyAsText());

        return if (response.status.value != 200) {
//            throw Exception("Erro na API: ${errorResponse.error.message} (Código: ${errorResponse.error.code})")
            throw Exception("Failed to get response from AI: ${response.status}")
        } else {
            val chatResponse: ChatResponse = response.body()
            chatResponse.choices.firstOrNull()?.message?.content ?: "No response from AI"
        }
    }

    @Serializable
    data class ErrorDetail(
        val message: String,
        val code: Int
    )



    @Serializable
    data class ChatResponse(
        val choices: List<Choice>
    )

    @Serializable
    data class ApiResponse(
        val id: String,
        val provider: String,
        val model: String,
        val `object`: String,
        val created: Long,
        val choices: List<Choice>? = null,
        val usage: Usage,
        val error: ErrorDetail? = null,
        @SerialName("user_id") val userId: String? = null
    )

    @Serializable
    data class LogprobsContent(
        val token: String,
        val logprob: Double
    )

    @Serializable
    data class Logprobs(
        val content: List<LogprobsContent>
    )

    @Serializable
    data class Choice(
        val logprobs: Logprobs? = null,
        val finishReason: String? = null,
        @SerialName("native_finish_reason") val nativeFinishReason: String? = null,
        val index: Int? = null,
        val message: Message? = null
    )

    @Serializable
    data class Message(
        val role: String,
        val content: String,
        val refusal: String? = null
    )

    @Serializable
    data class Usage(
        @SerialName("prompt_tokens") val promptTokens: Int,
        @SerialName("completion_tokens") val completionTokens: Int,
        @SerialName("total_tokens") val totalTokens: Int
    )

    @Serializable
    data class ChatRequest(
        val model: String,
        val messages: List<Message>,
        val temperature: Double,
        val max_tokens: Int
    )
}
