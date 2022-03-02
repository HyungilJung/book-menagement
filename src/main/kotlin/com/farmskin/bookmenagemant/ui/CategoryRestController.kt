package com.farmskin.bookmenagemant.ui

import com.farmskin.bookmenagemant.application.CategoryService
import com.farmskin.bookmenagemant.ui.dto.request.CreateBookRequest
import com.farmskin.bookmenagemant.ui.dto.request.CreateCategoryRequest
import io.swagger.annotations.ApiOperation
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("api/v1/categories")
class CategoryRestController(private val categoryService: CategoryService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "카테고리 생성", notes = "카테고리를 생성한다.")
    fun createCategory(@RequestBody @Valid request: CreateCategoryRequest) = categoryService.createCategory(request)

    @GetMapping("{id}/books")
    @ApiOperation(value = "카테고리 별 도서를 검색하고 도서 이름을 오름차순으로 정렬", notes = "카테고리 별로 도서를 검색하고 도서 이름을 오름차순으로 정렬한다.")
    fun searchBooksByCategory(@PathVariable("id") id: Int, pageable: Pageable) =
        categoryService.searchBooksByCategory(id, pageable)
}