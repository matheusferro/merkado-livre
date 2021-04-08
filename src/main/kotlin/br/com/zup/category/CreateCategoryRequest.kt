package br.com.zup.category

import br.com.zup.customValidation.ExistValue
import br.com.zup.customValidation.NotExistValue
import io.micronaut.core.annotation.Introspected
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive


@Introspected
data class CreateCategoryRequest(

    @field:NotBlank
    @field:NotExistValue(domainClass = Category::class, field = "name")
    val name: String,

    @field:Positive
    @field:ExistValue(domainClass = Category::class, field = "id", isNullable = true)
    val parent: Long?
) {
    fun toModel(categoryRepository: CategoryRepository): Category {
        val categoryParent = parent?.let { categoryRepository.findById(it) }
        return Category(name, categoryParent?.get())
    }
}
