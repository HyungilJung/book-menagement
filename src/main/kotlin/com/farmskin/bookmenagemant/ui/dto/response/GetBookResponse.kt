package com.farmskin.bookmenagemant.ui.dto.response

import com.farmskin.bookmenagemant.domain.Book

data class GetBookResponse(

    val book_id: Long,

    val title: String,

    val author: String,

    val rent_status: String,

    val category: GetCategoryResponse
) {
    companion object {
        fun from(book: Book) =
            GetBookResponse(book.id, book.title, book.author, book.rentStatus, GetCategoryResponse(book.category.id, book.category.name))
    }
}