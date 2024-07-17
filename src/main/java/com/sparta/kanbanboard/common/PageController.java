package com.sparta.kanbanboard.common;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @GetMapping("/trello/board")
    public String mainPage(){
        return "mainpage";
    }

    @GetMapping("/trello/signupPage")
    public String signUpPage() {
        return "signup";
    }

    @GetMapping("/trello")
    public String indexPage() {
        return "index";
    }
}
