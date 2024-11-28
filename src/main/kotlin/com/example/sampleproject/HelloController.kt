package com.example.sampleproject

import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/hello")
class HelloController {
    @GetMapping
    fun findSomething(
        @Valid @ModelAttribute request: SampleRequest
    ): String {
        return "hello"
    }

    @PostMapping
    fun createSomething(
        @Valid @RequestBody request: SampleRequest
    ): String {
        return "hello"
    }
}