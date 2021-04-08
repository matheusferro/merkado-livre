package br.com.zup.category

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.inject.Inject

@MicronautTest(transactional = false)
internal class CreateCategoryControllerTest(private val categoryRepository: CategoryRepository) {

    @field:Inject
    @field:Client("/")
    lateinit var clientRest: RxHttpClient

    @BeforeEach
    fun setUp(){
        categoryRepository.deleteAll()
    }

    @Test
    fun `should create a new category without parent`() {

        val request = HttpRequest.POST(
            "/api/v1/category",
            CreateCategoryRequest(
                name = "Car",
                parent = null
            )
        )

        val response = clientRest.toBlocking().exchange(request, Any::class.java)

        with(response) {
            assertEquals(HttpStatus.CREATED, this.status)
            assertNotNull(response.header("Location"))
        }
        assert(categoryRepository.findAll().size == 1)
    }

    @Test
    fun `should create a new  category with parent`() {
        //Foi necessário adicionar o "transactional = false".

        val parentCategory = categoryRepository.save(
            Category("Car", null)
        )

        val request = HttpRequest.POST(
            "/api/v1/category",
            CreateCategoryRequest(
                name = "Headlight",
                parent = parentCategory.id
            )
        )

        val response = clientRest.toBlocking().exchange(request, Any::class.java)

        with(response) {
            assertEquals(HttpStatus.CREATED, this.status)
            assertNotNull(response.header("Location"))
        }
        assert(categoryRepository.findAll().size == 2)
    }

    @Test
    fun `should not create new category (unique category)`() {
        categoryRepository.save(
            Category("Car", null)
        )

        val request = HttpRequest.POST(
            "/api/v1/category",
            CreateCategoryRequest(
                name = "Car",
                parent = null
            )
        )

        val responseException = assertThrows<HttpClientResponseException> {
            clientRest.toBlocking().exchange(request, Any::class.java)
        }

        with(responseException){
            assertEquals(HttpStatus.BAD_REQUEST, this.status)
            assertEquals("createCategoryRequest.name: Value already exists.", this.message)
        }
        assert(categoryRepository.findAll().size == 1)
    }

    @Test
    fun `should not create new category (exist parent)`() {

        val request = HttpRequest.POST(
            "/api/v1/category",
            CreateCategoryRequest(
                name = "Car",
                parent = 42
            )
        )

        val responseException = assertThrows<HttpClientResponseException> {
            clientRest.toBlocking().exchange(request, Any::class.java)
        }

        with(responseException){
            assertEquals(HttpStatus.BAD_REQUEST, this.status)
            assertEquals("createCategoryRequest.parent: Value is invalid.", this.message)
        }
        assert(categoryRepository.findAll().size == 0)
    }

}