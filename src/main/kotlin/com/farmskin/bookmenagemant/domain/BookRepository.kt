package com.farmskin.bookmenagemant.domain

import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface BookRepository : JpaRepository<Book, Long> {

    fun existsByTitleAndCategoryId(title: String, CategoryId: Int) : Boolean

    @Query("select b from Book b join fetch b.category where b.author = :author AND b.title = :title")
    fun findByAuthorAndTitleWithFetchJoin(author: String, title: String): Book?

    @Query("select b from Book b join fetch b.category where b.category.id = :id order by b.title")
    fun findAllByCategoryIdWithFetchJoin(id: Int, pageable: Pageable): List<Book>

    fun existsByIdAndCategoryId(id: Long, CategoryId: Int): Boolean

    fun existsByIdAndRentStatus(id: Long, rentStatus: String): Boolean
}