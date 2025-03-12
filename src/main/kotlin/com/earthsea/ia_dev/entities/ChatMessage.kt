package com.earthsea.ia_dev.entities

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "chat_messages")
data class ChatMessage(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "user_id", nullable = false)
    var userId: String,

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    var role: Role,

    @Column(columnDefinition = "TEXT", nullable = false)
    var content: String,

    @Column(name = "created_at", updatable = false)
    @CreationTimestamp
    var createdAt: LocalDateTime? = null
)

enum class Role {
    SYSTEM, USER, ASSISTANT
}
