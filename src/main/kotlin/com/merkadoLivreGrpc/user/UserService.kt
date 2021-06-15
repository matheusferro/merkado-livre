package com.merkadoLivreGrpc.user

import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.validation.Valid

@Validated
@Singleton
class UserService(val userRepository: UserRepository) {

    fun createUser(@Valid userModel: UserModel): UserEntity = userRepository.save(UserEntity(userModel.email, userModel.password))
}