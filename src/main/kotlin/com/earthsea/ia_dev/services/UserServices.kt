package com.earthsea.ia_dev.services

import com.earthsea.ia_dev.entities.Client
import com.earthsea.ia_dev.entities.PhoneNumber
import com.earthsea.ia_dev.entities.User
import com.earthsea.ia_dev.entities.interfaces.Person
import com.earthsea.ia_dev.forms.ClientForm
import com.earthsea.ia_dev.forms.UserForm
import com.earthsea.ia_dev.forms.results.PersonFormResult
import com.earthsea.ia_dev.repositories.ClientRepository
import com.earthsea.ia_dev.repositories.CredentialRepository
import com.earthsea.ia_dev.repositories.PhoneNumberRepository
import com.earthsea.ia_dev.repositories.UserRepository
import com.earthsea.ia_dev.services.interfaces.PhoneServicesInterface
import com.earthsea.ia_dev.services.interfaces.UserServicesInterface
import jakarta.transaction.Transactional
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.CrudRepository
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

@Service
class UserServices(
    @Autowired private val userRepository: UserRepository,
    @Autowired private val clientRepository: ClientRepository,
    @Autowired private val phoneNumberServices: PhoneNumberServices,
    @Autowired private val credentialServices: CredentialServices,
    private val phoneNumberRepository: PhoneNumberRepository,
) : UserServicesInterface,
    PhoneServicesInterface {

    @Transactional
    fun registerUser(userForm: UserForm): PersonFormResult {
        val personFormResult: PersonFormResult = validateUserForm(userForm)

        if (personFormResult.hasErrors()) {
            return personFormResult
        }

        val savedUser = saveOrUpdateByEmail(userForm.user, userRepository) { cpf -> userRepository.findByCpf(cpf) }

        if (userForm.phone.isNotEmpty()) {
            userForm.phone.forEach() { phone ->
                phone.user = savedUser
            }
            phoneNumberRepository.saveAll(userForm.phone)
        }

        userForm.credential.user = savedUser
        credentialServices.saveCredential(userForm.credential)

        return personFormResult
    }

    @Transactional
    fun registerClient(clientForm: ClientForm):PersonFormResult{
        val personFormResult: PersonFormResult = validateCommomFields(clientForm.client, clientForm.phone)
        if (personFormResult.hasErrors()){
            return personFormResult
        }

        val savedClient = saveOrUpdateByCPF(clientForm.client, clientRepository) { cpf -> clientRepository.findByCpf(cpf) }
        clientForm.phone.forEach() { phone ->
            phone.client = savedClient
        }

        phoneNumberRepository.saveAll(clientForm.phone)
        return personFormResult
    }

    fun validateUserForm(userForm: UserForm): PersonFormResult {
        val personFormResult = validateCommomFields(userForm.user, userForm.phone)

        if (!credentialServices.loginExists(userForm.credential)) {
            val retCredential: Map<String, String> = validateCredential(userForm.credential);
            retCredential.forEach() { (campo, erro) ->
                personFormResult.addUserError(campo, erro)
            }
        } else {
            personFormResult.addUserError("duplicate_login", "O login ${userForm.credential.login} já está em uso")
        }


        return personFormResult;
    }

    fun validateCommomFields(person: Person,  phone: List<PhoneNumber>) : PersonFormResult{
        val personFormResult = PersonFormResult();

        val retUser: Map<String, String> = validatePerson(person);

        retUser.forEach() { (campo, erro) ->
            personFormResult.addUserError(campo, erro)
        }

        if (phone.isNotEmpty()) {
            var skipPhoneValidation = false;
            phone.forEach { phoneNumber ->
                if (phoneNumberServices.phoneNumberExists(phoneNumber)) {
                    personFormResult.addUserError(
                        "duplicate_number",
                        "Combinação de DDI (${phoneNumber.phoneDDI}), DDD (${phoneNumber.phoneDDD}) e Numero ${phoneNumber.phoneNumber} já existem na base de dados"
                    )
                    skipPhoneValidation = true;
                }
            }

            if (!skipPhoneValidation) {
                val retPhones: Map<String, String> = validatePhones(phone);
                retPhones.forEach() { (campo, erro) ->
                    personFormResult.addUserError(campo, erro)
                }
            }
        }

        return personFormResult
    }

    fun <T> saveOrUpdateByCPF(
        entity: T,
        repository: CrudRepository<T, Int>,
        findByCpf: (String) -> T?
    ): T where T : Person {
        val existingEntity = entity.cpf?.let { findByCpf(it) }
        return if (existingEntity != null) {
            existingEntity.name = entity.name
            existingEntity.email = entity.email
            existingEntity.status = entity.status
            repository.save(existingEntity)
        } else {
            repository.save(entity)
        }
    }

    fun <T> saveOrUpdateByEmail(
        entity: T,
        repository: CrudRepository<T, Int>,
        findByEmail: (String) -> T?
    ): T where T : Person {
        val existingEntity = entity.email?.let { findByEmail(it) }
        return if (existingEntity != null) {
            existingEntity.name = entity.name
            existingEntity.email = entity.email
            existingEntity.status = entity.status
            repository.save(existingEntity)
        } else {
            repository.save(entity)
        }
    }
}