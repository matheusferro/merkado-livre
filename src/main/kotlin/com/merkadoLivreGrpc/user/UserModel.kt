package com.merkadoLivreGrpc.user

import io.micronaut.core.annotation.Introspected
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class UserModel(

    @field:NotBlank
    @field:Email
    val email: String,

    @field:Size(min = 6)
    @field:NotBlank
    var password: String
) {
    init {
        if(this.password.length > 6) {
            this.password = BCryptPasswordEncoder().encode(this.password)
        }
    }
}
