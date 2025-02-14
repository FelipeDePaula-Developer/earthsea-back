package com.earthsea.ia_dev.entities

import com.earthsea.ia_dev.entities.interfaces.Person
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
@Table(name = "app_user")
data class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idUser: Int? = null,

    @Column(length = 11)
    override var cpf: String? = null,

    @Column
    override var email: String? = null,

    @Column(name = "insert_timestamp", updatable = false)
    @CreationTimestamp
    var insertTimestamp: LocalDateTime? = null,

    @Column
    override var name: String? = null,

    @Column(length = 1, nullable = false)
    override var status: String? = "T"
) : Person
