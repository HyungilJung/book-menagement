package com.farmskin.bookmenagemant.domain

import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, Int> {

    fun findByName(name: String): Category?

    fun existsByName(name: String): Boolean
}