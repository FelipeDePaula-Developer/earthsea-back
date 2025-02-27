package com.earthsea.ia_dev.forms.results

data class ValidationError(val field: String, val message: String)

class PersonFormResult {
    private val userErrors: MutableList<ValidationError> = mutableListOf()
    private val credentialErrors: MutableList<ValidationError> = mutableListOf()
    private val phoneErrors: MutableList<ValidationError> = mutableListOf()

    fun getUserErrors(): List<ValidationError> = userErrors
    fun addUserError(field: String, message: String) {
        userErrors.add(ValidationError(field, message))
    }

    fun getCredentialErrors(): List<ValidationError> = credentialErrors
    fun addCredentialError(field: String, message: String) {
        credentialErrors.add(ValidationError(field, message))
    }

    fun getPhoneErrors(): List<ValidationError> = phoneErrors
    fun addPhoneError(field: String, message: String) {
        phoneErrors.add(ValidationError(field, message))
    }

    fun hasErrors(): Boolean {
        return userErrors.isNotEmpty() || credentialErrors.isNotEmpty() || phoneErrors.isNotEmpty()
    }

    fun getAllErrors(): List<ValidationError> {
        return userErrors + credentialErrors + phoneErrors
    }
}