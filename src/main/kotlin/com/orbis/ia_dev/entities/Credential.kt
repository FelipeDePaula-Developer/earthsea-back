package com.earthsea.ia_dev.entities

import jakarta.persistence.*

@Entity
data class Credential(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idCredential : Int? = null,

    @Column
    var login: String? = null,

    @Column
    var password: String? = null,

    @Column(nullable = false)
    var salt: String? = null,

    @Column(columnDefinition = "CHAR(1) default 'T'", length = 1, nullable = false)
    var status: String? = "T",

    @Column(columnDefinition = "CHAR(1) default 'F'", length = 1)
    var logged: String? = null,

    @ManyToOne
    @JoinColumn(name = "iduser", referencedColumnName = "iduser")
    var user: User? = null,

)