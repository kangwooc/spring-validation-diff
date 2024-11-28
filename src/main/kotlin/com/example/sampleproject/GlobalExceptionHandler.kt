package com.example.sampleproject

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.beans.BeanInstantiationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException


@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(Exception::class)
    fun handlingException(request: HttpServletRequest, ex: Exception): ResponseEntity<ErrorResponse> {
        val errorDto = ErrorResponse(
            errorCode = "DefaultErrorCode.ERR000.code",
            message = ex.message ?: "DefaultErrorCode.ERR000.message"
        )
        return ResponseEntity(errorDto, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(AccessDeniedException::class)
    fun handlingAuthorizationException(
        request: HttpServletRequest,
        ex: AccessDeniedException
    ): ResponseEntity<ErrorResponse> {
        val errorDto = ErrorResponse(
            errorCode = DefaultErrorCode.ERR002.code,
            message = ex.message ?: "DefaultErrorCode.ERR000.message"
        )
        return ResponseEntity(errorDto, HttpStatus.FORBIDDEN)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handlingIllegalArgumentException(
        request: HttpServletRequest,
        ex: IllegalArgumentException
    ): ResponseEntity<ErrorResponse> {
        val errorCode = DefaultErrorCode.ERR001
        val errorDto = ErrorResponse(errorCode = errorCode.code, message = ex.message ?: errorCode.message)
        return ResponseEntity(errorDto, errorCode.status)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handlingMethodArgumentNotValidException(
        request: HttpServletRequest,
        ex: MethodArgumentNotValidException
    ): ResponseEntity<*> {
        val msg = ex.bindingResult.fieldErrors.joinToString(", ") { it.defaultMessage ?: "" }
        return ResponseEntity(
            ErrorResponse(message = msg, errorCode = DefaultErrorCode.ERR001.code),
            DefaultErrorCode.ERR001.status
        )
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    @ResponseBody
    fun handlingDtoTypeMissMatchException(
        request: HttpServletRequest,
        ex: HttpMessageNotReadableException
    ): ResponseEntity<ErrorResponse> {
        val msg = when (val causeException = ex.cause) {
            is InvalidFormatException -> {
                "입력 받은 ${causeException.value} 를 ${causeException.targetType} 으로 변환중 에러가 발생했습니다."
            }

            is MismatchedInputException -> {
                "Parameter is missing: ${causeException.message}"
            }

            else -> "요청을 역직렬화 하는과정에서 예외가 발생했습니다."
        }

        return ResponseEntity(
            ErrorResponse(message = msg, errorCode = DefaultErrorCode.ERR001.code),
            DefaultErrorCode.ERR001.status
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handlingConstraintViolationException(
        request: HttpServletRequest,
        ex: ConstraintViolationException
    ): ResponseEntity<ErrorResponse> {
        val exceptionResult = ErrorResponse(DefaultErrorCode.ERR001.code, ex.message ?: DefaultErrorCode.ERR001.message)
        return ResponseEntity(exceptionResult, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handlingMethodArgumentTypeMismatchException(
        request: HttpServletRequest,
        ex: MethodArgumentTypeMismatchException
    ): ResponseEntity<ErrorResponse> {
        val exceptionResult = ErrorResponse(DefaultErrorCode.ERR001.code, ex.message ?: DefaultErrorCode.ERR001.message)
        return ResponseEntity(exceptionResult, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BeanInstantiationException::class)
    fun handlingBeanInstantiationException(
        request: HttpServletRequest,
        ex: BeanInstantiationException
    ): ResponseEntity<ErrorResponse> {
        val exceptionResult = ErrorResponse(DefaultErrorCode.ERR001.code,  ex.cause?.message ?: DefaultErrorCode.ERR001.message)
        return ResponseEntity(exceptionResult, HttpStatus.BAD_REQUEST)
    }
}
