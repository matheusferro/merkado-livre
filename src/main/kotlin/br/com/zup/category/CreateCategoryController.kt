package br.com.zup.category

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Controller("/api/v1/category")
class CreateCategoryController(val categoryRepository: CategoryRepository) {

    @Post
    @Transactional
    fun create(@Body @Valid createCategoryRequest : CreateCategoryRequest): HttpResponse<Any>{

        val category = createCategoryRequest.toModel(categoryRepository)
        categoryRepository.save(category)

        val uri = UriBuilder.of("/api/v1/category/{id}")
            .expand(mutableMapOf(Pair("id", category.id)))

        return HttpResponse.created(uri)
    }
}