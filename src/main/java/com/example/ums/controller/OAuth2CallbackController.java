package com.example.ums.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/oauth2")
public class OAuth2CallbackController {

    @GetMapping("/callback")
    String handleCallback(@RequestParam("code") String code) {
        return "Hey, user!" + code;
    }
}
