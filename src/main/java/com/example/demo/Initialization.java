package com.example.demo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Initialization {
    @GetMapping("/")
    public String index() {
        return "index";  // 對應到 resources/templates/index.html 或 resources/static/index.html
    }
}
