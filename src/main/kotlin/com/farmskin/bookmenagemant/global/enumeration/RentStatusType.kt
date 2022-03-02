package com.farmskin.bookmenagemant.global.enumeration

enum class RentStatusType(
    val status: String
) {
    POSSIBILITY("대여 가능"),
    RENTING("대여중"),
    STOP("대여 중단")
}