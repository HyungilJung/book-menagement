package com.farmskin.bookmenagemant.ui

import com.farmskin.bookmenagemant.application.BookService
import com.farmskin.bookmenagemant.global.enumeration.RentStatusType
import com.farmskin.bookmenagemant.global.exception.PostExistsException
import com.farmskin.bookmenagemant.global.exception.PostInvalidException
import com.farmskin.bookmenagemant.global.exception.PostNotFoundException
import com.farmskin.bookmenagemant.ui.dto.request.CreateBookRequest
import com.farmskin.bookmenagemant.ui.dto.response.GetBookResponse
import com.farmskin.bookmenagemant.ui.dto.response.GetCategoryResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.doNothing
import org.mockito.Mockito.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter

@WebMvcTest(BookRestController::class)
internal class BookRestControllerTest @Autowired constructor(
    private val objectMapper: ObjectMapper,
    private val webApplicationContext: WebApplicationContext,
    private var mockMvc: MockMvc,
) {
    @MockBean
    private lateinit var bookService: BookService

    @BeforeEach
    fun setUp() {

        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .build()
    }

    @Test
    fun `도서 생성에 성공할 경우 Http Status Code 201(Created)과 생성된 도서 정보를 리턴`() {
        val category = GetCategoryResponse(1, "문학")
        val request = CreateBookRequest("너에게 해주지 못한 말들", "권태영", "문학")
        val response = GetBookResponse(1, "너에게 해주지 못한 말들", "권태영", RentStatusType.POSSIBILITY.status, category)

        given(bookService.createBook(request)).willReturn(response)

        mockMvc.perform(
            post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun `잘못된 정보 입력으로 인해 도서 생성에 실패할 경우 Http Status Code 400(Bad Request)와 예외 메시지를 리턴`() {
        val request = CreateBookRequest("", " ", "문학")

        mockMvc.perform(
            post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `존재하지 않는 카테고리로 인해 도서 생성에 실패할 경우 Http Status Code 404(Not Found)와 예외 메시지를 리턴`() {
        val request = CreateBookRequest("너에게 해주지 못한 말들", "권태영", "문학문학학")

        doThrow(PostNotFoundException("이미 존재하는 리소스입니다.")).`when`(bookService).createBook(request)

        mockMvc.perform(
            post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `요청한 카테고리안에 이미 존재하는 도서명으로 인해 도서 생성에 실패할 경우 Http Status Code 400(Bad Request)와 예외 메시지를 리턴`() {
        val request = CreateBookRequest("너에게 해주지 못한 말들", "권태영", "문학")

        doThrow(PostExistsException("이미 존재하는 리소스입니다.")).`when`(bookService).createBook(request)

        mockMvc.perform(
            post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `지은이와 제목으로 도서 검색에 성공할 경우 Http Status Code 200(Ok)과 생성된 도서 정보를 리턴`() {
        val category = GetCategoryResponse(1, "문학")
        val response = GetBookResponse(1, "너에게 해주지 못한 말들", "권태영", RentStatusType.POSSIBILITY.status, category)

        given(bookService.searchBookWithAuthorAndTitle("권태영", "너에게 해주지 못한 말들")).willReturn(response)

        mockMvc.perform(
            get("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("author", "권태영")
                .param("title", "너에게 해주지 못한 말들")
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `존재하지 않는 지은이와 제목으로 도서 검색에 실패할 경우 Http Status Code 400(Bad Request)과 에러메시지 리턴`() {

        doThrow(PostExistsException("존재하지 않는 리소스입니다.")).`when`(bookService)
            .searchBookWithAuthorAndTitle("권태영", "너에게 해주지 못한 말들")

        mockMvc.perform(
            get("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("author", "권태영")
                .param("title", "너에게 해주지 못한 말들")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `도서의 카테고리 변경에 성공할 경우 Http Status Code 200(Ok)를 리턴`() {

        doNothing().`when`(bookService)?.updateCategoryOfBook(1, 2)

        mockMvc.perform(
            patch("/api/v1/books/1/categories")
                .param("category_id", "2")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `이미 속해있는 카테고리와 동일한 요청으로 인해 도서의 카테고리 변경에 실패할 경우 Http Status Code 400(Bad Request)와 예외 메시지를 리턴`() {


        doThrow(PostExistsException("이미 속해있는 카테고리와 동일한 요청입니다.")).`when`(bookService).updateCategoryOfBook(1, 2)

        mockMvc.perform(
            patch("/api/v1/books/1/categories")
                .param("category_id", "2")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `존재하지 않는 도서명 요청으로 인해 도서의 카테고리 변경에 실패할 경우 Http Status Code 404(Not Found)와 예외 메시지를 리턴`() {


        doThrow(PostNotFoundException("존재하지 않는 리소스입니다.")).`when`(bookService).updateCategoryOfBook(100000, 2)

        mockMvc.perform(
            patch("/api/v1/books/100000/categories")
                .param("category_id", "2")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `존재하지 않는 카테고리명 요청으로 인해 도서의 카테고리 변경에 실패할 경우 Http Status Code 404(Not Found)와 예외 메시지를 리턴`() {


        doThrow(PostNotFoundException("존재하지 않는 리소스입니다.")).`when`(bookService).updateCategoryOfBook(1, 200000)

        mockMvc.perform(
            patch("/api/v1/books/1/categories")
                .param("category_id", "200000")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `도서의 대여 가능 여부 상태를 수정하는 것에 성공한 경우 Http Status Code 200(Ok)를 리턴`() {


        doNothing().`when`(bookService)?.updateRentStatusOfBook(1, RentStatusType.RENTING.status)

        mockMvc.perform(
            patch("/api/v1/books/1/rent-status")
                .param("rent_status", RentStatusType.RENTING.status)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `현재 상태와 동일한 도서의 대여 가능 여부 상태 입력으로 인해 수정하는 것에 실패한 경우 Http Status Code 400(Bad Request)와 예외 메시지를 리턴`() {

        doThrow(PostInvalidException("현재 상태와 동일 요청입니다.")).`when`(bookService)
            ?.updateRentStatusOfBook(1, RentStatusType.RENTING.status)

        mockMvc.perform(
            patch("/api/v1/books/1/rent-status")
                .param("rent_status", RentStatusType.RENTING.status)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `잘못된 도서의 대여 가능 여부 상태 입력으로 인해 수정하는 것에 실패한 경우 Http Status Code 400(Bad Request)와 예외 메시지를 리턴`() {

        doThrow(PostInvalidException("대여 가능, 대여중, 대여 중단만 입력하실 수 있습니다.")).`when`(bookService)
            ?.updateRentStatusOfBook(1, "잘못된 입력")

        mockMvc.perform(
            patch("/api/v1/books/1/rent-status")
                .param("rent_status", "잘못된 입력")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `존재하지 않는 도서로 인해 대여 가능 여부 상태를 수정하는 것에 실패한 경우 Http Status Code 404(Not Found)와 예외 메시지를 리턴`() {

        doThrow(PostNotFoundException("존재하지 않는 리소스입니다.")).`when`(bookService)
            ?.updateRentStatusOfBook(1123253423423, RentStatusType.RENTING.status)

        mockMvc.perform(
            patch("/api/v1/books/1123253423423/rent-status")
                .param("rent_status", RentStatusType.RENTING.status)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }


    @Throws(JsonProcessingException::class)
    private fun toJsonString(userSignUpDto: Any): String {
        return objectMapper.writeValueAsString(userSignUpDto)
    }
}