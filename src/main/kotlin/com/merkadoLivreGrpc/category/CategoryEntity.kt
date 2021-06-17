package com.merkadoLivreGrpc.category

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity(name="category")
//@NamedStoredProcedureQuery(name = "nestedCategory", procedureName = "nested_category", parameters = [
//    StoredProcedureParameter(name = "cname",mode = ParameterMode.IN, type = String::class),
//    StoredProcedureParameter(name = "pname",mode = ParameterMode.IN, type = String::class)
//])
class CategoryEntity(
  @field:NotBlank
  val categoryName: String
){
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = true)
    var value_l: Long? = null

    @Column(nullable = true)
    var value_r: Long? = null

    @Column(updatable = false, nullable = false)
    val createdAt = LocalDateTime.now()
}