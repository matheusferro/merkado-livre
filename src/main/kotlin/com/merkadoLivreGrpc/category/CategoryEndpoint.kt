package com.merkadoLivreGrpc.category

import com.merkadoLivreGrpc.exceptionHandler.ErrorHandler
import com.merkadoLivreGrpc.testeSNSeSQS.SqsTest
import io.grpc.stub.StreamObserver
import java.security.InvalidParameterException
import javax.inject.Singleton

@Singleton
//@ErrorHandler
class CategoryEndpoint(val categoryService: CategoryService, private val sqs: SqsTest): CategoryServiceGrpc.CategoryServiceImplBase() {

    override fun createCategory(
        request: CreateCategoryRequest?,
        responseObserver: StreamObserver<CreateCategoryResponse>?
    ) {
        request ?: throw InvalidParameterException()
        responseObserver ?: throw InvalidParameterException()

        sqs.producer()

        sqs.consumer()

        //categoryService.save(CategoryModel(request.name, request.parent))

        responseObserver.onNext(CreateCategoryResponse.newBuilder().apply {
            message = "category saved!"
        }.build())
        responseObserver.onCompleted()
    }
}