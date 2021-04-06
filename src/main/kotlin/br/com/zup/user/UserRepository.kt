package br.com.zup.user

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jpa.repository.JpaRepository
import java.util.*

@Repository
interface UserRepository: JpaRepository<User, UUID> {
}