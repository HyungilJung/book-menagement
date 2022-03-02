package com.farmskin.bookmenagemant.ui

import com.farmskin.bookmenagemant.application.BookService
import com.farmskin.bookmenagemant.ui.dto.request.CreateBookRequest
import io.swagger.annotations.ApiOperation
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("api/v1/books")
class BookRestController(private val bookService: BookService) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "도서 생성", notes = "도서를 생성한다.")
    fun createBook(@RequestBody @Valid request: CreateBookRequest) = bookService.createBook(request)

    @GetMapping
    @ApiOperation(value = "지은이와 제목으로 도서 검색", notes = "지은이와 제목으로 도서를 검색한다.")
    fun searchBookWithAuthorAndTitle(@RequestParam("author") author: String, @RequestParam("title") title: String) =
        bookService.searchBookWithAuthorAndTitle(author, title)

    @PatchMapping("{id}/categories")
    @ApiOperation(value = "도서의 카테고리를 변경", notes = "도서의 카테고리를 변경한다.")
    fun updateCategoryOfBook(@PathVariable("id") id: Long, @RequestParam("category_id") categoryId: Int) {
        bookService.updateCategoryOfBook(id, categoryId)
    }

    @PatchMapping("{id}/rent-status")
    @ApiOperation(value = "도서의 대여 가능 여부 상태를 수정", notes = "도서의 대여 가능 여부 상태를 수정한다.")
    fun updateRentStatusOfBook(@PathVariable("id") id: Long, @RequestParam("rent_status") rentStatus: String) {
        bookService.updateRentStatusOfBook(id, rentStatus)
    }


}