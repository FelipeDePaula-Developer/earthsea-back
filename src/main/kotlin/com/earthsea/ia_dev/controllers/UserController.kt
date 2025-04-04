package com.earthsea.ia_dev.controllers

import com.earthsea.ia_dev.forms.AuthResponse
import com.earthsea.ia_dev.forms.AuthUserForm
import com.earthsea.ia_dev.forms.CadResponse
import com.earthsea.ia_dev.forms.UserForm
import com.earthsea.ia_dev.forms.results.PersonFormResult
import com.earthsea.ia_dev.forms.results.ValidationError
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
    fun cadUser(@RequestBody userForm: UserForm): ResponseEntity<CadResponse> {
        val personFormResult: PersonFormResult = userServices.registerUser(userForm);

        return if (personFormResult.hasErrors()){
            ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(CadResponse(HttpStatus.BAD_REQUEST, personFormResult.getAllErrors()))
        }else{
            ResponseEntity.ok(CadResponse(HttpStatus.OK))
        }
    }

    @PostMapping("/user/login")
    fun authUser(@RequestBody authUserForm: AuthUserForm, response: HttpServletResponse): ResponseEntity<AuthResponse> {
        val token = authServices.authenticate(authUserForm)

        return if (token != false) {
            val cookie = Cookie("jwt", token.toString())

            response.setHeader("Access-Control-Expose-Headers", "Set-Cookie")
            response.setHeader("Access-Control-Allow-Credentials", "true")

            cookie.isHttpOnly = true
            cookie.secure = true
            cookie.path = "/"
            cookie.maxAge = 3600
            cookie.setAttribute("SameSite", "None")

            response.addCookie(cookie)
            ResponseEntity.ok(AuthResponse(HttpStatus.OK))
        } else {
            ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponse(HttpStatus.UNAUTHORIZED, listOf("Falha na autenticação")))
        }
    }

    @PostMapping("/user/logout")
    fun logout(response: HttpServletResponse) {
        val cookie = Cookie("jwt", "")
        cookie.maxAge = 0
        response.addCookie(cookie)
    }
}