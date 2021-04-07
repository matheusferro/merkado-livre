package br.com.zup.category

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Positive


data class CreateCategoryRequest(

    @field:NotBlank
    val name: String,

    @field:Positive
    val parent: Long?
) {
    fun toModel(categoryRepository: CategoryRepository): Category {
        val categoryParent = parent?.let { categoryRepository.findById(it) }
        return Category(name, categoryParent?.get())
    }
}
