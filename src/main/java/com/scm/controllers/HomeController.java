package com.scm.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scm.dao.SUserRepository;
import com.scm.helper.Message;
import com.scm.models.SUser;

@Controller
public class HomeController {

	@Autowired
	private SUserRepository userRepo;

	@Autowired
	private PasswordEncoder encoder;
	
	@GetMapping(path = "/")
	public String home(Model model) {
		model.addAttribute("title", "Home - Spring Contact Manager");
		return "home";
	}

	@GetMapping(path = "/about")
	public String about(Model model) {
		model.addAttribute("title", "about us - Spring Contact Manager");
		return "about";
	}

	@GetMapping(path = "/signup")
	public String sighUp(Model model) {
		model.addAttribute("title", "Register - Spring Contact Manager");
		model.addAttribute("user", new SUser());
		return "signup";
	}

	@PostMapping(path = "/do_register")
	public String doRegister(@Valid @ModelAttribute("user") SUser user, BindingResult result,
			@RequestParam(value = "termscheckbox", defaultValue = "false") boolean termscheckbox, Model model,
			HttpSession session) {

		try {
			if (!termscheckbox) {
				System.out.println("Please accept the terms and conditions");
				throw new Exception("Please accept the terms and conditions");
			}
			if (result.hasErrors()) {
				System.out.println("Error:" + result.toString());
				model.addAttribute("user", user);
				return "signup";
			}
			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImgUrl("default.png");
			user.setPassword(encoder.encode(user.getPassword()));
			System.out.println("Agreement checked:" + termscheckbox);
			System.out.println("User Registered: " + user);
			this.userRepo.save(user);

			model.addAttribute("user", new SUser());
			session.setAttribute("message", new Message("Registered successfully", "alert-success"));
			return "signup";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);
			session.setAttribute("message", new Message("" + e.getMessage(), "alert-danger"));
			return "signup";
		}

	}
	
	@GetMapping(path = "/signin")
	public String login(Model model) {
		model.addAttribute("title","Login Page");
		return "login";
	}

}
