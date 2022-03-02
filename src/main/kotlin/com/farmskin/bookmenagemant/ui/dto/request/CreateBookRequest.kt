package com.farmskin.bookmenagemant.ui.dto.request

import com.farmskin.bookmenagemant.domain.Book
import com.farmskin.bookmenagemant.domain.Category
import javax.validation.constraints.NotBlank

data class CreateBookRequest(

    @field:NotBlank(message = "잘못된 정보 입력")
    val title: String,

    @field:NotBlank(message = "잘못된 정보 입력")
    val author: String,

    @field:NotBlank(message = "잘못된 정보 입력")
    var categoryName: String
) {
    fun toBookEntity(rentStatus: String, category: Category) = Book(title, author, rentStatus, category)
}