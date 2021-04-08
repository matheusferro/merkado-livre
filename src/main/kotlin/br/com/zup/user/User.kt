package br.com.zup.user

import org.hibernate.annotations.Type
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.NotBlank

@Entity
class User(
    @field:NotBlank
    @Column(nullable = false, unique = true)
    val email: String,

    @field:NotBlank
    var password: String
) {
    init {
        assert(this.password.length >= 6) { "The password length must be greater than 6." }
        this.password = BCryptPasswordEncoder().encode(this.password)
    }

    @Id
    @Type(type = "org.hibernate.type.UUIDCharType")
    @GeneratedValue
    var id: UUID? = null

    @Column(nullable = false, updatable = false)
    val createdAt = LocalDateTime.now()
}