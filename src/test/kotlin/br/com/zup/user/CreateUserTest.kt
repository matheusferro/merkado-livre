package br.com.zup.user

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.RxHttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import javax.inject.Inject

@MicronautTest
class CreateUserTest(val userRepository: UserRepository) {

    @field:Inject
    @field:Client("/")
    lateinit var clientPix: RxHttpClient

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        userRepository.save(User("test@default.com", "123456"))
    }

    @Test
    fun `should create a new user`() {
        val request = HttpRequest.POST(
            "/api/v1/user",
            CreateUserRequest(
                email = "test@email.com",
                password = "secretPassword"
            )
        )

        val response = clientPix.toBlocking().exchange(
            request,
            Any::class.java
        )
        assert(userRepository.existsByEmail("test@email.com"))
        assert(response.status.equals(HttpStatus.CREATED))
    }

    @Test
    fun `should validate unique email`() {
        val request = HttpRequest.POST(
            "/api/v1/user",
            CreateUserRequest(
                email = "test@default.com",
                password = "password"
            )
        )

        val exception = assertThrows<HttpClientResponseException> {
            clientPix.toBlocking().exchange(
                request,
                Any::class.java
            )
        }
        assert(exception.status.equals(HttpStatus.BAD_REQUEST))
        assert(exception.message.equals("createUserRequest.email: Email must be unique."))
    }

    @Test
    fun `should validate size of password`() {
        val request = HttpRequest.POST(
            "/api/v1/user",
            CreateUserRequest(
                email = "test@email.com",
                password = "123"
            )
        )

        //Validation exception throws in test HttpClientResponseException
        val exception = assertThrows<HttpClientResponseException> {
            clientPix.toBlocking().exchange(
                request,
                Any::class.java
            )
        }
        assert(exception.status.equals(HttpStatus.BAD_REQUEST))
        assert(exception.message.equals("createUserRequest.password: size must be between 6 and 2147483647"))
    }
}