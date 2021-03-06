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
    fun `?????? ????????? ????????? ?????? Http Status Code 201(Created)??? ????????? ?????? ????????? ??????`() {
        val category = GetCategoryResponse(1, "??????")
        val request = CreateBookRequest("????????? ????????? ?????? ??????", "?????????", "??????")
        val response = GetBookResponse(1, "????????? ????????? ?????? ??????", "?????????", RentStatusType.POSSIBILITY.status, category)

        given(bookService.createBook(request)).willReturn(response)

        mockMvc.perform(
            post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun `????????? ?????? ???????????? ?????? ?????? ????????? ????????? ?????? Http Status Code 400(Bad Request)??? ?????? ???????????? ??????`() {
        val request = CreateBookRequest("", " ", "??????")

        mockMvc.perform(
            post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `???????????? ?????? ??????????????? ?????? ?????? ????????? ????????? ?????? Http Status Code 404(Not Found)??? ?????? ???????????? ??????`() {
        val request = CreateBookRequest("????????? ????????? ?????? ??????", "?????????", "???????????????")

        doThrow(PostNotFoundException("?????? ???????????? ??????????????????.")).`when`(bookService).createBook(request)

        mockMvc.perform(
            post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `????????? ?????????????????? ?????? ???????????? ??????????????? ?????? ?????? ????????? ????????? ?????? Http Status Code 400(Bad Request)??? ?????? ???????????? ??????`() {
        val request = CreateBookRequest("????????? ????????? ?????? ??????", "?????????", "??????")

        doThrow(PostExistsException("?????? ???????????? ??????????????????.")).`when`(bookService).createBook(request)

        mockMvc.perform(
            post("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `???????????? ???????????? ?????? ????????? ????????? ?????? Http Status Code 200(Ok)??? ????????? ?????? ????????? ??????`() {
        val category = GetCategoryResponse(1, "??????")
        val response = GetBookResponse(1, "????????? ????????? ?????? ??????", "?????????", RentStatusType.POSSIBILITY.status, category)

        given(bookService.searchBookWithAuthorAndTitle("?????????", "????????? ????????? ?????? ??????")).willReturn(response)

        mockMvc.perform(
            get("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("author", "?????????")
                .param("title", "????????? ????????? ?????? ??????")
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `???????????? ?????? ???????????? ???????????? ?????? ????????? ????????? ?????? Http Status Code 400(Bad Request)??? ??????????????? ??????`() {

        doThrow(PostExistsException("???????????? ?????? ??????????????????.")).`when`(bookService)
            .searchBookWithAuthorAndTitle("?????????", "????????? ????????? ?????? ??????")

        mockMvc.perform(
            get("/api/v1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .param("author", "?????????")
                .param("title", "????????? ????????? ?????? ??????")
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `????????? ???????????? ????????? ????????? ?????? Http Status Code 200(Ok)??? ??????`() {

        doNothing().`when`(bookService)?.updateCategoryOfBook(1, 2)

        mockMvc.perform(
            patch("/api/v1/books/1/categories")
                .param("category_id", "2")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `?????? ???????????? ??????????????? ????????? ???????????? ?????? ????????? ???????????? ????????? ????????? ?????? Http Status Code 400(Bad Request)??? ?????? ???????????? ??????`() {


        doThrow(PostExistsException("?????? ???????????? ??????????????? ????????? ???????????????.")).`when`(bookService).updateCategoryOfBook(1, 2)

        mockMvc.perform(
            patch("/api/v1/books/1/categories")
                .param("category_id", "2")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `???????????? ?????? ????????? ???????????? ?????? ????????? ???????????? ????????? ????????? ?????? Http Status Code 404(Not Found)??? ?????? ???????????? ??????`() {


        doThrow(PostNotFoundException("???????????? ?????? ??????????????????.")).`when`(bookService).updateCategoryOfBook(100000, 2)

        mockMvc.perform(
            patch("/api/v1/books/100000/categories")
                .param("category_id", "2")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `???????????? ?????? ??????????????? ???????????? ?????? ????????? ???????????? ????????? ????????? ?????? Http Status Code 404(Not Found)??? ?????? ???????????? ??????`() {


        doThrow(PostNotFoundException("???????????? ?????? ??????????????????.")).`when`(bookService).updateCategoryOfBook(1, 200000)

        mockMvc.perform(
            patch("/api/v1/books/1/categories")
                .param("category_id", "200000")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
    }

    @Test
    fun `????????? ?????? ?????? ?????? ????????? ???????????? ?????? ????????? ?????? Http Status Code 200(Ok)??? ??????`() {


        doNothing().`when`(bookService)?.updateRentStatusOfBook(1, RentStatusType.RENTING.status)

        mockMvc.perform(
            patch("/api/v1/books/1/rent-status")
                .param("rent_status", RentStatusType.RENTING.status)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Test
    fun `?????? ????????? ????????? ????????? ?????? ?????? ?????? ?????? ???????????? ?????? ???????????? ?????? ????????? ?????? Http Status Code 400(Bad Request)??? ?????? ???????????? ??????`() {

        doThrow(PostInvalidException("?????? ????????? ?????? ???????????????.")).`when`(bookService)
            ?.updateRentStatusOfBook(1, RentStatusType.RENTING.status)

        mockMvc.perform(
            patch("/api/v1/books/1/rent-status")
                .param("rent_status", RentStatusType.RENTING.status)
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `????????? ????????? ?????? ?????? ?????? ?????? ???????????? ?????? ???????????? ?????? ????????? ?????? Http Status Code 400(Bad Request)??? ?????? ???????????? ??????`() {

        doThrow(PostInvalidException("?????? ??????, ?????????, ?????? ????????? ???????????? ??? ????????????.")).`when`(bookService)
            ?.updateRentStatusOfBook(1, "????????? ??????")

        mockMvc.perform(
            patch("/api/v1/books/1/rent-status")
                .param("rent_status", "????????? ??????")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `???????????? ?????? ????????? ?????? ?????? ?????? ?????? ????????? ???????????? ?????? ????????? ?????? Http Status Code 404(Not Found)??? ?????? ???????????? ??????`() {

        doThrow(PostNotFoundException("???????????? ?????? ??????????????????.")).`when`(bookService)
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