package com.earthsea.ia_dev.repositories

import com.earthsea.ia_dev.entities.User
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional
interface UserRepository : CrudRepository<User, Int> {
    @Query("SELECT u FROM User u WHERE u.cpf = :cpf")
    fun findByCpf(cpf: String): User?

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN TRUE ELSE FALSE END FROM User u WHERE u.cpf = :cpf")
    fun existsByCpf(cpf: String): Boolean
}
