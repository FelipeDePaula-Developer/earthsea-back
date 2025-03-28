package com.earthsea.ia_dev.services.interfaces

import com.earthsea.ia_dev.entities.Credential
import com.earthsea.ia_dev.entities.PhoneNumber
import com.earthsea.ia_dev.entities.User
import com.earthsea.ia_dev.entities.interfaces.Person
import java.util.function.Consumer

interface UserServicesInterface:ValidateServices {

    fun validatePerson(person: Person): Map<String, String> {
        val emailCheck = checkEmail(person.email)
        val ret = mutableMapOf<String, String>()

        if (!emailCheck) {
            ret["email"] = "Email ${person.email} is invalid"
        }

        if (person.cpf?.isNotEmpty() == true) {
            val cpfCheck = checkCPF(person.cpf)
            if (!cpfCheck) {true
                ret["cpf"] = "CPF ${person.cpf} is invalid"
            }
        }

        return ret
    }

    fun validateCredential(credential: Credential): Map<String, String> {
        val ret = mutableMapOf<String, String>()

        val usernameCheck = checkUsername(credential.login)
        if (!usernameCheck) {
            ret["login"] = "Login ${credential.login} is invalid"
        }

        val passwordCheck = checkPassword(credential.password)
        if (!passwordCheck) {
            ret["password"] = "The provided password is invalid. The password must contain at least one lowercase letter, one uppercase letter, one number, and one special character."
        }

        return ret
    }

}