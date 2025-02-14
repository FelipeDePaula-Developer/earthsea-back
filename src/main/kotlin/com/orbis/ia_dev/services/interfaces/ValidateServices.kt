package com.earthsea.ia_dev.services.interfaces

import java.util.regex.Pattern

interface ValidateServices {
    fun checkEmail(email: String?): Boolean {
        val regexEmail = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$"
        return Pattern.matches(regexEmail, email)
    }

    fun checkCPF(cpf: String?): Boolean {
        if (cpf.isNullOrEmpty()) return false

        val cleanedCpf = cpf.replace("\\D+".toRegex(), "")
        if (cleanedCpf.length != 11) return false

        val firstPartCpf = cleanedCpf.take(9)
        val firstVerifyDigitCpf = calculateVerifyDigit(firstPartCpf, 2)

        val secondPartCpf = firstPartCpf + firstVerifyDigitCpf
        val secondVerifyDigitCpf = calculateVerifyDigit(secondPartCpf, 2)

        val verifyNumbers = "$firstVerifyDigitCpf$secondVerifyDigitCpf"
        return verifyNumbers == cleanedCpf.takeLast(2)
    }

    fun calculateVerifyDigit(cpfPart: String, startMultiplier: Int): Int {
        val sum = cpfPart.reversed().foldIndexed(0) { index, acc, char ->
            acc + char.digitToInt() * (startMultiplier + index)
        }
        val modulo = sum % 11
        return if (modulo >= 2) 11 - modulo else 0
    }

    fun checkPassword(password: String?): Boolean {
        val regexPassword = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$"
        return Pattern.matches(regexPassword, password)
    }

    fun checkUsername(login: String?): Boolean {
        val regexLogin = "^[a-zA-Z0-9_-]{3,20}$"
        return Pattern.matches(regexLogin, login)
    }

    fun checkDDI(phoneDDI: String?): Boolean {
        val regexDDI = "^(?:\\d{1,4}\\s?)?(?:\\(?\\d{1,4}\\)?\\s?)?\\d{1,}$"
        return Pattern.matches(regexDDI, phoneDDI)
    }

    fun checkDDD(phoneDDD: String?): Boolean {
        val regexDDD = "^(1[1-9]|[2-9][0-9])$"
        return Pattern.matches(regexDDD, phoneDDD)
    }

    fun checkNumber(phoneNumber: String?): Boolean {
        val regexNumber = "^[0-9]{1,20}$"
        return Pattern.matches(regexNumber, phoneNumber)
    }

    fun checkTypeNumber(typeNumber: String?): Boolean {
        val types = ArrayList<String>()
        types.add("RE")
        types.add("CO")
        types.add("CE")
        types.add("CN")
        return types.contains(typeNumber)
    }
}