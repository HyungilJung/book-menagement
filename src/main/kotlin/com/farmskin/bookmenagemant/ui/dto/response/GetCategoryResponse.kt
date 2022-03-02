package com.farmskin.bookmenagemant.ui.dto.response

import com.farmskin.bookmenagemant.domain.Category

data class GetCategoryResponse(

    val category_id: Int,

    val category_name: String
) {
    companion object {
        fun from(category: Category) = GetCategoryResponse(category.id, category.name)
    }
}