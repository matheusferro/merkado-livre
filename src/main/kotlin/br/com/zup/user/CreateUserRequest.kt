package br.com.zup.user

import br.com.zup.customValidation.NotExistValue
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Introspected
data class CreateUserRequest(
    @field:NotBlank
    @field:Email
    @field:NotExistValue(domainClass = User::class, field = "email")
    val email: String,

    @field:Size(min = 6)
    @field:NotBlank
    val password: String
) {
    fun toModel(): User {
        return User(
            this.email,
            this.password
        )
    }
}
