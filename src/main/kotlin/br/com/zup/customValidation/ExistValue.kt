package br.com.zup.customValidation

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.transaction.SynchronousTransactionManager
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.Connection
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [ExistValueValidator::class])
annotation class ExistValue(
    val message: String = "Value is invalid.",
    val domainClass: KClass<*>,
    val field: String,
    val isNullable: Boolean,
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class ExistValueValidator(
    private val entityManager: EntityManager,
    private val transactionManager: SynchronousTransactionManager<Connection>
) : ConstraintValidator<ExistValue, Any> {

    private val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)

    override fun isValid(
        value: Any?,
        annotationMetadata: AnnotationValue<ExistValue>,
        context: ConstraintValidatorContext
    ): Boolean {

        val klassName: String = annotationMetadata.classValue("domainClass").get().simpleName
        val field: String = annotationMetadata.stringValue("field").get()
        val nullable: Boolean = annotationMetadata.booleanValue("isNullable").get()

        LOGGER.info("Validating '$field' with value '$value'. Field can be null: $nullable")

        if (nullable && value == null) {
            //nullable accepted and the parameter is null
            return true
        } else if (!nullable && value == null) {
            //nullable is not accepted and the parameter is null. saves a query to the database returning now.
            return false
        }

        return transactionManager.executeRead {
            entityManager.createQuery("SELECT 1 FROM $klassName WHERE $field = :param")
                .setParameter("param", value)
                .resultList
        }.let {
            it.size >= 1
        }
    }
}