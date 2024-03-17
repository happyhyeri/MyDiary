package org.edupoll.app.controller;

import org.edupoll.app.command.AddAccount;
import org.edupoll.app.service.AccountRegisterService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/auth")
@Controller
public class AuthController {
	
	
	private final AccountRegisterService accountRegisterService;

	@GetMapping("/login")
	 public String showLoginForm(Model model) {
		
		return "auth/login";
	}
	
	
	@GetMapping("/register")
	public String showRegisterForm(Model model) {
			
		return "auth/register";
	}
	
	@PostMapping("/register")
	public String proceedRegisterForm(AddAccount cmd ,Model model) {
		if(!accountRegisterService.isAvailable(cmd)) {
			return "redirect:/auth/register";
		}
		
		accountRegisterService.exeucte(cmd);
		return "redirect:/auth/login";

	}
}
