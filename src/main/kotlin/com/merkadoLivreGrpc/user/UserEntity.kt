package com.merkadoLivreGrpc.user

import java.time.LocalDateTime
import java.util.*
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name="users")
class UserEntity(
    @field:NotBlank
    @Column(unique = true)
    val email: String,

    @field:NotNull
    val password: String
){
    @Id
    var id: UUID = UUID.randomUUID()

    @Column(updatable = false, nullable = false)
    val createdAt = LocalDateTime.now()
}
