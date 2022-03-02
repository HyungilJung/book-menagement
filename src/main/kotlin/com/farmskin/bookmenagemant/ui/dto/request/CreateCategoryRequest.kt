package com.farmskin.bookmenagemant.ui.dto.request

import javax.validation.constraints.NotBlank

data class CreateCategoryRequest(

    @field:NotBlank(message = "잘못된 정보 입력")
    val name: String
)