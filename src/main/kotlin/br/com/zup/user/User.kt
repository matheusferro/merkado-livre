package br.com.zup.user

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Entity
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
class User(
    @field:NotBlank
    val email: String,

    @field:NotBlank
    var password: String
) {
    init {
        assert(this.password.length >= 6, { "The password length must be greater than 6." })
        this.password = BCryptPasswordEncoder().encode(this.password)
    }

    @Id
    //@GeneratedValue(strategy = )
    var id = UUID.randomUUID()

    val createdAt = LocalDateTime.now()
}