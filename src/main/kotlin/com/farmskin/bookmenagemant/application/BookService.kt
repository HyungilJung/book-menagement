package com.farmskin.bookmenagemant.application

import com.farmskin.bookmenagemant.domain.BookRepository
import com.farmskin.bookmenagemant.domain.CategoryRepository
import com.farmskin.bookmenagemant.global.enumeration.RentStatusType
import com.farmskin.bookmenagemant.global.enumeration.RentStatusType.RENTING
import com.farmskin.bookmenagemant.global.enumeration.RentStatusType.POSSIBILITY
import com.farmskin.bookmenagemant.global.enumeration.RentStatusType.STOP
import com.farmskin.bookmenagemant.global.exception.PostExistsException
import com.farmskin.bookmenagemant.global.exception.PostInvalidException
import com.farmskin.bookmenagemant.global.exception.PostNotFoundException
import com.farmskin.bookmenagemant.ui.dto.request.CreateBookRequest
import com.farmskin.bookmenagemant.ui.dto.response.GetBookResponse
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class BookService(
    private val bookRepository: BookRepository,
    private val categoryRepository: CategoryRepository
) {
    fun createBook(request: CreateBookRequest): GetBookResponse {
        val category = findCategoryByName(request.categoryName)
        existsBookByTitleAndCategoryId(request.title, category.id)
        val book = request.toBookEntity(POSSIBILITY.status, category)

        return GetBookResponse.from(bookRepository.save(book))
    }

    @Transactional
    fun updateCategoryOfBook(id: Long, categoryId: Int) {
        existsCategoryByIdAndCategoryId(id, categoryId)

        val book = findBookById(id)
        val category = findCategoryById(categoryId)

        book.updateCategory(category)
    }

    @Transactional(readOnly = true)
    fun searchBookWithAuthorAndTitle(author: String, title: String): GetBookResponse {
        val book = findBookByAuthorAndTitle(author, title)

        return GetBookResponse.from(book)
    }

    @Transactional
    fun updateRentStatusOfBook(id: Long, rentStatus: String) {
        invalidRentStatus(rentStatus)
        existsBookByIdAndRentStatus(id, rentStatus)

        val book = findBookById(id)

        book.updateStatus(rentStatus)
    }

    private fun findBookById(id: Long) = bookRepository.findByIdOrNull(id) ?: throw PostNotFoundException()

    private fun findCategoryById(categoryId: Int) =
        categoryRepository.findByIdOrNull(categoryId) ?: throw PostNotFoundException()

    private fun findCategoryByName(categoryName: String) =
        categoryRepository.findByName(categoryName) ?: throw PostNotFoundException()

    private fun existsBookByTitleAndCategoryId(title: String, categoryId: Int) {
        if(bookRepository.existsByTitleAndCategoryId(title, categoryId)) {
            throw PostExistsException()
        }
    }

    private fun invalidRentStatus(rentStatus: String) {
        if (rentStatus != RENTING.status && rentStatus != POSSIBILITY.status && rentStatus != STOP.status) {
            throw PostInvalidException("대여 가능, 대여중, 대여 중단만 입력하실 수 있습니다.")
        }
    }

    private fun existsBookByIdAndRentStatus(id: Long, rentStatus: String) {
        if (bookRepository.existsByIdAndRentStatus(id, rentStatus)) {
            throw PostInvalidException("현재 상태와 동일 요청입니다.")
        }
    }

    private fun existsCategoryByIdAndCategoryId(id: Long, categoryId: Int) {
        if (bookRepository.existsByIdAndCategoryId(id, categoryId)) {
            throw PostExistsException("이미 속해있는 카테고리와 동일한 요청입니다.")
        }
    }

    private fun findBookByAuthorAndTitle(author: String, title: String) =
        bookRepository.findByAuthorAndTitleWithFetchJoin(author, title) ?: throw PostNotFoundException()
}