package br.com.zup.category

import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class Category(

    @field:NotBlank
    @Column(nullable = false, unique = true)
    val name: String,

    @ManyToOne(fetch = FetchType.EAGER)
    val parent: Category?
) {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null

    @Column(nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
}
