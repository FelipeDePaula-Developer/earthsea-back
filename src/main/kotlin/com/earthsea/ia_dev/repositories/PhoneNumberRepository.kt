package com.earthsea.ia_dev.repositories

import com.earthsea.ia_dev.entities.PhoneNumber
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface PhoneNumberRepository : CrudRepository<PhoneNumber, Int>{
    @Query("Select CASE WHEN COUNT(p) > 0 THEN TRUE ELSE FALSE END from PhoneNumber p where p.phoneDDI = :ddi and p.phoneDDD = :ddd and p.phoneNumber = :number")
    fun existsByPhoneDDIAndPhoneDDDAndPhoneNumber(ddi : String, ddd:String, number:String) : Boolean;

}
