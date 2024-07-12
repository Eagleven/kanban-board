package com.sparta.kanbanboard.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {
    @GetMapping("/signupPage")
    public String signUpPage() {
        return "signup";
    }

    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }
}
