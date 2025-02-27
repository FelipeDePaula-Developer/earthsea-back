package com.earthsea.ia_dev.services

import com.earthsea.ia_dev.entities.PhoneNumber
import com.earthsea.ia_dev.repositories.PhoneNumberRepository
import com.earthsea.ia_dev.repositories.UserRepository
import com.earthsea.ia_dev.services.interfaces.PhoneServicesInterface
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class PhoneNumberServices(@Autowired private val phoneNumberRepository: PhoneNumberRepository):PhoneServicesInterface {
    fun phoneNumberExists(phoneNumber: PhoneNumber) : Boolean{
        return phoneNumberRepository.existsByPhoneDDIAndPhoneDDDAndPhoneNumber(
            phoneNumber.phoneDDI ?: "",
            phoneNumber.phoneDDD ?: "",
            phoneNumber.phoneNumber ?: "")
    }
}