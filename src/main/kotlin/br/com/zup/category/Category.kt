package br.com.zup.category

import java.time.LocalDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class Category(

    @field:NotBlank
    val name: String,

    @ManyToOne(fetch = FetchType.EAGER)
    val parent: Category?
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0

    val createdAt = LocalDateTime.now()
}
