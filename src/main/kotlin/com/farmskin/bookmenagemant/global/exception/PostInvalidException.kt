package com.farmskin.bookmenagemant.global.exception

class PostInvalidException(message: String) : RuntimeException(message) {
    constructor() : this("잘못된 요청입니다.")
}