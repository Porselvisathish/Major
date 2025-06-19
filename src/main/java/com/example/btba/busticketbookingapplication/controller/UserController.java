package com.example.btba.busticketbookingapplication.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value="")
public class UserController {
    @GetMapping(value = "/hi")
    @ResponseBody
    public String home() {
        return "Hi bro!";
    }

    @GetMapping(value = "/csrf")
    @ResponseBody
    public CsrfToken getCsrtToken(HttpServletRequest request) {
        return (CsrfToken) request.getAttribute("_csrf");
    }
}
