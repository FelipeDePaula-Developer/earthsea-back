package com.earthsea.ia_dev.forms

import com.earthsea.ia_dev.entities.Client
import com.earthsea.ia_dev.entities.Credential
import com.earthsea.ia_dev.entities.PhoneNumber
import com.earthsea.ia_dev.entities.User
import org.springframework.stereotype.Component

@Component
data class UserForm(
    var user: User = User(),
    var phone: List<PhoneNumber> = emptyList(),
    var credential: Credential = Credential()
)

data class ClientForm(
    var client: Client = Client(),
    var phone: List<PhoneNumber> = emptyList()
)

data class AuthUserForm(
    var login: String,
    var password: String
)

data class QuestionForm(
    var question: String
)