package com.earthsea.ia_dev.forms

import com.earthsea.ia_dev.entities.Client
import com.earthsea.ia_dev.entities.Credential
import com.earthsea.ia_dev.entities.PhoneNumber
import com.earthsea.ia_dev.entities.User
import com.earthsea.ia_dev.forms.results.ValidationError
import org.springframework.http.HttpStatus
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

data class AuthResponse(
    var status: HttpStatus = HttpStatus.OK,
    var messages: List<String> = emptyList()
)

data class CadResponse(
    var status: HttpStatus = HttpStatus.OK,
    var messages: List<ValidationError> = emptyList()
)