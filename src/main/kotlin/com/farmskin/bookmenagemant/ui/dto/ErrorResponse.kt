package com.farmskin.bookmenagemant.ui.dto

data class ErrorResponse(val message: String) {

    companion object {
        fun of(message: String): ErrorResponse {
            return ErrorResponse(message)
        }
    }
}