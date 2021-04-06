package br.com.zup.customValidation

import br.com.zup.user.UserRepository
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import javax.inject.Singleton
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.reflect.KClass

@MustBeDocumented
@Target(FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [UniqueEmailValidator::class])
annotation class UniqueEmail(
    val message: String = "Email must be unique.",
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class UniqueEmailValidator(val userRepository: UserRepository) : ConstraintValidator<UniqueEmail, String> {
    override fun isValid(
        value: String?,
        annotationMetadata: AnnotationValue<UniqueEmail>,
        context: ConstraintValidatorContext
    ): Boolean {
        return !userRepository.existsByEmail(value)
    }
}
