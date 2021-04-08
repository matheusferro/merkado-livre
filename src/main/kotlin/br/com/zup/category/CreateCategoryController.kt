package br.com.zup.category

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.scheduling.TaskExecutors
import io.micronaut.scheduling.annotation.ExecuteOn
import io.micronaut.validation.Validated
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Controller("/api/v1/category")
@ExecuteOn(TaskExecutors.IO)
class CreateCategoryController(private val categoryRepository: CategoryRepository) {

    private val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)

    @Post
    @Transactional
    fun create(@Body @Valid createCategoryRequest: CreateCategoryRequest): HttpResponse<Any> {

        LOGGER.info("Creating new category: $createCategoryRequest")

        val category = createCategoryRequest.toModel(categoryRepository)
        categoryRepository.save(category)

        val uri = UriBuilder.of("/api/v1/category/{id}")
            .expand(mutableMapOf(Pair("id", category.id)))

        return HttpResponse.created(uri)
    }
}