
package com.dna_testing_system.dev.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

//    @GetMapping("/")
//    public String home() {
//        return "redirect:/index";
//    }

    @GetMapping("/")
    public String index() {
        return "index";
    }
    @GetMapping("/user/home") // <--- Dòng này xử lý yêu cầu GET đến /user/home
    public String userHomePage() {
        return "user/home"; // Thymeleaf view tên user/home.html
    }
    @GetMapping("/layouts/user-layout")
    public String userLayout() {
        return "layouts/user-layout";
    }
}