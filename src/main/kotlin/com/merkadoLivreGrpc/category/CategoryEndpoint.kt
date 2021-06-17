package com.merkadoLivreGrpc.category

import com.merkadoLivreGrpc.exceptionHandler.ErrorHandler
import io.grpc.stub.StreamObserver
import java.security.InvalidParameterException
import javax.inject.Singleton

@Singleton
@ErrorHandler
class CategoryEndpoint(val categoryService: CategoryService): CategoryServiceGrpc.CategoryServiceImplBase() {

    override fun createCategory(
        request: CreateCategoryRequest?,
        responseObserver: StreamObserver<CreateCategoryResponse>?
    ) {
        request ?: throw InvalidParameterException()
        responseObserver ?: throw InvalidParameterException()

        categoryService.save(CategoryModel(request.name, request.parent))

        responseObserver.onNext(CreateCategoryResponse.newBuilder().apply {
            message = "category saved!"
        }.build())
        responseObserver.onCompleted()
    }
}