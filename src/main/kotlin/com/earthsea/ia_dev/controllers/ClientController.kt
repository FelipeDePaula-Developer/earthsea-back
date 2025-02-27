package com.earthsea.ia_dev.controllers

import com.earthsea.ia_dev.forms.AuthUserForm
import com.earthsea.ia_dev.forms.ClientForm
import com.earthsea.ia_dev.forms.UserForm
import com.earthsea.ia_dev.forms.results.PersonFormResult
import com.earthsea.ia_dev.services.CredentialServices
import com.earthsea.ia_dev.services.UserServices
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class ClientController(private val userServices: UserServices) {
    @PostMapping("cad/client")
    fun cadClient(@RequestBody clientForm : ClientForm): ResponseEntity<Any> {
        val personFormResult: PersonFormResult = userServices.registerClient(clientForm);

        return if (personFormResult.hasErrors()){
            ResponseEntity(personFormResult.getAllErrors(), HttpStatus.BAD_REQUEST)
        }else{
            ResponseEntity("Validation passed", HttpStatus.OK)
        }
    }
}