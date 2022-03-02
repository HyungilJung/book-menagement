package com.farmskin.bookmenagemant.global.exception

class PostNotFoundException(message: String) : RuntimeException(message) {
    constructor() : this("존재하지 않는 리소스입니다.")
}