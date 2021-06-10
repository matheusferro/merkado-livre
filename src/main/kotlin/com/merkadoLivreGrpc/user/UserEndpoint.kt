package com.merkadoLivreGrpc.user

import com.merkadoLivreGrpc.CreateUserRequest
import com.merkadoLivreGrpc.CreateUserResponse
import com.merkadoLivreGrpc.UserServiceGrpc
import com.merkadoLivreGrpc.exceptionHandler.ErrorHandler
import io.grpc.stub.StreamObserver
import java.security.InvalidParameterException
import javax.inject.Singleton

@Singleton
@ErrorHandler
class UserEndpoint(val userService: UserService) : UserServiceGrpc.UserServiceImplBase() {

    override fun createUser(request: CreateUserRequest?, responseObserver: StreamObserver<CreateUserResponse>?) {
        request ?: throw InvalidParameterException()
        responseObserver ?: throw InvalidParameterException()

        val response = userService.createUser(UserModel(email = request.email, password = request.password))

        responseObserver.onNext(CreateUserResponse.newBuilder().apply {
            message = "saved ${response.email}"
        }.build())
        responseObserver.onCompleted()
    }
}