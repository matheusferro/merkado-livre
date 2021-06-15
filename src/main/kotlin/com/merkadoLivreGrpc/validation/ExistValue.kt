package com.merkadoLivreGrpc.validation

import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.transaction.SynchronousTransactionManager
import io.micronaut.validation.validator.constraints.ConstraintValidator
import io.micronaut.validation.validator.constraints.ConstraintValidatorContext
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
@Constraint(validatedBy = [UniqueFieldValidator::class])
annotation class UniqueField(
    val message: String = "has not valid fields.",
    val domainKlass: KClass<*>,
    val fieldName: String,
    val groups: Array<KClass<Any>> = [],
    val payload: Array<KClass<Payload>> = []
)

@Singleton
class UniqueFieldValidator(private val entityManager: EntityManager,
                           private val transactionManager: SynchronousTransactionManager<Connection> ): ConstraintValidator<UniqueField, Any> {
    override fun isValid(
        value: Any?,
        annotationMetadata: AnnotationValue<UniqueField>,
        context: ConstraintValidatorContext
    ): Boolean {
        val klassName = annotationMetadata.classValue("domainKlass").get().simpleName
        val fieldName = annotationMetadata.stringValue("fieldName").get()

        return transactionManager.executeRead {
            entityManager.createQuery("SELECT 1 FROM $klassName WHERE $fieldName = :pVal")
                .setParameter("pVal", value)
                .resultList
        }.size < 1
    }

}