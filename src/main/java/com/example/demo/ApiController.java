package com.example.demo;

import org.springframework.web.bind.annotation.*;
//解決跨域問題（CORS）:由於前端與後端是兩個不同的 server，要設定 CORS。
@CrossOrigin(origins = "http://localhost:5173") // Vite 預設 port
@RestController
@RequestMapping("/api")
public class ApiController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello from Spring Boot!";
    }
}
