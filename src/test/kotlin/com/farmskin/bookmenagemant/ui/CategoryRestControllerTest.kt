package com.farmskin.bookmenagemant.ui

import com.farmskin.bookmenagemant.application.CategoryService
import com.farmskin.bookmenagemant.global.enumeration.RentStatusType
import com.farmskin.bookmenagemant.global.exception.PostExistsException
import com.farmskin.bookmenagemant.global.exception.PostNotFoundException
import com.farmskin.bookmenagemant.ui.dto.request.CreateCategoryRequest
import com.farmskin.bookmenagemant.ui.dto.response.GetBookResponse
import com.farmskin.bookmenagemant.ui.dto.response.GetCategoryResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.doThrow
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.filter.CharacterEncodingFilter


@WebMvcTest(CategoryRestController::class)
internal class CategoryRestControllerTest @Autowired constructor(
    private val objectMapper: ObjectMapper,
    private val webApplicationContext: WebApplicationContext,
    private var mockMvc: MockMvc,
) {
    @MockBean
    private lateinit var categoryService: CategoryService

    @BeforeEach
    fun setUp() {

        this.mockMvc = MockMvcBuilders
            .webAppContextSetup(webApplicationContext)
            .alwaysDo<DefaultMockMvcBuilder>(MockMvcResultHandlers.print())
            .addFilter<DefaultMockMvcBuilder>(CharacterEncodingFilter("UTF-8", true))
            .build()
    }

    @Test
    fun `카테고리 생성에 성공할 경우 Http Status Code 201(Created)과 생성된 카테고리 정보를 리턴`() {
        val request = CreateCategoryRequest("문학")
        val response = GetCategoryResponse(1, request.name)

        given(categoryService.createCategory(request)).willReturn(response)

        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isCreated)
    }

    @Test
    fun `이미 존재하는 카테고리로 인해 카테고리 생성에 실패할 경우 Http Status Code 404(Bad Request)와 예외메시지 리턴`() {
        val request = CreateCategoryRequest("문학")

        doThrow(PostExistsException("이미 존재하는 리소스입니다.")).`when`(categoryService).createCategory(request)

        mockMvc.perform(
            post("/api/v1/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJsonString(request))
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `카테고리별 도서 검색에 성공할 경우 Http Status Code 200(Ok)과 카테고리별 도서 정보를 리턴`() {
        val pageable: Pageable = PageRequest.of(0, 10)
        val category = GetCategoryResponse(1, "문학")
        val response = arrayListOf<GetBookResponse>()
        response.add(GetBookResponse(1, "너에게 해주지 못한 말들", "권태영", RentStatusType.POSSIBILITY.status, category))

        given(categoryService.searchBooksByCategory(1, pageable)).willReturn(response)

        mockMvc.perform(
            get("/api/v1/categories/1/books")
                .contentType(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
    }

    @Throws(JsonProcessingException::class)
    private fun toJsonString(userSignUpDto: Any): String {
        return objectMapper.writeValueAsString(userSignUpDto)
    }
}