package com.earthsea.ia_dev.controllers

import com.earthsea.ia_dev.forms.AuthUserForm
import com.earthsea.ia_dev.forms.UserForm
import com.earthsea.ia_dev.forms.results.PersonFormResult
import com.earthsea.ia_dev.services.CredentialServices
import com.earthsea.ia_dev.services.UserServices
import jakarta.servlet.http.Cookie
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController(private val userServices: UserServices, private val authServices: CredentialServices) {
    @PostMapping("/user/cad")
    fun cadUser(@RequestBody userForm: UserForm): ResponseEntity<Any> {
        val personFormResult: PersonFormResult = userServices.registerUser(userForm);

        return if (personFormResult.hasErrors()){
            ResponseEntity(personFormResult.getAllErrors(), HttpStatus.BAD_REQUEST)
        }else{
            ResponseEntity("Validation passed", HttpStatus.OK)
        }
    }

    @PostMapping("/user/login")
    fun authUser(@RequestBody authUserForm: AuthUserForm, response: HttpServletResponse): ResponseEntity<Any> {
        val token = authServices.authenticate(authUserForm)

        return if (token != null) {
            val cookie = Cookie("jwt", token.toString())
            cookie.isHttpOnly = true
            cookie.secure = true
            cookie.path = "/"
            cookie.maxAge = 3600

            response.addCookie(cookie)
            ResponseEntity.ok("Login bem-sucedido")
        } else {
            ResponseEntity("Falha na autenticação", HttpStatus.UNAUTHORIZED)
        }
    }

    @PostMapping("/user/logout")
    fun logout(response: HttpServletResponse) {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0
        response.addCookie(cookie)
    }
}