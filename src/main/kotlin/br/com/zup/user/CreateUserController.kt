package br.com.zup.user

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
import javax.inject.Inject
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Controller("/api/v1/user")
@ExecuteOn(TaskExecutors.IO)
class CreateUserController(@Inject private val userRepository: UserRepository) {

    private val LOGGER: Logger = LoggerFactory.getLogger(this::class.java)

    @Post
    @Transactional
    fun create(@Body @Valid createUserRequest: CreateUserRequest): HttpResponse<Any> {
        LOGGER.info("Creating new user.")
        val response = userRepository.save(createUserRequest.toModel())
        val uri = UriBuilder.of("/api/v1/user/{id}")
            .expand(mutableMapOf(Pair("id", response.id.toString())))
        return HttpResponse.created(uri)
    }
}