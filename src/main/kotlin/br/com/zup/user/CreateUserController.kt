package br.com.zup.user

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.http.uri.UriBuilder
import io.micronaut.validation.Validated
import javax.inject.Inject
import javax.validation.Valid

@Validated
@Controller("/api/v1/user")
class CreateUserController(@Inject val userRepository: UserRepository) {

    @Post
    fun create(@Valid createUserRequest: CreateUserRequest): HttpResponse<Any> {

        val response = userRepository.save(createUserRequest.toModel())
        val uri = UriBuilder.of("/api/v1/{id}")
            .expand(mutableMapOf(Pair("id", response.id.toString())))
        return HttpResponse.created(uri)
    }
}