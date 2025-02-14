package com.earthsea.ia_dev.services.interfaces

import com.earthsea.ia_dev.entities.PhoneNumber

interface PhoneServicesInterface:ValidateServices {

    fun validatePhones(phonesNumbers: List<PhoneNumber>): Map<String, String> {
        val ret = mutableMapOf<String, String>()
        phonesNumbers.forEach { phoneNumber ->
            val ddiCheck = checkDDI(phoneNumber.phoneDDI)
            if (!ddiCheck) {
                ret["ddi"] = "DDI ${phoneNumber.phoneDDI} is invalid"
            }

            val dddCheck = checkDDD(phoneNumber.phoneDDD)
            if (!dddCheck) {
                ret["ddd"] = "DDD ${phoneNumber.phoneDDD} is invalid"
            }

            val numberCheck = checkNumber(phoneNumber.phoneNumber)
            if (!numberCheck) {
                ret["phone_number"] = "Phone number ${phoneNumber.phoneNumber} is invalid"
            }

            val typeCheck = checkTypeNumber(phoneNumber.phoneType)
            if (!typeCheck) {
                ret["type_number"] = "Phone type ${phoneNumber.phoneType} is invalid"
            }
        }

        return ret
    }

}