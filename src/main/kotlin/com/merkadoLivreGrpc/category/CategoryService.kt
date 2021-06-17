package com.merkadoLivreGrpc.category

import io.micronaut.transaction.SynchronousTransactionManager
import io.micronaut.validation.Validated
import java.sql.Connection
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.persistence.ParameterMode
import javax.transaction.Transactional
import javax.validation.Valid

@Singleton
@Validated
class CategoryService(
    private val entityManager: EntityManager,
    private val transactionManager: SynchronousTransactionManager<Connection>
) {

    @Transactional
    fun save(@Valid categoryModel: CategoryModel) {

        val parentCategory = //transactionManager.executeRead {
            entityManager.createQuery(
                "SELECT C FROM category C WHERE C.categoryName = :pName",
                CategoryEntity::class.java
            )
                .setParameter("pName", categoryModel.parent)
                .resultList
        //}


        val sp = entityManager.createStoredProcedureQuery("nested_categoryfun")
        sp.registerStoredProcedureParameter("cname", String::class.java, ParameterMode.IN)
            .setParameter("cname", "teste")
        sp.registerStoredProcedureParameter("pname", String::class.java, ParameterMode.IN)
            .setParameter("pname", "categoryteste")
        sp.registerStoredProcedureParameter("msg", String::class.java, ParameterMode.INOUT)
            .setParameter("msg", "teste")
        sp.execute()
//        transactionManager.executeWrite {
//            entityManager.createNativeQuery("call nested_category(:pCname, :pPname)")
//                .setParameter("pCname", "teste")
//                .setParameter("pPname","categoryteste")
//                .
//        }
        return
    }
}



