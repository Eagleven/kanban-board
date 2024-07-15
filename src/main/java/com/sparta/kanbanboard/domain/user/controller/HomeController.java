package com.sparta.kanbanboard.domain.user.controller;

import com.sparta.kanbanboard.common.security.details.UserDetailsImpl;
import com.sparta.kanbanboard.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller

public class HomeController {
	@GetMapping("/users/")
	public String home() {
		return "index";
	}

	@GetMapping("/users/signupPage")
	public String signUp(){
		return "signup";
	}

	@GetMapping("/trello")
	public String trello(){
		return "mainpage";
	}
}