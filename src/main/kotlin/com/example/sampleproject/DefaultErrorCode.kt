package com.example.sampleproject

import org.springframework.http.HttpStatus

enum class DefaultErrorCode(
    val status: HttpStatus,
    val code: String,
    val message: String
) {
    ERR000(HttpStatus.INTERNAL_SERVER_ERROR, "ERR000", "알 수 없는 에러가 발생했습니다."),
    ERR001(HttpStatus.BAD_REQUEST, "ERR001", "요청이 잘못된 형식입니다."),
    ERR002(HttpStatus.UNAUTHORIZED, "ERR002", "잘못된 인증 토큰입니다.");

}