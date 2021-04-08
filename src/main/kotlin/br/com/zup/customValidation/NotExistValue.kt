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
@Constraint(validatedBy = [NotExistValueValidator::class])
annotation class NotExistValue(
    val message: String = "Value already exists.",
    val domainClass: KClass<*>,
    val field: String,
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class NotExistValueValidator(
    private val entityManager: EntityManager,
    private val transactionManager: SynchronousTransactionManager<Connection>
) : ConstraintValidator<NotExistValue, Any> {

    private val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)

    override fun isValid(
        value: Any?,
        annotationMetadata: AnnotationValue<NotExistValue>,
        context: ConstraintValidatorContext
    ): Boolean {

        val klassName: String = annotationMetadata.classValue("domainClass").get().simpleName
        val field: String = annotationMetadata.stringValue("field").get()

        LOGGER.info("Verifying field '$field' with value '$value' already exists.")

        return transactionManager.executeRead {
            entityManager.createQuery("SELECT 1 FROM $klassName WHERE $field = :param")
                .setParameter("param", value)
                .resultList
        }.let {
            it.size == 0
        }
    }
}