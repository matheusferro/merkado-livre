package com.merkadoLivreGrpc.category

import io.grpc.Status
import io.micronaut.transaction.SynchronousTransactionManager
import io.micronaut.validation.Validated
import java.sql.Connection
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.ParameterMode
import javax.validation.Valid

@Singleton
@Validated
class CategoryService(
    private val entityManager: EntityManager,
    private val transactionManager: SynchronousTransactionManager<Connection>
) {

    /**
     * *Nasted sets*
     *
     * TODO: search
     */
    fun save(@Valid categoryModel: CategoryModel): String {

        val parentCategory = transactionManager.executeRead {
            entityManager.createQuery(
                "SELECT C FROM category C WHERE C.categoryName = :pName",
                CategoryEntity::class.java
            )
                .setParameter("pName", categoryModel.parent)
                .resultList
        }

        val result = transactionManager.executeWrite {
            val sp = entityManager.createStoredProcedureQuery("nested_categoryfun")
            sp.registerStoredProcedureParameter("cname", String::class.java, ParameterMode.IN)
                .setParameter("cname", categoryModel.name)
            sp.registerStoredProcedureParameter("pname", String::class.java, ParameterMode.IN)
                .setParameter("pname", parentCategory[0].categoryName)
            sp.registerStoredProcedureParameter("msg", String::class.java, ParameterMode.INOUT)
                .setParameter("msg", "Cadastro com sucesso")
            val result = sp.execute()
            return@executeWrite result;
        }

        return when {
            result -> "Cadastrado!";
            else -> throw Status.UNAVAILABLE.withDescription("Não foi possível realizar o cadastro").asException()
        }
    }
}



