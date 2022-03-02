package com.farmskin.bookmenagemant.application

import com.farmskin.bookmenagemant.domain.BookRepository
import com.farmskin.bookmenagemant.domain.Category
import com.farmskin.bookmenagemant.domain.CategoryRepository
import com.farmskin.bookmenagemant.global.exception.PostExistsException
import com.farmskin.bookmenagemant.global.exception.PostNotFoundException
import com.farmskin.bookmenagemant.ui.dto.request.CreateCategoryRequest
import com.farmskin.bookmenagemant.ui.dto.response.GetBookResponse
import com.farmskin.bookmenagemant.ui.dto.response.GetCategoryResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val bookRepository: BookRepository
) {

    fun createCategory(request: CreateCategoryRequest): GetCategoryResponse {
        existsCategoryByName(request.name)
        val category = Category(request.name)

        return GetCategoryResponse.from(categoryRepository.save(category))
    }

    @Transactional(readOnly = true)
    fun searchBooksByCategory(id: Int, pageable: Pageable): List<GetBookResponse> {
        existsCategoryByCategoryId(id)
        val booksByCategory = findBookAllByCategoryId(id, pageable)

        return booksByCategory.map { GetBookResponse.from(it) }
    }


    private fun findBookAllByCategoryId(id: Int, pageable: Pageable) =
        bookRepository.findAllByCategoryIdWithFetchJoin(id, pageable)

    private fun existsCategoryByName(name: String) {
        if(categoryRepository.existsByName(name)) {
            throw PostExistsException()
        }
    }

    private fun existsCategoryByCategoryId(id: Int) {
        if (!categoryRepository.existsById(id)) {
            throw PostNotFoundException()
        }
    }
}