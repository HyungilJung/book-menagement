package com.farmskin.bookmenagemant.ui

import com.farmskin.bookmenagemant.global.exception.PostExistsException
import com.farmskin.bookmenagemant.global.exception.PostInvalidException
import com.farmskin.bookmenagemant.global.exception.PostNotFoundException
import com.farmskin.bookmenagemant.ui.dto.ErrorResponse
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionRestController {

    companion object {
        private val logger: Logger = LogManager.getLogger(this)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(exception: MethodArgumentNotValidException): ErrorResponse? {
        val message = exception.bindingResult.fieldError!!.defaultMessage
        return message?.let { ErrorResponse.of(it) }.also { logger.debug(exception.message, exception) }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(PostNotFoundException::class)
    fun handlePostNotFoundException(exception: PostNotFoundException) =
        exception.message?.let { ErrorResponse.of(it) }.also { logger.debug(exception.message, exception) }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PostExistsException::class)
    fun handlePostExistsException(exception: PostExistsException) =
        exception.message?.let { ErrorResponse.of(it) }.also { logger.debug(exception.message, exception) }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PostInvalidException::class)
    fun handlePostInvalidException(exception: PostInvalidException) =
        exception.message?.let { ErrorResponse.of(it) }.also { logger.debug(exception.message, exception) }
}