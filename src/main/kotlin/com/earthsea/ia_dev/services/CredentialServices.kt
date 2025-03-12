package com.earthsea.ia_dev.services

import com.earthsea.ia_dev.config.JwtService
import com.earthsea.ia_dev.entities.Credential
import com.earthsea.ia_dev.forms.AuthUserForm
import com.earthsea.ia_dev.repositories.CredentialRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.security.SecureRandom
import java.security.spec.KeySpec
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import java.util.HexFormat

@Service
class CredentialServices(@Autowired private val credentialRepository: CredentialRepository, private val jwtService: JwtService) {
    private val ALGORITHM = "PBKDF2WithHmacSHA512";
    private val ITERATIONS = 120_000
    private val KEY_LENGTH = 256
    private val SECRET = "zxYtQ4oc_EDIKNq"

    private fun ByteArray.toHexString(): String =
        HexFormat.of().formatHex(this)

    fun loginExists(credential: Credential): Boolean {
        return credentialRepository.existsByLogin(credential.login);
    }

    fun generateRandomSalt()  :String{
        val random = SecureRandom();
        val salt = ByteArray(16);
        random.nextBytes(salt);

        return salt.toHexString();
    }

    fun generateHash(password: String, salt: String?): String {
        val combinedSalt = "$salt$SECRET".toByteArray()
        val factory: SecretKeyFactory = SecretKeyFactory.getInstance(ALGORITHM)
        val spec: KeySpec = PBEKeySpec(password.toCharArray(), combinedSalt, ITERATIONS, KEY_LENGTH)
        val key: SecretKey = factory.generateSecret(spec)
        val hash: ByteArray = key.encoded
        return hash.toHexString()
    }

    fun saveCredential(credential: Credential){
        val salt = generateRandomSalt()
        credential.salt = salt
        credential.password = generateHash(credential.password ?: "empty", salt)
        credentialRepository.save(credential)
    }

    fun authenticate(authUserForm: AuthUserForm): Any? {
        val credential = credentialRepository.findByLogin(authUserForm.login)

        if (credential == null) {
            return false
        }

        val hashedPassword = generateHash(authUserForm.password, credential.salt)
        return if (hashedPassword == credential.password) jwtService.generateToken(credential.user?.idUser!!) else null
    }
}