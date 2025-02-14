package com.earthsea.ia_dev.entities
import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import java.time.LocalDateTime

@Entity
data class PhoneNumber(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var idPhoneNumber : Int? = null,

    @ManyToOne
    @JoinColumn(name = "iduser", referencedColumnName = "iduser")
    var user: User? = null,

    @ManyToOne
    @JoinColumn(name = "idclient", referencedColumnName = "idclient")
    var client: Client? = null,

    @Column(length = 3, name = "phone_ddi")
    var phoneDDI: String? = null,

    @Column(length = 3, name = "phone_ddd")
    var phoneDDD: String? = null,

    @Column(length = 10, name = "phone_number")
    var phoneNumber: String? = null,

    @Column(length = 2, name = "phone_type")
    var phoneType: String? = null,

    @Column(columnDefinition = "CHAR(1) default 'T'", length = 1)
    var status: String? = "T",

    @Column(columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP", name = "insert_timestampp")
    @CreationTimestamp
    var insertTimestamp: LocalDateTime? = null
)