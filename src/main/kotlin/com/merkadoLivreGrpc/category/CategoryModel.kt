package com.merkadoLivreGrpc.category

import io.micronaut.core.annotation.Introspected

@Introspected
data class CategoryModel(
    val name: String,
    val parent: String
)
