package com.github.arhor.aws.microservices.playground.users.controller

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class FaviconController {

    @GetMapping("favicon.ico")
    fun getEmptyFavicon() = ResponseEntity.ok("")
}
